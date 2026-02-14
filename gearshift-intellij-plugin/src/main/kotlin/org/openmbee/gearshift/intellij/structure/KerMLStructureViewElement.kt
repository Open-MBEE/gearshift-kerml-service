package org.openmbee.gearshift.intellij.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.openmbee.gearshift.intellij.KerMLIcons
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration
import org.openmbee.gearshift.intellij.psi.KerMLFile
import javax.swing.Icon

class KerMLStructureViewElement(private val element: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getAlphaSortKey(): String {
        return when (element) {
            is KerMLDeclaration -> element.declaredName ?: ""
            is KerMLFile -> element.name
            else -> ""
        }
    }

    override fun getPresentation(): ItemPresentation {
        return when (element) {
            is KerMLDeclaration -> {
                val keyword = element.declarationKeyword ?: "element"
                val name = element.declaredName ?: "<anonymous>"
                PresentationData(name, keyword, iconForKeyword(keyword), null)
            }
            is KerMLFile -> PresentationData(element.name, null, KerMLIcons.FILE, null)
            else -> PresentationData(element.text?.take(30) ?: "", null, null, null)
        }
    }

    override fun getChildren(): Array<TreeElement> {
        return when (element) {
            is KerMLFile -> {
                PsiTreeUtil.findChildrenOfType(element, KerMLDeclaration::class.java)
                    .filter { it.parent is KerMLFile || it.parent?.parent is KerMLFile }
                    .map { KerMLStructureViewElement(it) }
                    .toTypedArray()
            }
            is KerMLDeclaration -> {
                element.nestedDeclarations
                    .map { KerMLStructureViewElement(it) }
                    .toTypedArray()
            }
            else -> emptyArray()
        }
    }

    private fun iconForKeyword(keyword: String): Icon {
        return when (keyword) {
            "package", "namespace", "library" -> KerMLIcons.PACKAGE
            "class", "classifier", "datatype", "struct", "assoc",
            "behavior", "function", "predicate", "interaction", "metaclass" -> KerMLIcons.CLASS
            else -> KerMLIcons.FEATURE
        }
    }
}
