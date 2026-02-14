package org.openmbee.gearshift.intellij.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.openmbee.gearshift.intellij.parser.KerMLTokenTypes

class KerMLBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(KerMLTokenTypes.LBRACE, KerMLTokenTypes.RBRACE, true),
            BracePair(KerMLTokenTypes.LBRACKET, KerMLTokenTypes.RBRACKET, false),
            BracePair(KerMLTokenTypes.LPAREN, KerMLTokenTypes.RPAREN, false)
        )
    }
}
