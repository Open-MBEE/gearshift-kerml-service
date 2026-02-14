package org.openmbee.gearshift.intellij.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.openmbee.gearshift.intellij.KerMLFileType
import org.openmbee.gearshift.intellij.KerMLLanguage

class KerMLFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, KerMLLanguage) {
    override fun getFileType(): FileType = KerMLFileType
    override fun toString(): String = "KerML File"
}
