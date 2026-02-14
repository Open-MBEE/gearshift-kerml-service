package org.openmbee.gearshift.intellij.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import org.openmbee.gearshift.intellij.parser.KerMLElementTypes
import org.openmbee.gearshift.intellij.parser.KerMLTokenTypes

/**
 * PSI element for a KerML declaration (package, class, feature, etc.).
 * Implements [PsiNameIdentifierOwner] for rename/navigation support.
 */
class KerMLDeclaration(node: ASTNode) : ASTWrapperPsiElement(node), PsiNameIdentifierOwner {

    /** The declaration keyword text (e.g., "package", "class", "feature"). */
    val declarationKeyword: String?
        get() {
            var child = node.firstChildNode
            while (child != null) {
                if (KerMLTokenTypes.DECLARATION_KEYWORDS.contains(child.elementType)) {
                    return child.text
                }
                child = child.treeNext
            }
            return null
        }

    /** The declared name, if any. */
    val declaredName: String?
        get() = nameIdentifier?.text

    override fun getNameIdentifier(): PsiElement? {
        val nameRef = node.findChildByType(KerMLElementTypes.NAME_REF) ?: return null
        // The first NAME_REF child is the declaration name
        return nameRef.psi
    }

    override fun getName(): String? = declaredName

    override fun setName(name: String): PsiElement {
        // TODO: implement rename refactoring
        return this
    }

    /** The body block, if present. */
    val bodyBlock: KerMLBodyBlock?
        get() {
            val bodyNode = node.findChildByType(KerMLElementTypes.BODY_BLOCK) ?: return null
            return bodyNode.psi as? KerMLBodyBlock
        }

    /** Nested declarations within the body block. */
    val nestedDeclarations: List<KerMLDeclaration>
        get() {
            val body = bodyBlock ?: return emptyList()
            return body.node.getChildren(null)
                .filter { it.elementType == KerMLElementTypes.DECLARATION }
                .mapNotNull { it.psi as? KerMLDeclaration }
        }
}

/**
 * PSI element for a `{ ... }` body block.
 */
class KerMLBodyBlock(node: ASTNode) : ASTWrapperPsiElement(node)

/**
 * PSI element for an import statement.
 */
class KerMLImportStatement(node: ASTNode) : ASTWrapperPsiElement(node) {
    val importedName: String?
        get() {
            val text = node.text
            // Strip 'import' prefix and ';' suffix, trim
            return text.removePrefix("import").removeSuffix(";").trim()
        }
}

/**
 * PSI element for a name reference (identifier or qualified name).
 */
class KerMLNameRef(node: ASTNode) : ASTWrapperPsiElement(node) {
    val referenceName: String?
        get() = node.text
}
