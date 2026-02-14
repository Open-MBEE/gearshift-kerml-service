package org.openmbee.gearshift.intellij.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.openmbee.gearshift.intellij.parser.KerMLElementTypes

object KerMLPsiUtil {
    fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            KerMLElementTypes.DECLARATION -> KerMLDeclaration(node)
            KerMLElementTypes.BODY_BLOCK -> KerMLBodyBlock(node)
            KerMLElementTypes.IMPORT_STATEMENT -> KerMLImportStatement(node)
            KerMLElementTypes.NAME_REF -> KerMLNameRef(node)
            else -> com.intellij.extapi.psi.ASTWrapperPsiElement(node)
        }
    }
}
