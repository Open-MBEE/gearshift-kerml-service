/******************************
* Parser for OCL 2.4 with extensions for textual notations
* for UML classes and usecases.
*
* Arrow operators ->op are used consistently for any OCL
* operator, not just collection operators.
*
* Original work Copyright (c) 2003--2023 Kevin Lano
* Licensed under Eclipse Public License 2.0
* http://www.eclipse.org/legal/epl-2.0
* SPDX-License-Identifier: EPL-2.0
*
* Modifications Copyright (c) 2026 Charles Galey / OpenMBEE
* Licensed under Apache License 2.0
* http://www.apache.org/licenses/LICENSE-2.0
* SPDX-License-Identifier: Apache-2.0
*
* Modifications:
* - Refactored arrow operations into modular suffix rules
* - Added explicit lexer tokens for arrow operations (ARROW_*)
* - Split basicExpression into individual literal/access rules
* - Added singleExpression rule for parsing standalone expressions
* *****************************/

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

grammar OCL;

// ===== Top-level Rules =====

multipleContextSpecifications
    : (singleInvariant | singleDerivedAttribute)+ EOF
    ;

contextSpecification
    : (singleInvariant | singleDerivedAttribute) EOF
    ;

singleInvariant
    : 'context' ID 'inv' ID? ':' expression
    ;

singleDerivedAttribute
    : 'context' qualified_name ':' type ('init:' expression)? 'derive:' expression
    ;

// Single expression for operation bodies
singleExpression
    : expression EOF
    ;

enumeration
    : 'enumeration' identifier '{' enumerationLiteral+ '}'
    ;

enumerationLiteral
    : 'literal' identifier
    ;

type
    : 'Sequence' '(' type ')'
    | 'Set' '(' type ')'
    | 'Bag' '(' type ')'
    | 'OrderedSet' '(' type ')'
    | 'Ref' '(' type ')'
    | 'Map' '(' type ',' type ')'
    | 'Function' '(' type ',' type ')'
    | identifier
    ;

expressionList
    : expression (',' expression)*
    ;

// ===== Expression Hierarchy =====

expression
    : conditionalExpression
    | lambdaExpression
    | letExpression
    | logicalExpression
    ;

conditionalExpression
    : 'if' expression 'then' expression 'else' expression 'endif'
    ;

lambdaExpression
    : 'lambda' identifier ':' type 'in' expression
    ;

letExpression
    : 'let' ID (':' type)? '=' expression 'in' expression
    ;

logicalExpression
    : logicalExpression 'and' logicalExpression
    | logicalExpression '&' logicalExpression
    | logicalExpression 'or' logicalExpression
    | logicalExpression 'xor' logicalExpression
    | logicalExpression '=>' logicalExpression
    | logicalExpression 'implies' logicalExpression
    | 'not' logicalExpression
    | equalityExpression
    ;

equalityExpression
    : additiveExpression comparisonOp additiveExpression
    | additiveExpression
    ;

comparisonOp
    : '=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' | ':' | '/:' | '<:'
    ;

additiveExpression
    : additiveExpression ('+' | '-') multiplicativeExpression
    | additiveExpression ('..' | '|->') multiplicativeExpression
    | multiplicativeExpression
    ;

multiplicativeExpression
    : multiplicativeExpression ('*' | '/' | 'mod' | 'div') unaryExpression
    | unaryExpression
    ;

unaryExpression
    : ('-' | '+' | '?' | '!') unaryExpression
    | postfixExpression
    ;

// ===== Postfix Expression with Arrow Operations =====
// Uses suffix pattern to avoid mutual left-recursion while keeping modular rules

postfixExpression
    : primaryExpression (dotSuffix | arrowSuffix)*
    ;

dotSuffix
    : '.' ID ('(' expressionList? ')')?
    | '[' expression ']'
    | typeDotSuffix
    ;

// Dot-style type operations (standard OCL: element.oclIsKindOf(Type))
// Accept expression instead of just typeName to support:
// - Static types: element.oclIsKindOf(SomeType) -> ID as expression
// - String literals: element.oclIsKindOf('SomeType') -> STRING as expression
// - Dynamic types: element.oclIsKindOf(other.oclType()) -> expression
typeDotSuffix
    : '.oclIsKindOf' '(' expression ')'
    | '.oclIsTypeOf' '(' expression ')'
    | '.oclAsType' '(' expression ')'
    ;

// Arrow suffixes - each category gets its own rule for clean visitor methods
arrowSuffix
    : nullaryArrowSuffix
    | unaryArrowSuffix
    | binaryArrowSuffix
    | typeArrowSuffix
    | iteratorSuffix
    | iterateSuffix
    ;

// ----- Nullary Arrow Operations (no arguments) -----

nullaryArrowSuffix
    : ARROW_SIZE
    | ARROW_COPY
    | ARROW_IS_EMPTY
    | ARROW_NOT_EMPTY
    | ARROW_AS_SET
    | ARROW_AS_BAG
    | ARROW_AS_ORDERED_SET
    | ARROW_AS_SEQUENCE
    | ARROW_SORT
    | ARROW_ANY_NULLARY
    | ARROW_LOG
    | ARROW_EXP
    | ARROW_SIN
    | ARROW_COS
    | ARROW_TAN
    | ARROW_ASIN
    | ARROW_ACOS
    | ARROW_ATAN
    | ARROW_LOG10
    | ARROW_FIRST
    | ARROW_LAST
    | ARROW_FRONT
    | ARROW_TAIL
    | ARROW_REVERSE
    | ARROW_TANH
    | ARROW_SINH
    | ARROW_COSH
    | ARROW_FLOOR
    | ARROW_CEIL
    | ARROW_ROUND
    | ARROW_ABS
    | ARROW_OCL_TYPE
    | ARROW_ALL_INSTANCES
    | ARROW_OCL_IS_UNDEFINED
    | ARROW_OCL_IS_INVALID
    | ARROW_OCL_IS_NEW
    | ARROW_SUM
    | ARROW_PRD
    | ARROW_MAX_NULLARY
    | ARROW_MIN_NULLARY
    | ARROW_SQRT
    | ARROW_CBRT
    | ARROW_SQR
    | ARROW_CHARACTERS
    | ARROW_TO_INTEGER
    | ARROW_TO_REAL
    | ARROW_TO_BOOLEAN
    | ARROW_DISPLAY
    | ARROW_TO_UPPER_CASE
    | ARROW_TO_LOWER_CASE
    | ARROW_UNION_ALL
    | ARROW_INTERSECT_ALL
    | ARROW_CONCATENATE_ALL
    | ARROW_FLATTEN
    ;

// ----- Unary Arrow Operations (one expression argument) -----

unaryArrowSuffix
    : ARROW_POW '(' expression ')'
    | ARROW_GCD '(' expression ')'
    | ARROW_AT '(' expression ')'
    | ARROW_UNION '(' expression ')'
    | ARROW_INTERSECTION '(' expression ')'
    | ARROW_INCLUDES '(' expression ')'
    | ARROW_EXCLUDES '(' expression ')'
    | ARROW_INCLUDING '(' expression ')'
    | ARROW_EXCLUDING '(' expression ')'
    | ARROW_INCLUDES_ALL '(' expression ')'
    | ARROW_SYMMETRIC_DIFFERENCE '(' expression ')'
    | ARROW_EXCLUDES_ALL '(' expression ')'
    | ARROW_PREPEND '(' expression ')'
    | ARROW_APPEND '(' expression ')'
    | ARROW_COUNT '(' expression ')'
    | ARROW_APPLY '(' expression ')'
    | ARROW_HAS_MATCH '(' expression ')'
    | ARROW_IS_MATCH '(' expression ')'
    | ARROW_FIRST_MATCH '(' expression ')'
    | ARROW_INDEX_OF '(' expression ')'
    | ARROW_LAST_INDEX_OF '(' expression ')'
    | ARROW_SPLIT '(' expression ')'
    | ARROW_HAS_PREFIX '(' expression ')'
    | ARROW_HAS_SUFFIX '(' expression ')'
    | ARROW_EQUALS_IGNORE_CASE '(' expression ')'
    | ARROW_MAX '(' expression ')'
    | ARROW_MIN '(' expression ')'
    ;

// ----- Binary Arrow Operations (two expression arguments) -----

binaryArrowSuffix
    : ARROW_SUBRANGE '(' expression ',' expression ')'
    | ARROW_REPLACE '(' expression ',' expression ')'
    | ARROW_REPLACE_ALL '(' expression ',' expression ')'
    | ARROW_REPLACE_ALL_MATCHES '(' expression ',' expression ')'
    | ARROW_REPLACE_FIRST_MATCH '(' expression ',' expression ')'
    | ARROW_INSERT_AT '(' expression ',' expression ')'
    | ARROW_INSERT_INTO '(' expression ',' expression ')'
    | ARROW_SET_AT '(' expression ',' expression ')'
    ;

// ----- Type Operations -----

typeArrowSuffix
    : ARROW_OCL_AS_TYPE '(' typeName ')'
    | ARROW_OCL_IS_TYPE_OF '(' typeName ')'
    | ARROW_OCL_IS_KIND_OF '(' typeName ')'
    | ARROW_OCL_AS_SET_OP '(' typeName ')'
    | ARROW_SELECT_BY_KIND '(' typeName ')'
    | ARROW_SELECT_BY_TYPE '(' typeName ')'
    ;

typeName
    : ID
    ;

// ----- Iterator Operations (with iterator variable and body) -----
// Each iterator gets its own labeled alternative for clean visitor dispatch

iteratorSuffix
    : collectSuffix
    | selectSuffix
    | rejectSuffix
    | forAllSuffix
    | existsSuffix
    | exists1Suffix
    | oneSuffix
    | anySuffix
    | closureSuffix
    | sortedBySuffix
    | isUniqueSuffix
    ;

collectSuffix
    : ARROW_COLLECT '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

selectSuffix
    : ARROW_SELECT '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

rejectSuffix
    : ARROW_REJECT '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

forAllSuffix
    : ARROW_FOR_ALL '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

existsSuffix
    : ARROW_EXISTS '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'  // Full form: ->exists(x | body)
    | ARROW_EXISTS '(' body=expression ')'  // Shorthand form: ->exists(body)
    ;

exists1Suffix
    : ARROW_EXISTS1 '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

oneSuffix
    : ARROW_ONE '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

anySuffix
    : ARROW_ANY_OP '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

closureSuffix
    : ARROW_CLOSURE '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'  // Full form: ->closure(x | x.parent)
    | ARROW_CLOSURE '(' body=expression ')'  // Shorthand form: ->closure(parent)
    ;

sortedBySuffix
    : ARROW_SORTED_BY '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    | ARROW_SORTED_BY '(' propertyName=ID ')'
    ;

isUniqueSuffix
    : ARROW_IS_UNIQUE '(' iteratorVar=ID (':' iteratorType=type)? '|' body=expression ')'
    ;

// ----- Iterate Operation (with accumulator) -----

iterateSuffix
    : ARROW_ITERATE '(' iteratorVar=ID ';' accumulatorVar=ID '=' accumulatorInit=expression '|' body=expression ')'
    ;

// ===== Primary Expressions =====

primaryExpression
    : nullLiteral
    | booleanLiteral
    | intLiteral
    | floatLiteral
    | stringLiteral
    | collectionLiteral
    | enumerationLiteralExp
    | preValueExp
    | standaloneTypeOp
    | standaloneOperationCall
    | variableExp
    | parenthesizedExpression
    ;

// Standalone type operations with implicit self (used in iterator bodies)
standaloneTypeOp
    : 'oclIsKindOf' '(' typeName ')'
    | 'oclIsTypeOf' '(' typeName ')'
    | 'oclAsType' '(' typeName ')'
    ;

// Standalone operation call with implicit self
standaloneOperationCall
    : ID '(' expressionList? ')'
    ;

nullLiteral
    : 'null'
    ;

booleanLiteral
    : 'true'
    | 'false'
    ;

intLiteral
    : INT
    ;

floatLiteral
    : FLOAT_LITERAL
    ;

stringLiteral
    : STRING1_LITERAL
    | STRING2_LITERAL
    ;

collectionLiteral
    : 'Set' '{' expressionList? '}'
    | 'Bag' '{' expressionList? '}'
    | 'Sequence' '{' expressionList? '}'
    | 'OrderedSet' '{' expressionList? '}'
    | 'Map' '{' expressionList? '}'
    ;

enumerationLiteralExp
    : ENUMERATION_LITERAL
    ;

preValueExp
    : ID '@pre'
    ;

variableExp
    : ID
    ;

parenthesizedExpression
    : '(' expression ')'
    ;

identifier
    : ID
    ;

qualified_name
    : ID ('::' ID)*
    ;

// ===== Arrow Operation Tokens =====
// Nullary (no arguments - include parens in token)

ARROW_SIZE: '->size()';
ARROW_COPY: '->copy()';
ARROW_IS_EMPTY: '->isEmpty()';
ARROW_NOT_EMPTY: '->notEmpty()';
ARROW_AS_SET: '->asSet()';
ARROW_AS_BAG: '->asBag()';
ARROW_AS_ORDERED_SET: '->asOrderedSet()';
ARROW_AS_SEQUENCE: '->asSequence()';
ARROW_SORT: '->sort()';
ARROW_ANY_NULLARY: '->any()';
ARROW_LOG: '->log()';
ARROW_EXP: '->exp()';
ARROW_SIN: '->sin()';
ARROW_COS: '->cos()';
ARROW_TAN: '->tan()';
ARROW_ASIN: '->asin()';
ARROW_ACOS: '->acos()';
ARROW_ATAN: '->atan()';
ARROW_LOG10: '->log10()';
ARROW_FIRST: '->first()';
ARROW_LAST: '->last()';
ARROW_FRONT: '->front()';
ARROW_TAIL: '->tail()';
ARROW_REVERSE: '->reverse()';
ARROW_TANH: '->tanh()';
ARROW_SINH: '->sinh()';
ARROW_COSH: '->cosh()';
ARROW_FLOOR: '->floor()';
ARROW_CEIL: '->ceil()';
ARROW_ROUND: '->round()';
ARROW_ABS: '->abs()';
ARROW_OCL_TYPE: '->oclType()';
ARROW_ALL_INSTANCES: '->allInstances()';
ARROW_OCL_IS_UNDEFINED: '->oclIsUndefined()';
ARROW_OCL_IS_INVALID: '->oclIsInvalid()';
ARROW_OCL_IS_NEW: '->oclIsNew()';
ARROW_SUM: '->sum()';
ARROW_PRD: '->prd()';
ARROW_MAX_NULLARY: '->max()';
ARROW_MIN_NULLARY: '->min()';
ARROW_SQRT: '->sqrt()';
ARROW_CBRT: '->cbrt()';
ARROW_SQR: '->sqr()';
ARROW_CHARACTERS: '->characters()';
ARROW_TO_INTEGER: '->toInteger()';
ARROW_TO_REAL: '->toReal()';
ARROW_TO_BOOLEAN: '->toBoolean()';
ARROW_DISPLAY: '->display()';
ARROW_TO_UPPER_CASE: '->toUpperCase()';
ARROW_TO_LOWER_CASE: '->toLowerCase()';
ARROW_UNION_ALL: '->unionAll()';
ARROW_INTERSECT_ALL: '->intersectAll()';
ARROW_CONCATENATE_ALL: '->concatenateAll()';
ARROW_FLATTEN: '->flatten()';

// Unary arrow operations (with one argument - no parens in token)
ARROW_POW: '->pow';
ARROW_GCD: '->gcd';
ARROW_AT: '->at';
ARROW_UNION: '->union';
ARROW_INTERSECTION: '->intersection';
ARROW_INCLUDES: '->includes';
ARROW_EXCLUDES: '->excludes';
ARROW_INCLUDING: '->including';
ARROW_EXCLUDING: '->excluding';
ARROW_INCLUDES_ALL: '->includesAll';
ARROW_SYMMETRIC_DIFFERENCE: '->symmetricDifference';
ARROW_EXCLUDES_ALL: '->excludesAll';
ARROW_PREPEND: '->prepend';
ARROW_APPEND: '->append';
ARROW_COUNT: '->count';
ARROW_APPLY: '->apply';
ARROW_HAS_MATCH: '->hasMatch';
ARROW_IS_MATCH: '->isMatch';
ARROW_FIRST_MATCH: '->firstMatch';
ARROW_INDEX_OF: '->indexOf';
ARROW_LAST_INDEX_OF: '->lastIndexOf';
ARROW_SPLIT: '->split';
ARROW_HAS_PREFIX: '->hasPrefix';
ARROW_HAS_SUFFIX: '->hasSuffix';
ARROW_EQUALS_IGNORE_CASE: '->equalsIgnoreCase';
ARROW_MAX: '->max';
ARROW_MIN: '->min';

// Binary arrow operations (with two arguments)
ARROW_SUBRANGE: '->subrange';
ARROW_REPLACE: '->replace';
ARROW_REPLACE_ALL: '->replaceAll';
ARROW_REPLACE_ALL_MATCHES: '->replaceAllMatches';
ARROW_REPLACE_FIRST_MATCH: '->replaceFirstMatch';
ARROW_INSERT_AT: '->insertAt';
ARROW_INSERT_INTO: '->insertInto';
ARROW_SET_AT: '->setAt';

// Type operations
ARROW_OCL_AS_TYPE: '->oclAsType';
ARROW_OCL_IS_TYPE_OF: '->oclIsTypeOf';
ARROW_OCL_IS_KIND_OF: '->oclIsKindOf';
ARROW_OCL_AS_SET_OP: '->oclAsSet';
ARROW_SELECT_BY_KIND: '->selectByKind';
ARROW_SELECT_BY_TYPE: '->selectByType';

// Iterator operations
ARROW_COLLECT: '->collect';
ARROW_SELECT: '->select';
ARROW_REJECT: '->reject';
ARROW_FOR_ALL: '->forAll';
ARROW_EXISTS: '->exists';
ARROW_EXISTS1: '->exists1';
ARROW_ONE: '->one';
ARROW_ANY_OP: '->any';
ARROW_CLOSURE: '->closure';
ARROW_SORTED_BY: '->sortedBy';
ARROW_IS_UNIQUE: '->isUnique';
ARROW_ITERATE: '->iterate';

// ===== Other Lexer Rules =====

FLOAT_LITERAL
    : Digits '.' Digits
    ;

STRING1_LITERAL
    : '"' (~["\\\r\n] | EscapeSequence)* '"'
    ;

STRING2_LITERAL
    : '\'' (~['\\\r\n] | EscapeSequence)* '\''
    ;

ENUMERATION_LITERAL
    : ID '::' ID
    ;

MULTILINE_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

LINE_COMMENT
    : '--' ~[\r\n]* -> channel(HIDDEN)
    ;

fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

fragment Digits
    : [0-9]+
    ;

INT
    : [0-9]+
    ;

ID
    : [a-zA-Z_$] [a-zA-Z0-9_$]*
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
