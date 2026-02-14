package org.openmbee.gearshift.intellij.structure

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration

class KerMLStructureViewModel(file: PsiFile, editor: Editor?) :
    StructureViewModelBase(file, editor, KerMLStructureViewElement(file)),
    StructureViewModel.ElementInfoProvider {

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?): Boolean = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        val value = element?.value
        if (value is KerMLDeclaration) {
            return value.bodyBlock == null
        }
        return false
    }

    override fun getSuitableClasses(): Array<Class<*>> {
        return arrayOf(KerMLDeclaration::class.java)
    }
}
