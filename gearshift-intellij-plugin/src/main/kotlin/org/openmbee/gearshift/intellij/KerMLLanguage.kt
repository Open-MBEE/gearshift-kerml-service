package org.openmbee.gearshift.intellij

import com.intellij.lang.Language

object KerMLLanguage : Language("KerML") {
    private fun readResolve(): Any = KerMLLanguage

    override fun getDisplayName(): String = "KerML"
    override fun isCaseSensitive(): Boolean = true
}
