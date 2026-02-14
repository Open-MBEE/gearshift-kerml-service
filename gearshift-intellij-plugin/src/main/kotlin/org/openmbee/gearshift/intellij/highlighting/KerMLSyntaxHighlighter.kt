package org.openmbee.gearshift.intellij.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.openmbee.gearshift.intellij.parser.KerMLLexerAdapter
import org.openmbee.gearshift.intellij.parser.KerMLTokenTypes

class KerMLSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = KerMLLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when {
            KerMLTokenTypes.DECLARATION_KEYWORDS.contains(tokenType) -> DECLARATION_KEYWORD_KEYS
            KerMLTokenTypes.MODIFIER_KEYWORDS.contains(tokenType) -> MODIFIER_KEYWORD_KEYS
            KerMLTokenTypes.RELATIONSHIP_KEYWORDS.contains(tokenType) -> RELATIONSHIP_KEYWORD_KEYS
            KerMLTokenTypes.OTHER_KEYWORDS.contains(tokenType) -> KEYWORD_KEYS

            tokenType == KerMLTokenTypes.SINGLE_LINE_NOTE -> LINE_COMMENT_KEYS
            tokenType == KerMLTokenTypes.MULTILINE_NOTE -> BLOCK_COMMENT_KEYS
            tokenType == KerMLTokenTypes.REGULAR_COMMENT -> DOC_COMMENT_KEYS

            tokenType == KerMLTokenTypes.STRING_VALUE -> STRING_KEYS
            tokenType == KerMLTokenTypes.DECIMAL_VALUE -> NUMBER_KEYS
            tokenType == KerMLTokenTypes.EXPONENTIAL_VALUE -> NUMBER_KEYS

            tokenType == KerMLTokenTypes.NAME -> IDENTIFIER_KEYS

            tokenType == KerMLTokenTypes.LBRACE || tokenType == KerMLTokenTypes.RBRACE -> BRACES_KEYS
            tokenType == KerMLTokenTypes.LBRACKET || tokenType == KerMLTokenTypes.RBRACKET -> BRACKETS_KEYS
            tokenType == KerMLTokenTypes.LPAREN || tokenType == KerMLTokenTypes.RPAREN -> PARENTHESES_KEYS
            tokenType == KerMLTokenTypes.SEMICOLON -> SEMICOLON_KEYS
            tokenType == KerMLTokenTypes.COMMA -> COMMA_KEYS
            tokenType == KerMLTokenTypes.DOT -> DOT_KEYS

            KerMLTokenTypes.OPERATORS.contains(tokenType) -> OPERATOR_KEYS

            tokenType == TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS

            else -> EMPTY_KEYS
        }
    }

    companion object {
        // Text attribute keys
        val DECLARATION_KEYWORD = createTextAttributesKey("KERML_DECLARATION_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val MODIFIER_KEYWORD = createTextAttributesKey("KERML_MODIFIER_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val RELATIONSHIP_KEYWORD = createTextAttributesKey("KERML_RELATIONSHIP_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val KEYWORD = createTextAttributesKey("KERML_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val IDENTIFIER = createTextAttributesKey("KERML_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val STRING = createTextAttributesKey("KERML_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("KERML_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val LINE_COMMENT = createTextAttributesKey("KERML_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = createTextAttributesKey("KERML_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val DOC_COMMENT = createTextAttributesKey("KERML_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT)
        val OPERATOR = createTextAttributesKey("KERML_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACES = createTextAttributesKey("KERML_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = createTextAttributesKey("KERML_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val PARENTHESES = createTextAttributesKey("KERML_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val SEMICOLON = createTextAttributesKey("KERML_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMA = createTextAttributesKey("KERML_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val DOT = createTextAttributesKey("KERML_DOT", DefaultLanguageHighlighterColors.DOT)
        val BAD_CHARACTER = createTextAttributesKey("KERML_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        // Key arrays
        private val DECLARATION_KEYWORD_KEYS = arrayOf(DECLARATION_KEYWORD)
        private val MODIFIER_KEYWORD_KEYS = arrayOf(MODIFIER_KEYWORD)
        private val RELATIONSHIP_KEYWORD_KEYS = arrayOf(RELATIONSHIP_KEYWORD)
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val LINE_COMMENT_KEYS = arrayOf(LINE_COMMENT)
        private val BLOCK_COMMENT_KEYS = arrayOf(BLOCK_COMMENT)
        private val DOC_COMMENT_KEYS = arrayOf(DOC_COMMENT)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val DOT_KEYS = arrayOf(DOT)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}
