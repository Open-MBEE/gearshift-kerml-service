package org.openmbee.gearshift.intellij

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object KerMLFileType : LanguageFileType(KerMLLanguage) {
    override fun getName(): String = "KerML"
    override fun getDescription(): String = "KerML model file"
    override fun getDefaultExtension(): String = "kerml"
    override fun getIcon(): Icon = KerMLIcons.FILE
}
