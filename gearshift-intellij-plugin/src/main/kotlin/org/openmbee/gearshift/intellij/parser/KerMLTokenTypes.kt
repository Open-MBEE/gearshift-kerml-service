package org.openmbee.gearshift.intellij.parser

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import org.openmbee.gearshift.intellij.KerMLLanguage
import org.openmbee.gearshift.kerml.antlr.KerMLLexer

class KerMLTokenType(debugName: String) : IElementType(debugName, KerMLLanguage)

object KerMLTokenTypes {

    // --- Keywords (1-107) ---
    @JvmField val ABOUT = KerMLTokenType("ABOUT")
    @JvmField val ABSTRACT = KerMLTokenType("ABSTRACT")
    @JvmField val ALIAS = KerMLTokenType("ALIAS")
    @JvmField val ALL = KerMLTokenType("ALL")
    @JvmField val AND = KerMLTokenType("AND")
    @JvmField val AS = KerMLTokenType("AS")
    @JvmField val ASSOC = KerMLTokenType("ASSOC")
    @JvmField val BEHAVIOR = KerMLTokenType("BEHAVIOR")
    @JvmField val BINDING = KerMLTokenType("BINDING")
    @JvmField val BOOL = KerMLTokenType("BOOL")
    @JvmField val BY = KerMLTokenType("BY")
    @JvmField val CHAINS = KerMLTokenType("CHAINS")
    @JvmField val CLASS = KerMLTokenType("CLASS")
    @JvmField val CLASSIFIER = KerMLTokenType("CLASSIFIER")
    @JvmField val COMMENT = KerMLTokenType("COMMENT")
    @JvmField val COMPOSITE = KerMLTokenType("COMPOSITE")
    @JvmField val CONJUGATE = KerMLTokenType("CONJUGATE")
    @JvmField val CONJUGATES = KerMLTokenType("CONJUGATES")
    @JvmField val CONJUGATION = KerMLTokenType("CONJUGATION")
    @JvmField val CONNECTOR = KerMLTokenType("CONNECTOR")
    @JvmField val CONST = KerMLTokenType("CONST")
    @JvmField val CROSSES = KerMLTokenType("CROSSES")
    @JvmField val DATATYPE = KerMLTokenType("DATATYPE")
    @JvmField val DEFAULT = KerMLTokenType("DEFAULT")
    @JvmField val DEPENDENCY = KerMLTokenType("DEPENDENCY")
    @JvmField val DERIVED = KerMLTokenType("DERIVED")
    @JvmField val DIFFERENCES = KerMLTokenType("DIFFERENCES")
    @JvmField val DISJOINING = KerMLTokenType("DISJOINING")
    @JvmField val DISJOINT = KerMLTokenType("DISJOINT")
    @JvmField val DOC = KerMLTokenType("DOC")
    @JvmField val ELSE = KerMLTokenType("ELSE")
    @JvmField val END = KerMLTokenType("END")
    @JvmField val EXPOSE = KerMLTokenType("EXPOSE")
    @JvmField val EXPR = KerMLTokenType("EXPR")
    @JvmField val FALSE = KerMLTokenType("FALSE")
    @JvmField val FEATURE = KerMLTokenType("FEATURE")
    @JvmField val FEATURED = KerMLTokenType("FEATURED")
    @JvmField val FEATURING = KerMLTokenType("FEATURING")
    @JvmField val FILTER = KerMLTokenType("FILTER")
    @JvmField val FIRST = KerMLTokenType("FIRST")
    @JvmField val FLOW = KerMLTokenType("FLOW")
    @JvmField val FOR = KerMLTokenType("FOR")
    @JvmField val FROM = KerMLTokenType("FROM")
    @JvmField val FUNCTION = KerMLTokenType("FUNCTION")
    @JvmField val HASTYPE = KerMLTokenType("HASTYPE")
    @JvmField val IF = KerMLTokenType("IF")
    @JvmField val IMPLIES = KerMLTokenType("IMPLIES")
    @JvmField val IMPORT = KerMLTokenType("IMPORT")
    @JvmField val IN = KerMLTokenType("IN")
    @JvmField val INOUT = KerMLTokenType("INOUT")
    @JvmField val INTERACTION = KerMLTokenType("INTERACTION")
    @JvmField val INTERSECTS = KerMLTokenType("INTERSECTS")
    @JvmField val INV = KerMLTokenType("INV")
    @JvmField val INVERSE = KerMLTokenType("INVERSE")
    @JvmField val INVERTING = KerMLTokenType("INVERTING")
    @JvmField val ISTYPE = KerMLTokenType("ISTYPE")
    @JvmField val LANGUAGE = KerMLTokenType("LANGUAGE")
    @JvmField val LIBRARY = KerMLTokenType("LIBRARY")
    @JvmField val LOCALE = KerMLTokenType("LOCALE")
    @JvmField val MEMBER = KerMLTokenType("MEMBER")
    @JvmField val META = KerMLTokenType("META")
    @JvmField val METACLASS = KerMLTokenType("METACLASS")
    @JvmField val METADATA = KerMLTokenType("METADATA")
    @JvmField val MULTIPLICITY = KerMLTokenType("MULTIPLICITY")
    @JvmField val NAMESPACE = KerMLTokenType("NAMESPACE")
    @JvmField val NEW = KerMLTokenType("NEW")
    @JvmField val NONUNIQUE = KerMLTokenType("NONUNIQUE")
    @JvmField val NOT = KerMLTokenType("NOT")
    @JvmField val NULL = KerMLTokenType("NULL")
    @JvmField val OF = KerMLTokenType("OF")
    @JvmField val OR = KerMLTokenType("OR")
    @JvmField val ORDERED = KerMLTokenType("ORDERED")
    @JvmField val OUT = KerMLTokenType("OUT")
    @JvmField val PACKAGE = KerMLTokenType("PACKAGE")
    @JvmField val PORTION = KerMLTokenType("PORTION")
    @JvmField val PREDICATE = KerMLTokenType("PREDICATE")
    @JvmField val PRIVATE = KerMLTokenType("PRIVATE")
    @JvmField val PROTECTED = KerMLTokenType("PROTECTED")
    @JvmField val PUBLIC = KerMLTokenType("PUBLIC")
    @JvmField val REDEFINES = KerMLTokenType("REDEFINES")
    @JvmField val REDEFINITION = KerMLTokenType("REDEFINITION")
    @JvmField val REFERENCES = KerMLTokenType("REFERENCES")
    @JvmField val RENDER = KerMLTokenType("RENDER")
    @JvmField val RENDERING = KerMLTokenType("RENDERING")
    @JvmField val REP = KerMLTokenType("REP")
    @JvmField val RETURN = KerMLTokenType("RETURN")
    @JvmField val SPECIALIZATION = KerMLTokenType("SPECIALIZATION")
    @JvmField val SPECIALIZES = KerMLTokenType("SPECIALIZES")
    @JvmField val STANDARD = KerMLTokenType("STANDARD")
    @JvmField val STEP = KerMLTokenType("STEP")
    @JvmField val STRUCT = KerMLTokenType("STRUCT")
    @JvmField val SUBCLASSIFIER = KerMLTokenType("SUBCLASSIFIER")
    @JvmField val SUBSET = KerMLTokenType("SUBSET")
    @JvmField val SUBSETS = KerMLTokenType("SUBSETS")
    @JvmField val SUBTYPE = KerMLTokenType("SUBTYPE")
    @JvmField val SUCCESSION = KerMLTokenType("SUCCESSION")
    @JvmField val THEN = KerMLTokenType("THEN")
    @JvmField val TO = KerMLTokenType("TO")
    @JvmField val TRUE = KerMLTokenType("TRUE")
    @JvmField val TYPE = KerMLTokenType("TYPE")
    @JvmField val TYPED = KerMLTokenType("TYPED")
    @JvmField val TYPING = KerMLTokenType("TYPING")
    @JvmField val UNIONS = KerMLTokenType("UNIONS")
    @JvmField val VAR = KerMLTokenType("VAR")
    @JvmField val VIEW = KerMLTokenType("VIEW")
    @JvmField val VIEWPOINT = KerMLTokenType("VIEWPOINT")
    @JvmField val XOR = KerMLTokenType("XOR")

    // --- Multi-character operators (108-124) ---
    @JvmField val DOUBLE_COLON_GT = KerMLTokenType("DOUBLE_COLON_GT")
    @JvmField val COLON_GT_GT = KerMLTokenType("COLON_GT_GT")
    @JvmField val TRIPLE_EQUALS = KerMLTokenType("TRIPLE_EQUALS")
    @JvmField val EXCLAIM_EQUALS_EQUALS = KerMLTokenType("EXCLAIM_EQUALS_EQUALS")
    @JvmField val DOUBLE_STAR = KerMLTokenType("DOUBLE_STAR")
    @JvmField val DOUBLE_EQUALS = KerMLTokenType("DOUBLE_EQUALS")
    @JvmField val EXCLAIM_EQUALS = KerMLTokenType("EXCLAIM_EQUALS")
    @JvmField val LESS_EQUALS = KerMLTokenType("LESS_EQUALS")
    @JvmField val GREATER_EQUALS = KerMLTokenType("GREATER_EQUALS")
    @JvmField val COLON_EQUALS = KerMLTokenType("COLON_EQUALS")
    @JvmField val DOUBLE_COLON = KerMLTokenType("DOUBLE_COLON")
    @JvmField val COLON_GT = KerMLTokenType("COLON_GT")
    @JvmField val ARROW = KerMLTokenType("ARROW")
    @JvmField val DOUBLE_DOT = KerMLTokenType("DOUBLE_DOT")
    @JvmField val EQUALS_GT = KerMLTokenType("EQUALS_GT")
    @JvmField val DOUBLE_QUESTION = KerMLTokenType("DOUBLE_QUESTION")
    @JvmField val DOT_QUESTION = KerMLTokenType("DOT_QUESTION")

    // --- Single-character delimiters (125-150) ---
    @JvmField val LPAREN = KerMLTokenType("LPAREN")
    @JvmField val RPAREN = KerMLTokenType("RPAREN")
    @JvmField val LBRACE = KerMLTokenType("LBRACE")
    @JvmField val RBRACE = KerMLTokenType("RBRACE")
    @JvmField val LBRACKET = KerMLTokenType("LBRACKET")
    @JvmField val RBRACKET = KerMLTokenType("RBRACKET")
    @JvmField val SEMICOLON = KerMLTokenType("SEMICOLON")
    @JvmField val COMMA = KerMLTokenType("COMMA")
    @JvmField val TILDE = KerMLTokenType("TILDE")
    @JvmField val AT = KerMLTokenType("AT")
    @JvmField val HASH = KerMLTokenType("HASH")
    @JvmField val PERCENT = KerMLTokenType("PERCENT")
    @JvmField val AMPERSAND = KerMLTokenType("AMPERSAND")
    @JvmField val CARET = KerMLTokenType("CARET")
    @JvmField val PIPE = KerMLTokenType("PIPE")
    @JvmField val STAR = KerMLTokenType("STAR")
    @JvmField val PLUS = KerMLTokenType("PLUS")
    @JvmField val MINUS = KerMLTokenType("MINUS")
    @JvmField val SLASH = KerMLTokenType("SLASH")
    @JvmField val DOLLAR = KerMLTokenType("DOLLAR")
    @JvmField val DOT = KerMLTokenType("DOT")
    @JvmField val COLON = KerMLTokenType("COLON")
    @JvmField val LESS = KerMLTokenType("LESS")
    @JvmField val EQUALS = KerMLTokenType("EQUALS")
    @JvmField val GREATER = KerMLTokenType("GREATER")
    @JvmField val QUESTION = KerMLTokenType("QUESTION")

    // --- Comments (151-154) ---
    @JvmField val LINE_TERMINATOR = KerMLTokenType("LINE_TERMINATOR")
    @JvmField val SINGLE_LINE_NOTE = KerMLTokenType("SINGLE_LINE_NOTE")
    @JvmField val MULTILINE_NOTE = KerMLTokenType("MULTILINE_NOTE")
    @JvmField val REGULAR_COMMENT = KerMLTokenType("REGULAR_COMMENT")

    // --- Literals (155-158) ---
    @JvmField val DECIMAL_VALUE = KerMLTokenType("DECIMAL_VALUE")
    @JvmField val EXPONENTIAL_VALUE = KerMLTokenType("EXPONENTIAL_VALUE")
    @JvmField val STRING_VALUE = KerMLTokenType("STRING_VALUE")
    @JvmField val NAME = KerMLTokenType("NAME")

    // --- Whitespace (159) ---
    @JvmField val WS = KerMLTokenType("WS")

    // --- Bad character fallback ---
    @JvmField val BAD_CHARACTER = TokenType.BAD_CHARACTER

    /**
     * Map ANTLR token type (int) → IntelliJ IElementType.
     * Index = ANTLR token type; value = IElementType.
     * Index 0 = EOF (unused). Indices 1..159 match the .tokens file.
     */
    private val tokenMap: Array<IElementType> = arrayOf(
        TokenType.BAD_CHARACTER,  // 0 = EOF
        ABOUT,          // 1
        ABSTRACT,       // 2
        ALIAS,          // 3
        ALL,            // 4
        AND,            // 5
        AS,             // 6
        ASSOC,          // 7
        BEHAVIOR,       // 8
        BINDING,        // 9
        BOOL,           // 10
        BY,             // 11
        CHAINS,         // 12
        CLASS,          // 13
        CLASSIFIER,     // 14
        COMMENT,        // 15
        COMPOSITE,      // 16
        CONJUGATE,      // 17
        CONJUGATES,     // 18
        CONJUGATION,    // 19
        CONNECTOR,      // 20
        CONST,          // 21
        CROSSES,        // 22
        DATATYPE,       // 23
        DEFAULT,        // 24
        DEPENDENCY,     // 25
        DERIVED,        // 26
        DIFFERENCES,    // 27
        DISJOINING,     // 28
        DISJOINT,       // 29
        DOC,            // 30
        ELSE,           // 31
        END,            // 32
        EXPOSE,         // 33
        EXPR,           // 34
        FALSE,          // 35
        FEATURE,        // 36
        FEATURED,       // 37
        FEATURING,      // 38
        FILTER,         // 39
        FIRST,          // 40
        FLOW,           // 41
        FOR,            // 42
        FROM,           // 43
        FUNCTION,       // 44
        HASTYPE,        // 45
        IF,             // 46
        IMPLIES,        // 47
        IMPORT,         // 48
        IN,             // 49
        INOUT,          // 50
        INTERACTION,    // 51
        INTERSECTS,     // 52
        INV,            // 53
        INVERSE,        // 54
        INVERTING,      // 55
        ISTYPE,         // 56
        LANGUAGE,       // 57
        LIBRARY,        // 58
        LOCALE,         // 59
        MEMBER,         // 60
        META,           // 61
        METACLASS,      // 62
        METADATA,       // 63
        MULTIPLICITY,   // 64
        NAMESPACE,      // 65
        NEW,            // 66
        NONUNIQUE,      // 67
        NOT,            // 68
        NULL,           // 69
        OF,             // 70
        OR,             // 71
        ORDERED,        // 72
        OUT,            // 73
        PACKAGE,        // 74
        PORTION,        // 75
        PREDICATE,      // 76
        PRIVATE,        // 77
        PROTECTED,      // 78
        PUBLIC,         // 79
        REDEFINES,      // 80
        REDEFINITION,   // 81
        REFERENCES,     // 82
        RENDER,         // 83
        RENDERING,      // 84
        REP,            // 85
        RETURN,         // 86
        SPECIALIZATION, // 87
        SPECIALIZES,    // 88
        STANDARD,       // 89
        STEP,           // 90
        STRUCT,         // 91
        SUBCLASSIFIER,  // 92
        SUBSET,         // 93
        SUBSETS,        // 94
        SUBTYPE,        // 95
        SUCCESSION,     // 96
        THEN,           // 97
        TO,             // 98
        TRUE,           // 99
        TYPE,           // 100
        TYPED,          // 101
        TYPING,         // 102
        UNIONS,         // 103
        VAR,            // 104
        VIEW,           // 105
        VIEWPOINT,      // 106
        XOR,            // 107
        DOUBLE_COLON_GT,        // 108
        COLON_GT_GT,            // 109
        TRIPLE_EQUALS,          // 110
        EXCLAIM_EQUALS_EQUALS,  // 111
        DOUBLE_STAR,            // 112
        DOUBLE_EQUALS,          // 113
        EXCLAIM_EQUALS,         // 114
        LESS_EQUALS,            // 115
        GREATER_EQUALS,         // 116
        COLON_EQUALS,           // 117
        DOUBLE_COLON,           // 118
        COLON_GT,               // 119
        ARROW,                  // 120
        DOUBLE_DOT,             // 121
        EQUALS_GT,              // 122
        DOUBLE_QUESTION,        // 123
        DOT_QUESTION,           // 124
        LPAREN,         // 125
        RPAREN,         // 126
        LBRACE,         // 127
        RBRACE,         // 128
        LBRACKET,       // 129
        RBRACKET,       // 130
        SEMICOLON,      // 131
        COMMA,          // 132
        TILDE,          // 133
        AT,             // 134
        HASH,           // 135
        PERCENT,        // 136
        AMPERSAND,      // 137
        CARET,          // 138
        PIPE,           // 139
        STAR,           // 140
        PLUS,           // 141
        MINUS,          // 142
        SLASH,          // 143
        DOLLAR,         // 144
        DOT,            // 145
        COLON,          // 146
        LESS,           // 147
        EQUALS,         // 148
        GREATER,        // 149
        QUESTION,       // 150
        LINE_TERMINATOR,    // 151
        SINGLE_LINE_NOTE,   // 152
        MULTILINE_NOTE,     // 153
        REGULAR_COMMENT,    // 154
        DECIMAL_VALUE,      // 155
        EXPONENTIAL_VALUE,  // 156
        STRING_VALUE,       // 157
        NAME,               // 158
        WS                  // 159
    )

    fun fromAntlrType(antlrType: Int): IElementType {
        if (antlrType < 0 || antlrType >= tokenMap.size) return BAD_CHARACTER
        return tokenMap[antlrType]
    }

    // --- Declaration keywords: introduce named declarations ---
    @JvmField val DECLARATION_KEYWORDS = TokenSet.create(
        PACKAGE, NAMESPACE, CLASS, CLASSIFIER, DATATYPE, STRUCT, ASSOC,
        BEHAVIOR, FUNCTION, PREDICATE, INTERACTION, METACLASS,
        FEATURE, STEP, EXPR, BOOL, CONNECTOR, BINDING, SUCCESSION,
        FLOW, VIEW, VIEWPOINT, RENDERING, ALIAS, IMPORT, COMMENT, DOC,
        SPECIALIZATION, CONJUGATION, DISJOINING, REDEFINITION, SUBSET,
        SUBCLASSIFIER, SUBTYPE, TYPING, FEATURING, INVERTING,
        MULTIPLICITY, DEPENDENCY, METADATA, REP
    )

    // --- Modifier keywords ---
    @JvmField val MODIFIER_KEYWORDS = TokenSet.create(
        ABSTRACT, COMPOSITE, CONST, DERIVED, END, ORDERED, NONUNIQUE,
        PORTION, STANDARD, VAR, DISJOINT, LIBRARY
    )

    // --- Relationship keywords ---
    @JvmField val RELATIONSHIP_KEYWORDS = TokenSet.create(
        SPECIALIZES, CONJUGATES, SUBSETS, REDEFINES, REFERENCES,
        TYPED, FEATURED, CHAINS, CROSSES, UNIONS, INTERSECTS, DIFFERENCES
    )

    // --- Other keywords ---
    @JvmField val OTHER_KEYWORDS = TokenSet.create(
        ABOUT, ALL, AND, AS, BY, DEFAULT, ELSE, EXPOSE, FALSE, FILTER,
        FIRST, FOR, FROM, HASTYPE, IF, IMPLIES, IN, INOUT, INV, INVERSE,
        ISTYPE, LANGUAGE, LOCALE, MEMBER, META, NEW, NOT, NULL, OF, OR,
        OUT, PRIVATE, PROTECTED, PUBLIC, RENDER, RETURN, THEN, TO, TRUE, XOR
    )

    @JvmField val ALL_KEYWORDS = TokenSet.orSet(
        DECLARATION_KEYWORDS, MODIFIER_KEYWORDS, RELATIONSHIP_KEYWORDS, OTHER_KEYWORDS
    )

    @JvmField val OPERATORS = TokenSet.create(
        DOUBLE_COLON_GT, COLON_GT_GT, TRIPLE_EQUALS, EXCLAIM_EQUALS_EQUALS,
        DOUBLE_STAR, DOUBLE_EQUALS, EXCLAIM_EQUALS, LESS_EQUALS, GREATER_EQUALS,
        COLON_EQUALS, DOUBLE_COLON, COLON_GT, ARROW, DOUBLE_DOT, EQUALS_GT,
        DOUBLE_QUESTION, DOT_QUESTION,
        TILDE, AT, HASH, PERCENT, AMPERSAND, CARET, PIPE, STAR, PLUS, MINUS,
        SLASH, DOLLAR, DOT, COLON, LESS, EQUALS, GREATER, QUESTION
    )

    @JvmField val BRACES = TokenSet.create(LBRACE, RBRACE)
    @JvmField val BRACKETS = TokenSet.create(LBRACKET, RBRACKET)
    @JvmField val PARENS = TokenSet.create(LPAREN, RPAREN)

    @JvmField val COMMENTS = TokenSet.create(SINGLE_LINE_NOTE, MULTILINE_NOTE, REGULAR_COMMENT)

    @JvmField val STRING_LITERALS = TokenSet.create(STRING_VALUE)

    @JvmField val NUMBER_LITERALS = TokenSet.create(DECIMAL_VALUE, EXPONENTIAL_VALUE)

    @JvmField val WHITESPACE_TOKENS = TokenSet.create(TokenType.WHITE_SPACE)

    @JvmField val IDENTIFIERS = TokenSet.create(NAME)
}
