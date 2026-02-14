package org.openmbee.gearshift.intellij.parser

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import org.openmbee.gearshift.intellij.KerMLLanguage

object KerMLElementTypes {
    @JvmField val FILE = IFileElementType(KerMLLanguage)

    // Composite element types recognized by the lightweight PSI parser
    @JvmField val DECLARATION = KerMLElementType("DECLARATION")
    @JvmField val BODY_BLOCK = KerMLElementType("BODY_BLOCK")
    @JvmField val IMPORT_STATEMENT = KerMLElementType("IMPORT_STATEMENT")
    @JvmField val NAME_REF = KerMLElementType("NAME_REF")
}

class KerMLElementType(debugName: String) : IElementType(debugName, KerMLLanguage)
