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
package org.openmbee.gearshift.framework.constraints.ocl

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.openmbee.gearshift.framework.constraints.ocl.antlr.OCLBaseVisitor
import org.openmbee.gearshift.framework.constraints.ocl.antlr.OCLLexer
import org.openmbee.gearshift.framework.constraints.ocl.antlr.OCLParser

/**
 * Adapter that converts ANTLR parse trees to OclExpression AST nodes.
 *
 * This adapter bridges the ANTLR-generated parser with our custom AST,
 * providing a clean interface for OCL evaluation.
 */
object OclParser {

    /**
     * Parse an OCL expression string and return the AST.
     */
    fun parse(input: String): OclExpression {
        val lexer = OCLLexer(CharStreams.fromString(input))
        val tokens = CommonTokenStream(lexer)
        val parser = OCLParser(tokens)

        // Configure error handling
        parser.removeErrorListeners()
        parser.addErrorListener(OclErrorListener())

        val tree = parser.singleExpression()
        return AstBuilder().visit(tree.expression())
    }

    /**
     * Visitor that builds AST nodes from ANTLR parse tree contexts.
     */
    private class AstBuilder : OCLBaseVisitor<OclExpression>() {

        // ===== Expression Hierarchy =====

        override fun visitExpression(ctx: OCLParser.ExpressionContext): OclExpression {
            return when {
                ctx.conditionalExpression() != null -> visit(ctx.conditionalExpression())
                ctx.lambdaExpression() != null -> visitLambda(ctx.lambdaExpression())
                ctx.letExpression() != null -> visit(ctx.letExpression())
                ctx.logicalExpression() != null -> visit(ctx.logicalExpression())
                else -> throw OclParseException("Unknown expression type")
            }
        }

        override fun visitConditionalExpression(ctx: OCLParser.ConditionalExpressionContext): OclExpression {
            val condition = visit(ctx.expression(0))
            val thenExpr = visit(ctx.expression(1))
            val elseExpr = visit(ctx.expression(2))
            return IfExp(condition, thenExpr, elseExpr)
        }

        private fun visitLambda(ctx: OCLParser.LambdaExpressionContext): OclExpression {
            // Lambda expressions are translated to a LetExp with a special variable
            val varName = ctx.identifier().text
            val body = visit(ctx.expression())
            // For now, treat lambda as a let expression with null init
            // The actual lambda semantics would need a LambdaExp AST node
            return LetExp(varName, NullLiteralExp, body)
        }

        override fun visitLetExpression(ctx: OCLParser.LetExpressionContext): OclExpression {
            val varName = ctx.ID().text
            val varValue = visit(ctx.expression(0))
            val body = visit(ctx.expression(1))
            return LetExp(varName, varValue, body)
        }

        // ===== Logical Expressions =====

        override fun visitLogicalExpression(ctx: OCLParser.LogicalExpressionContext): OclExpression {
            // Handle 'not' prefix
            if (ctx.getChild(0)?.text == "not") {
                return PrefixExp("not", visit(ctx.logicalExpression(0)))
            }

            // Handle binary logical operators
            if (ctx.logicalExpression().size == 2) {
                val left = visit(ctx.logicalExpression(0))
                val right = visit(ctx.logicalExpression(1))
                val op = when {
                    ctx.text.contains("and") || ctx.text.contains("&") -> "and"
                    ctx.text.contains("or") -> "or"
                    ctx.text.contains("xor") -> "xor"
                    ctx.text.contains("=>") || ctx.text.contains("implies") -> "implies"
                    else -> throw OclParseException("Unknown logical operator")
                }
                // Find the actual operator in the parse tree
                val operator = (1 until ctx.childCount - 1).firstNotNullOfOrNull { i ->
                    val child = ctx.getChild(i)
                    if (child !is ParseTree || child is OCLParser.LogicalExpressionContext) null
                    else child.text
                } ?: op
                return InfixExp(left, normalizeOperator(operator), right)
            }

            // Delegate to equality expression
            return visit(ctx.equalityExpression())
        }

        override fun visitEqualityExpression(ctx: OCLParser.EqualityExpressionContext): OclExpression {
            val left = visit(ctx.additiveExpression(0))

            if (ctx.comparisonOp() != null) {
                val op = ctx.comparisonOp().text
                val right = visit(ctx.additiveExpression(1))
                return InfixExp(left, normalizeOperator(op), right)
            }

            return left
        }

        override fun visitAdditiveExpression(ctx: OCLParser.AdditiveExpressionContext): OclExpression {
            if (ctx.additiveExpression() != null) {
                val left = visit(ctx.additiveExpression())
                val right = visit(ctx.multiplicativeExpression())
                val op = ctx.getChild(1).text
                return InfixExp(left, op, right)
            }
            return visit(ctx.multiplicativeExpression())
        }

        override fun visitMultiplicativeExpression(ctx: OCLParser.MultiplicativeExpressionContext): OclExpression {
            if (ctx.multiplicativeExpression() != null) {
                val left = visit(ctx.multiplicativeExpression())
                val right = visit(ctx.unaryExpression())
                val op = ctx.getChild(1).text
                return InfixExp(left, op, right)
            }
            return visit(ctx.unaryExpression())
        }

        override fun visitUnaryExpression(ctx: OCLParser.UnaryExpressionContext): OclExpression {
            if (ctx.unaryExpression() != null) {
                val op = ctx.getChild(0).text
                return PrefixExp(op, visit(ctx.unaryExpression()))
            }
            return visit(ctx.postfixExpression())
        }

        // ===== Postfix Expression with Suffixes =====

        override fun visitPostfixExpression(ctx: OCLParser.PostfixExpressionContext): OclExpression {
            var expr = visit(ctx.primaryExpression())

            // Apply each suffix in order
            for (dotSuffix in ctx.dotSuffix()) {
                expr = applyDotSuffix(expr, dotSuffix)
            }
            for (arrowSuffix in ctx.arrowSuffix()) {
                expr = applyArrowSuffix(expr, arrowSuffix)
            }

            return expr
        }

        private fun applyDotSuffix(source: OclExpression, ctx: OCLParser.DotSuffixContext): OclExpression {
            // Property access or method call
            if (ctx.ID() != null) {
                val name = ctx.ID().text

                // Check if it's a method call with arguments
                if (ctx.expressionList() != null) {
                    val args = ctx.expressionList().expression().map { visit(it) }
                    return OperationCallExp(source, name, args)
                }

                // Check for empty parentheses (nullary method)
                if (ctx.text.contains("()")) {
                    return OperationCallExp(source, name, emptyList())
                }

                // Simple property access
                return PropertyCallExp(source, name)
            }

            // Index access [expr]
            if (ctx.expression() != null) {
                val index = visit(ctx.expression())
                return ArrowCallExp(source, "at", listOf(index))
            }

            throw OclParseException("Unknown dot suffix: ${ctx.text}")
        }

        private fun applyArrowSuffix(source: OclExpression, ctx: OCLParser.ArrowSuffixContext): OclExpression {
            return when {
                ctx.nullaryArrowSuffix() != null -> applyNullaryArrow(source, ctx.nullaryArrowSuffix())
                ctx.unaryArrowSuffix() != null -> applyUnaryArrow(source, ctx.unaryArrowSuffix())
                ctx.binaryArrowSuffix() != null -> applyBinaryArrow(source, ctx.binaryArrowSuffix())
                ctx.typeArrowSuffix() != null -> applyTypeArrow(source, ctx.typeArrowSuffix())
                ctx.iteratorSuffix() != null -> applyIteratorSuffix(source, ctx.iteratorSuffix())
                ctx.iterateSuffix() != null -> applyIterateSuffix(source, ctx.iterateSuffix())
                else -> throw OclParseException("Unknown arrow suffix: ${ctx.text}")
            }
        }

        private fun applyNullaryArrow(source: OclExpression, ctx: OCLParser.NullaryArrowSuffixContext): OclExpression {
            val opName = extractOperationName(ctx.text)
            return ArrowCallExp(source, opName, emptyList())
        }

        private fun applyUnaryArrow(source: OclExpression, ctx: OCLParser.UnaryArrowSuffixContext): OclExpression {
            val opName = extractOperationName(ctx.getChild(0).text)
            val arg = visit(ctx.expression())
            return ArrowCallExp(source, opName, listOf(arg))
        }

        private fun applyBinaryArrow(source: OclExpression, ctx: OCLParser.BinaryArrowSuffixContext): OclExpression {
            val opName = extractOperationName(ctx.getChild(0).text)
            val arg1 = visit(ctx.expression(0))
            val arg2 = visit(ctx.expression(1))
            return ArrowCallExp(source, opName, listOf(arg1, arg2))
        }

        private fun applyTypeArrow(source: OclExpression, ctx: OCLParser.TypeArrowSuffixContext): OclExpression {
            val opToken = ctx.getChild(0).text
            val opName = when {
                opToken.contains("oclAsType") -> "oclAsType"
                opToken.contains("oclIsTypeOf") -> "oclIsTypeOf"
                opToken.contains("oclIsKindOf") -> "oclIsKindOf"
                opToken.contains("oclAsSet") -> "oclAsSet"
                opToken.contains("selectByKind") -> "selectByKind"
                opToken.contains("selectByType") -> "selectByType"
                else -> extractOperationName(opToken)
            }
            val typeName = ctx.typeName().text
            return TypeExp(source, opName, typeName)
        }

        private fun applyIteratorSuffix(source: OclExpression, ctx: OCLParser.IteratorSuffixContext): OclExpression {
            return when {
                ctx.collectSuffix() != null -> applyCollect(source, ctx.collectSuffix())
                ctx.selectSuffix() != null -> applySelect(source, ctx.selectSuffix())
                ctx.rejectSuffix() != null -> applyReject(source, ctx.rejectSuffix())
                ctx.forAllSuffix() != null -> applyForAll(source, ctx.forAllSuffix())
                ctx.existsSuffix() != null -> applyExists(source, ctx.existsSuffix())
                ctx.exists1Suffix() != null -> applyExists1(source, ctx.exists1Suffix())
                ctx.oneSuffix() != null -> applyOne(source, ctx.oneSuffix())
                ctx.anySuffix() != null -> applyAny(source, ctx.anySuffix())
                ctx.closureSuffix() != null -> applyClosure(source, ctx.closureSuffix())
                ctx.sortedBySuffix() != null -> applySortedBy(source, ctx.sortedBySuffix())
                ctx.isUniqueSuffix() != null -> applyIsUnique(source, ctx.isUniqueSuffix())
                else -> throw OclParseException("Unknown iterator suffix: ${ctx.text}")
            }
        }

        private fun applyCollect(source: OclExpression, ctx: OCLParser.CollectSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "collect", iterVar, body)
        }

        private fun applySelect(source: OclExpression, ctx: OCLParser.SelectSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "select", iterVar, body)
        }

        private fun applyReject(source: OclExpression, ctx: OCLParser.RejectSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "reject", iterVar, body)
        }

        private fun applyForAll(source: OclExpression, ctx: OCLParser.ForAllSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "forAll", iterVar, body)
        }

        private fun applyExists(source: OclExpression, ctx: OCLParser.ExistsSuffixContext): OclExpression {
            // Support both full form (x | body) and shorthand form (body)
            val iterVar = ctx.iteratorVar?.text ?: "_it"
            val body = visit(ctx.body)
            return IteratorExp(source, "exists", iterVar, body)
        }

        private fun applyExists1(source: OclExpression, ctx: OCLParser.Exists1SuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "exists1", iterVar, body)
        }

        private fun applyOne(source: OclExpression, ctx: OCLParser.OneSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "one", iterVar, body)
        }

        private fun applyAny(source: OclExpression, ctx: OCLParser.AnySuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "any", iterVar, body)
        }

        private fun applyClosure(source: OclExpression, ctx: OCLParser.ClosureSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "closure", iterVar, body)
        }

        private fun applySortedBy(source: OclExpression, ctx: OCLParser.SortedBySuffixContext): OclExpression {
            // Check for simple property name form: ->sortedBy(propertyName)
            if (ctx.propertyName != null) {
                val propName = ctx.propertyName.text
                // Create an iterator that accesses the property
                return IteratorExp(source, "sortedBy", "_it", PropertyCallExp(VariableExp("_it"), propName))
            }

            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "sortedBy", iterVar, body)
        }

        private fun applyIsUnique(source: OclExpression, ctx: OCLParser.IsUniqueSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val body = visit(ctx.body)
            return IteratorExp(source, "isUnique", iterVar, body)
        }

        private fun applyIterateSuffix(source: OclExpression, ctx: OCLParser.IterateSuffixContext): OclExpression {
            val iterVar = ctx.iteratorVar.text
            val accVar = ctx.accumulatorVar.text
            val accInit = visit(ctx.accumulatorInit)
            val body = visit(ctx.body)
            return IterateExp(source, iterVar, accVar, accInit, body)
        }

        // ===== Primary Expressions =====

        override fun visitPrimaryExpression(ctx: OCLParser.PrimaryExpressionContext): OclExpression {
            return when {
                ctx.nullLiteral() != null -> NullLiteralExp
                ctx.booleanLiteral() != null -> visitBooleanLiteral(ctx.booleanLiteral())
                ctx.intLiteral() != null -> visitIntLiteral(ctx.intLiteral())
                ctx.floatLiteral() != null -> visitFloatLiteral(ctx.floatLiteral())
                ctx.stringLiteral() != null -> visitStringLiteral(ctx.stringLiteral())
                ctx.collectionLiteral() != null -> visitCollectionLiteral(ctx.collectionLiteral())
                ctx.enumerationLiteralExp() != null -> visitEnumerationLiteralExp(ctx.enumerationLiteralExp())
                ctx.preValueExp() != null -> visitPreValueExp(ctx.preValueExp())
                ctx.standaloneTypeOp() != null -> visitStandaloneTypeOp(ctx.standaloneTypeOp())
                ctx.standaloneOperationCall() != null -> visitStandaloneOperationCall(ctx.standaloneOperationCall())
                ctx.variableExp() != null -> visitVariableExp(ctx.variableExp())
                ctx.parenthesizedExpression() != null -> visit(ctx.parenthesizedExpression().expression())
                else -> throw OclParseException("Unknown primary expression: ${ctx.text}")
            }
        }

        override fun visitStandaloneTypeOp(ctx: OCLParser.StandaloneTypeOpContext): OclExpression {
            // Standalone type operations have implicit self as source
            val source = VariableExp("_it")  // Use iterator variable or self
            val typeName = ctx.typeName().text
            val opName = when {
                ctx.text.startsWith("oclIsKindOf") -> "oclIsKindOf"
                ctx.text.startsWith("oclIsTypeOf") -> "oclIsTypeOf"
                ctx.text.startsWith("oclAsType") -> "oclAsType"
                else -> throw OclParseException("Unknown standalone type operation: ${ctx.text}")
            }
            return TypeExp(source, opName, typeName)
        }

        override fun visitStandaloneOperationCall(ctx: OCLParser.StandaloneOperationCallContext): OclExpression {
            // Standalone operation calls have implicit self as source
            val source = VariableExp("self")
            val opName = ctx.ID().text
            val args = ctx.expressionList()?.expression()?.map { visit(it) } ?: emptyList()
            return OperationCallExp(source, opName, args)
        }

        override fun visitBooleanLiteral(ctx: OCLParser.BooleanLiteralContext): OclExpression {
            return BooleanLiteralExp(ctx.text == "true")
        }

        override fun visitIntLiteral(ctx: OCLParser.IntLiteralContext): OclExpression {
            return IntegerLiteralExp(ctx.INT().text.toLong())
        }

        override fun visitFloatLiteral(ctx: OCLParser.FloatLiteralContext): OclExpression {
            return RealLiteralExp(ctx.FLOAT_LITERAL().text.toDouble())
        }

        override fun visitStringLiteral(ctx: OCLParser.StringLiteralContext): OclExpression {
            val text = ctx.text
            // Remove quotes and handle escape sequences
            val content = if (text.startsWith("\"")) {
                text.removeSurrounding("\"")
            } else {
                text.removeSurrounding("'")
            }
            return StringLiteralExp(unescapeString(content))
        }

        override fun visitCollectionLiteral(ctx: OCLParser.CollectionLiteralContext): OclExpression {
            val kind = when {
                ctx.text.startsWith("Set") -> CollectionKind.SET
                ctx.text.startsWith("Bag") -> CollectionKind.BAG
                ctx.text.startsWith("Sequence") -> CollectionKind.SEQUENCE
                ctx.text.startsWith("OrderedSet") -> CollectionKind.ORDERED_SET
                else -> CollectionKind.SET
            }

            val parts = ctx.expressionList()?.expression()?.map { visit(it) } ?: emptyList()
            return CollectionLiteralExp(kind, parts)
        }

        override fun visitEnumerationLiteralExp(ctx: OCLParser.EnumerationLiteralExpContext): OclExpression {
            // Parse "Type::value" into a variable reference for now
            // Could create a dedicated EnumerationLiteralExp if needed
            return VariableExp(ctx.ENUMERATION_LITERAL().text)
        }

        override fun visitPreValueExp(ctx: OCLParser.PreValueExpContext): OclExpression {
            // Pre-value expression (var@pre) - translate to special variable access
            val varName = ctx.ID().text
            return VariableExp("${varName}@pre")
        }

        override fun visitVariableExp(ctx: OCLParser.VariableExpContext): OclExpression {
            return VariableExp(ctx.ID().text)
        }

        // ===== Helper Methods =====

        /**
         * Extract operation name from arrow token (e.g., "->size()" -> "size")
         */
        private fun extractOperationName(token: String): String {
            return token
                .removePrefix("->")
                .removeSuffix("()")
                .removeSuffix("(")
        }

        /**
         * Normalize comparison operators to standard OCL forms.
         */
        private fun normalizeOperator(op: String): String {
            return when (op) {
                "/=" -> "<>"
                "=>" -> "implies"
                "&" -> "and"
                else -> op
            }
        }

        /**
         * Unescape string literal content.
         */
        private fun unescapeString(s: String): String {
            val sb = StringBuilder()
            var i = 0
            while (i < s.length) {
                if (s[i] == '\\' && i + 1 < s.length) {
                    i++
                    sb.append(when (s[i]) {
                        'n' -> '\n'
                        't' -> '\t'
                        'r' -> '\r'
                        'b' -> '\b'
                        'f' -> '\u000C'
                        '\'' -> '\''
                        '"' -> '"'
                        '\\' -> '\\'
                        else -> s[i]
                    })
                } else {
                    sb.append(s[i])
                }
                i++
            }
            return sb.toString()
        }
    }
}

/**
 * Custom error listener for ANTLR parser.
 */
private class OclErrorListener : org.antlr.v4.runtime.BaseErrorListener() {
    override fun syntaxError(
        recognizer: org.antlr.v4.runtime.Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: org.antlr.v4.runtime.RecognitionException?
    ) {
        throw OclParseException("Syntax error at line $line:$charPositionInLine - $msg")
    }
}
