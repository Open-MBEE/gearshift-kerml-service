package org.openmbee.gearshift.intellij.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

/**
 * Lightweight PsiBuilder parser for KerML files.
 *
 * Intentionally loose — recognizes:
 * - Declaration keyword + optional name + optional body block `{ }`
 * - Import statements
 * - Name references (qualified names used in specializations, etc.)
 *
 * Does NOT mirror the full ANTLR grammar. Just enough structure for
 * folding, structure view, and navigation. Semantic correctness comes
 * from the KerMLModel layer (via KerMLExternalAnnotator).
 */
class KerMLPsiParser : PsiParser {

    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        parseFileContent(builder)
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseFileContent(builder: PsiBuilder) {
        while (!builder.eof()) {
            if (!parseTopLevelElement(builder)) {
                // Skip unrecognized token
                builder.advanceLexer()
            }
        }
    }

    private fun parseTopLevelElement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType ?: return false

        // Skip whitespace-like tokens
        if (tokenType == TokenType.WHITE_SPACE) {
            builder.advanceLexer()
            return true
        }

        // Import statement
        if (tokenType == KerMLTokenTypes.IMPORT) {
            return parseImport(builder)
        }

        // Declaration keywords
        if (isDeclarationStart(tokenType)) {
            return parseDeclaration(builder)
        }

        // Visibility prefix before declaration
        if (isVisibility(tokenType)) {
            val lookAhead = lookAheadSkippingModifiers(builder)
            if (lookAhead != null && isDeclarationStart(lookAhead)) {
                return parseDeclaration(builder)
            }
        }

        // Modifier prefix before declaration
        if (isModifier(tokenType)) {
            val lookAhead = lookAheadSkippingModifiers(builder)
            if (lookAhead != null && isDeclarationStart(lookAhead)) {
                return parseDeclaration(builder)
            }
        }

        return false
    }

    private fun parseImport(builder: PsiBuilder): Boolean {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'import'

        // Consume everything until ';' or '{' or EOF
        while (!builder.eof()) {
            val t = builder.tokenType
            if (t == KerMLTokenTypes.SEMICOLON) {
                builder.advanceLexer()
                break
            }
            if (t == KerMLTokenTypes.LBRACE || isDeclarationStart(t)) {
                break
            }
            builder.advanceLexer()
        }

        marker.done(KerMLElementTypes.IMPORT_STATEMENT)
        return true
    }

    private fun parseDeclaration(builder: PsiBuilder): Boolean {
        val marker = builder.mark()

        // Consume visibility modifiers (public, private, protected)
        while (isVisibility(builder.tokenType)) {
            builder.advanceLexer()
        }

        // Consume other modifiers (abstract, composite, etc.)
        while (isModifier(builder.tokenType)) {
            builder.advanceLexer()
        }

        // Consume declaration keyword(s)
        if (!isDeclarationStart(builder.tokenType)) {
            marker.drop()
            return false
        }
        builder.advanceLexer()

        // Some declarations have a second keyword (e.g., "item flow", "binding connector")
        if (isDeclarationStart(builder.tokenType)) {
            builder.advanceLexer()
        }

        // Consume optional name (NAME token or keyword used as name)
        if (builder.tokenType == KerMLTokenTypes.NAME) {
            val nameMarker = builder.mark()
            builder.advanceLexer()
            nameMarker.done(KerMLElementTypes.NAME_REF)
        }

        // Consume everything until body block or semicolon
        consumeUntilBodyOrEnd(builder)

        // Parse body block if present
        if (builder.tokenType == KerMLTokenTypes.LBRACE) {
            parseBodyBlock(builder)
        }

        marker.done(KerMLElementTypes.DECLARATION)
        return true
    }

    private fun parseBodyBlock(builder: PsiBuilder) {
        val bodyMarker = builder.mark()
        builder.advanceLexer() // consume '{'

        var braceDepth = 1
        while (!builder.eof() && braceDepth > 0) {
            val tokenType = builder.tokenType

            when {
                tokenType == KerMLTokenTypes.LBRACE -> {
                    // Try to parse nested declarations
                    if (!parseTopLevelElement(builder)) {
                        builder.advanceLexer()
                    }
                    // Note: parseTopLevelElement may have consumed the brace
                    // if it was part of a nested declaration body
                    continue
                }
                tokenType == KerMLTokenTypes.RBRACE -> {
                    braceDepth--
                    if (braceDepth == 0) {
                        builder.advanceLexer() // consume '}'
                    } else {
                        builder.advanceLexer()
                    }
                }
                else -> {
                    if (!parseTopLevelElement(builder)) {
                        builder.advanceLexer()
                    }
                }
            }
        }

        bodyMarker.done(KerMLElementTypes.BODY_BLOCK)
    }

    private fun consumeUntilBodyOrEnd(builder: PsiBuilder) {
        // Consume tokens that are part of the declaration header
        // (typing, specialization, multiplicity, etc.) until we hit
        // '{', ';', or another declaration start at the same level
        while (!builder.eof()) {
            val t = builder.tokenType
            if (t == KerMLTokenTypes.LBRACE || t == KerMLTokenTypes.SEMICOLON) {
                if (t == KerMLTokenTypes.SEMICOLON) {
                    builder.advanceLexer() // consume ';'
                }
                return
            }
            if (t == KerMLTokenTypes.RBRACE) return

            // If we see a qualified name reference (e.g., Foo::Bar, :> Subsystem)
            if (t == KerMLTokenTypes.NAME) {
                val nameMarker = builder.mark()
                builder.advanceLexer()
                // Check for qualified name continuation
                while (builder.tokenType == KerMLTokenTypes.DOUBLE_COLON && !builder.eof()) {
                    builder.advanceLexer() // ::
                    if (builder.tokenType == KerMLTokenTypes.NAME) {
                        builder.advanceLexer()
                    }
                }
                nameMarker.done(KerMLElementTypes.NAME_REF)
                continue
            }

            builder.advanceLexer()
        }
    }

    private fun lookAheadSkippingModifiers(builder: PsiBuilder): IElementType? {
        var steps = 1
        while (true) {
            val t = builder.lookAhead(steps) ?: return null
            if (!isModifier(t) && !isVisibility(t)) return t
            steps++
        }
    }

    companion object {
        private val declarationKeywords = setOf(
            KerMLTokenTypes.PACKAGE, KerMLTokenTypes.NAMESPACE,
            KerMLTokenTypes.CLASS, KerMLTokenTypes.CLASSIFIER,
            KerMLTokenTypes.DATATYPE, KerMLTokenTypes.STRUCT,
            KerMLTokenTypes.ASSOC, KerMLTokenTypes.BEHAVIOR,
            KerMLTokenTypes.FUNCTION, KerMLTokenTypes.PREDICATE,
            KerMLTokenTypes.INTERACTION, KerMLTokenTypes.METACLASS,
            KerMLTokenTypes.FEATURE, KerMLTokenTypes.STEP,
            KerMLTokenTypes.EXPR, KerMLTokenTypes.BOOL,
            KerMLTokenTypes.CONNECTOR, KerMLTokenTypes.BINDING,
            KerMLTokenTypes.SUCCESSION, KerMLTokenTypes.FLOW,
            KerMLTokenTypes.VIEW, KerMLTokenTypes.VIEWPOINT,
            KerMLTokenTypes.RENDERING, KerMLTokenTypes.ALIAS,
            KerMLTokenTypes.COMMENT, KerMLTokenTypes.DOC,
            KerMLTokenTypes.SPECIALIZATION, KerMLTokenTypes.CONJUGATION,
            KerMLTokenTypes.DISJOINING, KerMLTokenTypes.REDEFINITION,
            KerMLTokenTypes.SUBSET, KerMLTokenTypes.SUBCLASSIFIER,
            KerMLTokenTypes.SUBTYPE, KerMLTokenTypes.TYPING,
            KerMLTokenTypes.FEATURING, KerMLTokenTypes.INVERTING,
            KerMLTokenTypes.MULTIPLICITY, KerMLTokenTypes.DEPENDENCY,
            KerMLTokenTypes.METADATA, KerMLTokenTypes.REP,
            KerMLTokenTypes.INV, KerMLTokenTypes.LIBRARY
        )

        private val modifierKeywords = setOf(
            KerMLTokenTypes.ABSTRACT, KerMLTokenTypes.COMPOSITE,
            KerMLTokenTypes.CONST, KerMLTokenTypes.DERIVED,
            KerMLTokenTypes.END, KerMLTokenTypes.ORDERED,
            KerMLTokenTypes.NONUNIQUE, KerMLTokenTypes.PORTION,
            KerMLTokenTypes.STANDARD, KerMLTokenTypes.VAR,
            KerMLTokenTypes.DISJOINT
        )

        private val visibilityKeywords = setOf(
            KerMLTokenTypes.PRIVATE, KerMLTokenTypes.PROTECTED, KerMLTokenTypes.PUBLIC
        )

        fun isDeclarationStart(tokenType: IElementType?): Boolean =
            tokenType != null && tokenType in declarationKeywords

        fun isModifier(tokenType: IElementType?): Boolean =
            tokenType != null && tokenType in modifierKeywords

        fun isVisibility(tokenType: IElementType?): Boolean =
            tokenType != null && tokenType in visibilityKeywords
    }
}
