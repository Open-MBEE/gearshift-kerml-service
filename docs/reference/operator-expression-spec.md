# KerML Operator Expression Parsing Specification

This document provides implementation guidance for parsing KerML operator expressions according to Section 8.4.4.9 of the KerML v1.0 specification.

## Table of Contents

1. [Overview](#overview)
2. [Implementation Notes](#implementation-notes)
3. [Operator Mapping (Table 5)](#operator-mapping-table-5)
4. [Operator Precedence (Table 6)](#operator-precedence-table-6)
5. [Primary Expression Operators (Table 7)](#primary-expression-operators-table-7)
6. [Implementation Requirements](#implementation-requirements)
7. [References](#references)

## Overview

OperatorExpressions provide shorthand notation for InvocationExpressions that invoke library Functions represented as operator symbols. Each operator maps to a specific Function from the Kernel Model Library (Clause 9).

An OperatorExpression contains subexpressions called its **operands** that generally correspond to the argumentExpressions of the OperatorExpression, except in the case of operators representing control Functions, where the evaluation of certain operands is determined by the Function itself.

## Implementation Notes

### 1. Operator Mapping

OperatorExpressions contain operands (subexpressions) that generally correspond to the argumentExpressions of the OperatorExpression, except for operators representing control Functions where evaluation is determined by the Function itself (see Section 8.4.4.9 for details).

### 2. Precedence and Associativity

Though not directly expressed in the syntactic productions, in any OperatorExpression containing nested OperatorExpressions, the nested OperatorExpressions shall be implicitly grouped according to the **precedence** of the operators involved (see Table 6).

- OperatorExpressions with **higher precedence** operators shall be grouped more tightly than those with lower precedence operators
- All **BinaryOperators** other than exponentiation are **left-associative** (they group to the left)
- The **exponentiation operators** (`^` and `**`) are **right-associative** (they group to the right)

### 3. Undefined Operator

The unary operator symbol `~` maps to the library Function `DataFunctions::'~'`. This abstract Function may be given a concrete definition in a domain-specific Function library, but no default definition is provided in the Kernel Functions Library.

**Important**: If no domain-specific definition is available, a tool should give a **warning** if this operator is used.

## Operator Mapping (Table 5)

This table shows the complete mapping from operator symbols to the Functions they represent from the Kernel Model Library.

| Operator | Library Function | Description | Model-Level Evaluable? |
|----------|-----------------|-------------|----------------------|
| `all` | `BaseFunctions::'all'` | Type extent | No |
| `istype` | `BaseFunctions::'istype'` | All argument values are directly or indirectly instances of a type | Yes |
| `hastype` | `BaseFunctions::'hastype'` | All argument values are directly instances of a type | Yes |
| `@` | `BaseFunctions::'@'` | Any argument value is directly or indirectly an instance of a type | Yes |
| `@@` | `BaseFunctions::'@@'` | Any argument value is directly or indirectly an instance of a metaclass | Yes |
| `as` | `BaseFunctions::as` | Select instances of type (cast) | Yes |
| `meta` | `BaseFunctions::meta` | Select instances of a metaclass (metacast) | Yes |
| `==` | `BaseFunctions::'=='` | Equality | Yes |
| `!=` | `BaseFunctions::'!='` | Inequality | Yes |
| `===` | `BaseFunctions::'==='` | Same (equality for data values, same lives for occurrences) | Yes |
| `!==` | `BaseFunctions::'!=='` | Not same | Yes |
| `xor` | `DataFunctions::'xor'` | Logical "exclusive or" | Yes |
| `not` | `DataFunctions::'not'` | Logical "not" | Yes |
| `~` | `DataFunctions::'-'` | Undefined (requires domain-specific definition) | No |
| `\|` | `DataFunctions::'\|'` | Logical "inclusive or" | Yes |
| `&` | `DataFunctions::'&'` | Logical "and" | Yes |
| `<` | `DataFunctions::'<'` | Less than | Yes |
| `>` | `DataFunctions::'>'` | Greater than | Yes |
| `<=` | `DataFunctions::'<='` | Less than or equal to | Yes |
| `>=` | `DataFunctions::'>='` | Greater than or equal to | Yes |
| `+` | `DataFunctions::'+'` | Addition | Yes |
| `-` | `DataFunctions::'-'` | Subtraction | Yes |
| `*` | `DataFunctions::'*'` | Multiplication | Yes |
| `/` | `DataFunctions::'/'` | Division | Yes |
| `%` | `DataFunctions::'%'` | Remainder | Yes |
| `^` `**` | `DataFunctions::'^'` | Exponentiation | Yes |
| `..` | `DataFunctions::'..'` | Range construction | Yes |
| `??` | `ControlFunctions::'??'` | Null coalescing | Yes |
| `if` | `ControlFunctions::'if'` | Conditional test (ternary) | Yes |
| `or` | `ControlFunctions::'or'` | Conditional "or" | Yes |
| `and` | `ControlFunctions::'and'` | Conditional "and" | Yes |
| `implies` | `ControlFunctions::'implies'` | Conditional "implication" | Yes |

## Operator Precedence (Table 6)

Operators are listed from **highest to lowest precedence**. Operators at the same level have equal precedence.

### Unary Operators

1. `all` (highest precedence)
2. `+` `-` `~` `not`

### Binary Operators

3. `^` `**` (right-associative)
4. `*` `/` `%`
5. `+` `-`
6. `..`
7. `<` `>` `<=` `>=`
8. `istype` `hastype` `@` `@@` `as` `meta`
9. `==` `!=` `===` `!==`
10. `&` `and`
11. `xor`
12. `|` `or`
13. `implies`
14. `??`

### Ternary Operators

15. `if` (lowest precedence)

### Associativity Rules

- **Left-associative** (all binary operators except exponentiation):
  ```
  a + b + c  →  (a + b) + c
  a * b / c  →  (a * b) / c
  ```

- **Right-associative** (exponentiation only):
  ```
  a ^ b ^ c  →  a ^ (b ^ c)
  a ** b ** c  →  a ** (b ** c)
  ```

## Primary Expression Operators (Table 7)

These operators are used in primary expressions for indexing, sequences, and feature operations.

| Operator | Library Function | Description | Model-level Evaluable? |
|----------|-----------------|-------------|----------------------|
| `[` | `BaseFunctions::'['` | Undefined (requires domain-specific definition) | No |
| `#` | `BaseFunctions::'#'` | Indexing | Yes |
| `,` | `BaseFunctions::','` | Sequence construction | Yes |
| `.` | `ControlFunctions::'.'` | Feature chaining | Yes |
| `collect` | `ControlFunctions::collect` | Sequence collection | Yes |
| `select` | `ControlFunctions::select` | Sequence selection | Yes |

## Implementation Requirements

### Parser Implementation

1. **Tokenization**:
   - Recognize all operators from Tables 5 and 7
   - Handle multi-character operators (`==`, `!=`, `===`, `!==`, `<=`, `>=`, `**`, `..`, `??`)
   - Distinguish between unary and binary uses of `+` and `-`

2. **Expression AST Construction**:
   - Implement precedence climbing or operator precedence parser
   - Respect the 15 precedence levels defined in Table 6
   - Handle left-associativity for most binary operators
   - Handle right-associativity for exponentiation operators (`^`, `**`)

3. **Operator Mapping**:
   - Map each operator token to its corresponding library Function
   - Generate InvocationExpression nodes for OperatorExpressions
   - Set the invoked Function reference to the appropriate library Function

4. **Validation**:
   - Issue warnings for undefined operators (`~`, `[`) without domain-specific definitions
   - Verify operator usage matches expected arity (unary, binary, ternary)

### Expression Evaluation

1. **Model-Level Evaluation**:
   - Check "Model-Level Evaluable?" column to determine if operator can be evaluated at model level
   - Operators marked "No" cannot be evaluated and should produce runtime errors if evaluation is attempted

2. **Control Function Semantics**:
   - For control Functions (`if`, `or`, `and`, `implies`, `??`), implement special evaluation semantics
   - These operators may not evaluate all operands (short-circuit evaluation)
   - Evaluation order and conditions are defined by the Function semantics

3. **Undefined Operators**:
   - For operators without domain-specific definitions (`~`, `[`), issue warnings
   - May allow domain-specific Function libraries to provide definitions

### Example Parsing

```kerml
// Expression: a + b * c
// Precedence: * (level 4) binds tighter than + (level 5)
// Result: a + (b * c)

// Expression: a * b + c * d
// Left-associative: groups left-to-right at same level
// Result: (a * b) + (c * d)

// Expression: a ^ b ^ c
// Right-associative: exponentiation groups right-to-left
// Result: a ^ (b ^ c)

// Expression: x < y and y < z
// Precedence: < (level 7) before and (level 10)
// Result: (x < y) and (y < z)

// Expression: if test then a + b else c * d
// Precedence: if (level 15) is lowest, + (5) and * (4) bind tighter
// Result: if test then (a + b) else (c * d)
```

## References

- KerML Specification v1.0, Section 8.4.4.9: Operator Expressions
- KerML Specification v1.0, Clause 9: Kernel Model Library
- [KerML.g4](../src/resource/third-party/grammars/KerML.g4) - ANTLR4 Grammar
- [Name Resolution Spec](name-resolution-spec.md) - Name Resolution Specification
