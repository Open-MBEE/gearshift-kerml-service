/*
 * Copyright 2026 Charles Galey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openmbee.gearshift.kerml.generator

import org.openmbee.gearshift.generated.interfaces.*

/**
 * Helper for generating inline expression text (without indentation).
 *
 * Used by multiplicity bounds, value parts, and other contexts where
 * expressions must be emitted inline rather than as standalone elements.
 */
object InlineExpressionHelper {

    fun generateInline(expression: Expression, context: GenerationContext): String {
        return when (expression) {
            is LiteralInteger -> expression.value.toString()
            is LiteralRational -> expression.value.toString()
            is LiteralBoolean -> if (expression.value) "true" else "false"
            is LiteralString -> {
                val escaped = expression.value
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                "\"$escaped\""
            }
            is LiteralInfinity -> "*"
            is NullExpression -> "null"
            is FeatureReferenceExpression -> context.resolveDisplayName(expression.referent)
            is OperatorExpression -> generateOperatorExpression(expression, context)
            is InvocationExpression -> generateInvocationExpression(expression, context)
            else -> context.resolveDisplayName(expression)
        }
    }

    private fun generateOperatorExpression(expr: OperatorExpression, context: GenerationContext): String {
        val args = expr.argument
        val op = expr.operator

        return when {
            // Conditional: if a ? b else c
            op == "if" && args.size == 3 -> {
                val cond = generateInline(args[0], context)
                val thenExpr = generateInline(args[1], context)
                val elseExpr = generateInline(args[2], context)
                "if $cond ? $thenExpr else $elseExpr"
            }
            // Unary prefix: not a, -a
            args.size == 1 -> {
                val operand = generateInline(args[0], context)
                val needsParens = args[0] is OperatorExpression
                if (needsParens) "$op ($operand)" else "$op $operand"
            }
            // Binary infix: a + b, a and b, etc.
            args.size == 2 -> {
                val left = generateInline(args[0], context)
                val right = generateInline(args[1], context)
                val leftParens = args[0] is OperatorExpression
                val rightParens = args[1] is OperatorExpression
                val leftStr = if (leftParens) "($left)" else left
                val rightStr = if (rightParens) "($right)" else right
                "$leftStr $op $rightStr"
            }
            else -> op
        }
    }

    private fun generateInvocationExpression(expr: InvocationExpression, context: GenerationContext): String {
        val funcName = context.resolveDisplayName(expr.instantiatedType)
        val args = expr.argument.joinToString(", ") { generateInline(it, context) }
        return "$funcName($args)"
    }
}
