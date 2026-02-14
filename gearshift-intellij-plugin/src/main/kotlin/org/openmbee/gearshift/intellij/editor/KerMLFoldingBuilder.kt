package org.openmbee.gearshift.intellij.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.openmbee.gearshift.intellij.parser.KerMLElementTypes
import org.openmbee.gearshift.intellij.psi.KerMLBodyBlock
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration

class KerMLFoldingBuilder : FoldingBuilderEx() {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = mutableListOf<FoldingDescriptor>()

        PsiTreeUtil.findChildrenOfType(root, KerMLBodyBlock::class.java).forEach { bodyBlock ->
            val range = bodyBlock.textRange
            if (range.length > 2) { // at least { + }
                val group = FoldingGroup.newGroup("kerml-body")
                descriptors.add(FoldingDescriptor(bodyBlock.node, range, group))
            }
        }

        // Fold multi-line comments
        root.node.getChildren(null).forEach { child ->
            foldComments(child, descriptors)
        }

        return descriptors.toTypedArray()
    }

    private fun foldComments(node: ASTNode, descriptors: MutableList<FoldingDescriptor>) {
        if (node.elementType == org.openmbee.gearshift.intellij.parser.KerMLTokenTypes.MULTILINE_NOTE ||
            node.elementType == org.openmbee.gearshift.intellij.parser.KerMLTokenTypes.REGULAR_COMMENT
        ) {
            val range = node.textRange
            if (range.length > 4) { // at least /* */
                descriptors.add(FoldingDescriptor(node, range))
            }
        }
        node.getChildren(null).forEach { foldComments(it, descriptors) }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return when (node.elementType) {
            KerMLElementTypes.BODY_BLOCK -> "{...}"
            org.openmbee.gearshift.intellij.parser.KerMLTokenTypes.MULTILINE_NOTE -> "/* ... */"
            org.openmbee.gearshift.intellij.parser.KerMLTokenTypes.REGULAR_COMMENT -> "/* ... */"
            else -> "..."
        }
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
