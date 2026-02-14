package org.openmbee.gearshift.intellij.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.openmbee.gearshift.kerml.parser.KerMLParseError

/**
 * External annotator that runs the project-wide [KerMLModelCache] in the
 * background and highlights:
 * - Parse errors (red squiggles)
 * - Duplicate namespace warnings (yellow squiggles on the namespace declaration)
 */
class KerMLExternalAnnotator :
    ExternalAnnotator<KerMLExternalAnnotator.Info, KerMLModelCache.AnnotationResult>() {

    data class Info(
        val text: String,
        val filePath: String,
        val modificationStamp: Long,
        val document: Document,
        val file: PsiFile
    )

    override fun collectInformation(file: PsiFile): Info? {
        val virtualFile = file.virtualFile ?: return null
        val document = file.viewProvider.document ?: return null
        return Info(
            text = file.text,
            filePath = virtualFile.path,
            modificationStamp = virtualFile.modificationStamp,
            document = document,
            file = file
        )
    }

    override fun doAnnotate(info: Info): KerMLModelCache.AnnotationResult {
        val project = info.file.project
        val cache = KerMLModelCache.getInstance(project)
        return cache.getParseResult(info.filePath, info.modificationStamp, info.text)
    }

    override fun apply(file: PsiFile, result: KerMLModelCache.AnnotationResult, holder: AnnotationHolder) {
        val document = file.viewProvider.document ?: return

        // Parse errors → ERROR severity
        for (error in result.parseErrors) {
            val range = errorToTextRange(error, document) ?: continue
            holder.newAnnotation(HighlightSeverity.ERROR, error.message)
                .range(range)
                .create()
        }

        // Duplicate namespace warnings → WARNING severity on first line
        for (warning in result.duplicateWarnings) {
            val namespaceName = extractNameFromWarning(warning)
            val range = findNamespaceDeclarationRange(namespaceName, document)
            if (range != null) {
                holder.newAnnotation(HighlightSeverity.WARNING, warning)
                    .range(range)
                    .create()
            } else {
                // Fallback: annotate first line of file
                val lineEnd = document.getLineEndOffset(0).coerceAtMost(document.textLength)
                if (lineEnd > 0) {
                    holder.newAnnotation(HighlightSeverity.WARNING, warning)
                        .range(TextRange(0, lineEnd))
                        .create()
                }
            }
        }
    }

    private fun errorToTextRange(error: KerMLParseError, document: Document): TextRange? {
        val line = error.line - 1
        if (line < 0 || line >= document.lineCount) return null

        val lineStart = document.getLineStartOffset(line)
        val lineEnd = document.getLineEndOffset(line)
        val start = (lineStart + error.column).coerceIn(lineStart, lineEnd)

        val symbol = error.offendingSymbol
        val end = if (symbol != null) {
            (start + symbol.length).coerceIn(start, lineEnd)
        } else {
            (start + 1).coerceIn(start, lineEnd)
        }

        if (start >= end) return null
        return TextRange(start, end)
    }

    companion object {
        private val NS_DECLARATION_PATTERN = Regex(
            """(?:package|namespace)\s+(?:'([^']+)'|(\w+))"""
        )

        /** Extract "Foo" from "Namespace 'Foo' is also defined in: bar.kerml" */
        private fun extractNameFromWarning(warning: String): String? {
            val match = Regex("Namespace '([^']+)'").find(warning)
            return match?.groupValues?.get(1)
        }

        /** Find the text range of the namespace declaration name in the document. */
        private fun findNamespaceDeclarationRange(name: String?, document: Document): TextRange? {
            if (name == null) return null
            val text = document.text
            val match = NS_DECLARATION_PATTERN.find(text) ?: return null
            val declaredName = match.groupValues[1].ifEmpty { match.groupValues[2] }
            if (declaredName != name) return null
            // Highlight the name portion
            val nameStart = match.range.first + match.value.indexOf(declaredName)
            val nameEnd = nameStart + declaredName.length
            return TextRange(nameStart, nameEnd)
        }
    }
}
