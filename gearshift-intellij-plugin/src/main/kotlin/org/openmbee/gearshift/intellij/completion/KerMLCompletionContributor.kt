package org.openmbee.gearshift.intellij.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import org.openmbee.gearshift.intellij.KerMLIcons
import org.openmbee.gearshift.intellij.KerMLLanguage
import org.openmbee.gearshift.intellij.annotator.KerMLModelCache
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration
import org.openmbee.gearshift.intellij.psi.KerMLFile
import org.openmbee.gearshift.intellij.psi.KerMLImportStatement

class KerMLCompletionContributor : CompletionContributor() {

    init {
        // Import completion — qualified names from the project declaration index
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(KerMLLanguage),
            ImportCompletionProvider()
        )

        // Keyword completion
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(KerMLLanguage),
            KeywordCompletionProvider()
        )

        // In-scope name completion (current file)
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(KerMLLanguage),
            NameCompletionProvider()
        )
    }

    /**
     * Provides import completions from the project-wide declaration index.
     * Activated when the caret is inside or immediately after an `import` statement.
     *
     * Offers:
     * - Top-level qualified names: `SpacecraftDesign`, `ISQ`
     * - Nested qualified names: `SpacecraftDesign::Spacecraft`
     * - Wildcard variants: `SpacecraftDesign::*`
     */
    private class ImportCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            // Check if we're in an import context
            if (!isImportContext(parameters)) return

            val project = parameters.position.project
            val cache = KerMLModelCache.getInstance(project)
            val index = cache.getDeclarationIndex()
            val currentFile = parameters.originalFile.virtualFile?.path

            // Determine prefix for qualified name filtering
            val prefix = getImportPrefix(parameters)

            // Track offered names to avoid duplicates
            val offered = mutableSetOf<String>()

            for (entry in index) {
                // Don't suggest things from the current file
                if (entry.filePath == currentFile) continue

                val qualifiedName = entry.qualifiedName

                if (prefix.isNotEmpty()) {
                    // User has typed a prefix like "Foo::" — offer children
                    if (!qualifiedName.startsWith("$prefix::")) continue
                    val suffix = qualifiedName.removePrefix("$prefix::")
                    // Only offer direct children, not deeper nesting
                    if (suffix.contains("::")) continue

                    if (offered.add(qualifiedName)) {
                        result.addElement(
                            LookupElementBuilder.create(qualifiedName)
                                .withIcon(iconForKind(entry.kind))
                                .withTypeText("${entry.kind} (${entry.filePath.substringAfterLast('/')})")
                                .withPresentableText(suffix)
                        )
                    }
                } else {
                    // No prefix — offer top-level names
                    if (qualifiedName.contains("::")) continue

                    if (offered.add(qualifiedName)) {
                        result.addElement(
                            LookupElementBuilder.create(qualifiedName)
                                .withIcon(iconForKind(entry.kind))
                                .withTypeText("${entry.kind} (${entry.filePath.substringAfterLast('/')})")
                        )
                    }

                    // Also offer wildcard variant
                    val wildcard = "$qualifiedName::*"
                    if (offered.add(wildcard)) {
                        result.addElement(
                            LookupElementBuilder.create(wildcard)
                                .withIcon(KerMLIcons.PACKAGE)
                                .withTypeText("wildcard import")
                        )
                    }
                }
            }
        }

        private fun isImportContext(parameters: CompletionParameters): Boolean {
            // Walk up from caret position to see if we're inside an import statement
            var element = parameters.position
            while (element != null) {
                if (element is KerMLImportStatement) return true
                element = element.parent
                if (element is KerMLFile) break
            }

            // Fallback: check if the text before the caret starts with "import"
            val offset = parameters.offset
            val text = parameters.editor.document.text
            val lineStart = text.lastIndexOf('\n', offset - 1) + 1
            val lineBefore = text.substring(lineStart, offset).trimStart()
            return lineBefore.startsWith("import ")
        }

        private fun getImportPrefix(parameters: CompletionParameters): String {
            val offset = parameters.offset
            val text = parameters.editor.document.text
            val lineStart = text.lastIndexOf('\n', offset - 1) + 1
            val lineBefore = text.substring(lineStart, offset).trimStart()

            // Extract what's after "import " and before the caret
            val afterImport = lineBefore.removePrefix("import ").removePrefix("all ").trim()
            // If it ends with "::", return everything before that as the prefix
            val lastSep = afterImport.lastIndexOf("::")
            return if (lastSep >= 0) afterImport.substring(0, lastSep) else ""
        }

        private fun iconForKind(kind: String) = when (kind) {
            "package", "namespace", "library" -> KerMLIcons.PACKAGE
            "class", "classifier", "datatype", "struct", "assoc",
            "behavior", "function", "predicate", "interaction", "metaclass" -> KerMLIcons.CLASS
            else -> KerMLIcons.FEATURE
        }
    }

    private class KeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            for (keyword in KEYWORDS) {
                result.addElement(
                    LookupElementBuilder.create(keyword)
                        .bold()
                        .withTypeText("keyword")
                )
            }
        }
    }

    private class NameCompletionProvider : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            val file = parameters.originalFile as? KerMLFile ?: return
            val declarations = PsiTreeUtil.findChildrenOfType(file, KerMLDeclaration::class.java)

            for (decl in declarations) {
                val name = decl.declaredName ?: continue
                val keyword = decl.declarationKeyword ?: "element"
                result.addElement(
                    LookupElementBuilder.create(name)
                        .withIcon(KerMLIcons.FEATURE)
                        .withTypeText(keyword)
                )
            }
        }
    }

    companion object {
        private val KEYWORDS = listOf(
            "about", "abstract", "alias", "all", "and", "as", "assoc",
            "behavior", "binding", "bool", "by",
            "chains", "class", "classifier", "comment", "composite",
            "conjugate", "conjugates", "conjugation", "connector", "const", "crosses",
            "datatype", "default", "dependency", "derived", "differences",
            "disjoining", "disjoint", "doc",
            "else", "end", "expose", "expr",
            "false", "feature", "featured", "featuring", "filter",
            "first", "flow", "for", "from", "function",
            "hastype", "if", "implies", "import", "in", "inout",
            "interaction", "intersects", "inv", "inverse", "inverting", "istype",
            "language", "library", "locale",
            "member", "meta", "metaclass", "metadata", "multiplicity",
            "namespace", "new", "nonunique", "not", "null",
            "of", "or", "ordered", "out",
            "package", "portion", "predicate", "private", "protected", "public",
            "redefines", "redefinition", "references", "render", "rendering", "rep", "return",
            "specialization", "specializes", "standard", "step", "struct",
            "subclassifier", "subset", "subsets", "subtype", "succession",
            "then", "to", "true", "type", "typed", "typing",
            "unions", "var", "view", "viewpoint", "xor"
        )
    }
}
