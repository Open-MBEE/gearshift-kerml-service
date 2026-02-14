package org.openmbee.gearshift.intellij.parser

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import org.openmbee.gearshift.kerml.antlr.KerMLLexer

/**
 * Wraps the ANTLR [KerMLLexer] as an IntelliJ [LexerBase].
 *
 * ANTLR lexers are not incremental — on each [start] call we re-lex the entire buffer range
 * and cache the resulting token list. IntelliJ calls [advance]/[getTokenType] sequentially.
 */
class KerMLLexerAdapter : LexerBase() {

    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0

    private var tokens: List<LexedToken> = emptyList()
    private var tokenIndex = 0

    private data class LexedToken(
        val type: IElementType,
        val start: Int,
        val end: Int
    )

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenIndex = 0

        val text = buffer.subSequence(startOffset, endOffset).toString()
        val lexer = KerMLLexer(CharStreams.fromString(text))
        lexer.removeErrorListeners() // suppress console noise

        val result = mutableListOf<LexedToken>()
        var lastEnd = startOffset

        while (true) {
            val token = lexer.nextToken()
            if (token.type == Token.EOF) break

            val tokStart = startOffset + token.startIndex
            val tokEnd = startOffset + token.stopIndex + 1

            // Fill any gap with BAD_CHARACTER (shouldn't happen with a correct lexer)
            if (tokStart > lastEnd) {
                result.add(LexedToken(TokenType.BAD_CHARACTER, lastEnd, tokStart))
            }

            val elementType = mapTokenType(token.type)
            result.add(LexedToken(elementType, tokStart, tokEnd))
            lastEnd = tokEnd
        }

        // Fill trailing gap
        if (lastEnd < endOffset) {
            result.add(LexedToken(TokenType.BAD_CHARACTER, lastEnd, endOffset))
        }

        tokens = result
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? {
        if (tokenIndex >= tokens.size) return null
        return tokens[tokenIndex].type
    }

    override fun getTokenStart(): Int {
        if (tokenIndex >= tokens.size) return endOffset
        return tokens[tokenIndex].start
    }

    override fun getTokenEnd(): Int {
        if (tokenIndex >= tokens.size) return endOffset
        return tokens[tokenIndex].end
    }

    override fun advance() {
        tokenIndex++
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    private fun mapTokenType(antlrType: Int): IElementType {
        // Whitespace and line terminators → IntelliJ WHITE_SPACE
        return when (antlrType) {
            KerMLLexer.WS, KerMLLexer.LINE_TERMINATOR -> TokenType.WHITE_SPACE
            else -> KerMLTokenTypes.fromAntlrType(antlrType)
        }
    }
}
