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
package org.openmbee.mdm.framework.query.gql.parser

import org.antlr.v4.runtime.*
import org.openmbee.mdm.framework.query.gql.antlr.GQLBaseVisitor
import org.openmbee.mdm.framework.query.gql.antlr.GQLLexer
import org.openmbee.mdm.framework.query.gql.antlr.GQLParser
import org.openmbee.mdm.framework.query.gql.ast.*

/**
 * Parser that converts GQL query strings to AST.
 */
object GqlParser {

    /**
     * Parse a GQL query string and return the AST.
     */
    fun parse(query: String): GqlQuery {
        val lexer = GQLLexer(CharStreams.fromString(query))
        val tokens = CommonTokenStream(lexer)
        val parser = GQLParser(tokens)

        // Configure error handling
        parser.removeErrorListeners()
        parser.addErrorListener(GqlErrorListener())

        val tree = parser.gqlProgram()
        return GqlAstBuilder().visitGqlProgram(tree)
    }
}

/**
 * Exception thrown when GQL parsing fails.
 */
class GqlParseException(message: String) : RuntimeException(message)

/**
 * Custom error listener for ANTLR parser.
 */
private class GqlErrorListener : BaseErrorListener() {
    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        throw GqlParseException("Syntax error at line $line:$charPositionInLine - $msg")
    }
}

/**
 * Visitor that builds AST nodes from ANTLR parse tree contexts.
 */
private class GqlAstBuilder : GQLBaseVisitor<Any?>() {

    override fun visitGqlProgram(ctx: GQLParser.GqlProgramContext): GqlQuery {
        // Navigate to the query content
        val programActivity = ctx.programActivity() ?: throw GqlParseException("Empty query")
        val transactionActivity = programActivity.transactionActivity()
            ?: throw GqlParseException("No transaction activity found")
        val procedureSpec = transactionActivity.procedureSpecification()
            ?: throw GqlParseException("No procedure specification found")

        return processProcedureSpecification(procedureSpec)
    }

    private fun processProcedureSpecification(ctx: GQLParser.ProcedureSpecificationContext): GqlQuery {
        val body = ctx.procedureBody()
        return processProcedureBody(body)
    }

    private fun processProcedureBody(ctx: GQLParser.ProcedureBodyContext): GqlQuery {
        val statementBlock = ctx.statementBlock()
        return processStatementBlock(statementBlock)
    }

    private fun processStatementBlock(ctx: GQLParser.StatementBlockContext): GqlQuery {
        val statement = ctx.statement()
        return processStatement(statement)
    }

    private fun processStatement(ctx: GQLParser.StatementContext): GqlQuery {
        // Handle composite query statement (which contains linear query statement)
        val compositeQuery = ctx.compositeQueryStatement()
        if (compositeQuery != null) {
            return processCompositeQueryStatement(compositeQuery)
        }
        throw GqlParseException("Unsupported statement type")
    }

    private fun processCompositeQueryStatement(ctx: GQLParser.CompositeQueryStatementContext): GqlQuery {
        return processCompositeQueryExpression(ctx.compositeQueryExpression())
    }

    private fun processCompositeQueryExpression(ctx: GQLParser.CompositeQueryExpressionContext): GqlQuery {
        val primary = ctx.compositeQueryPrimary()
        if (primary != null) {
            return processCompositeQueryPrimary(primary)
        }
        throw GqlParseException("No query primary found")
    }

    private fun processCompositeQueryPrimary(ctx: GQLParser.CompositeQueryPrimaryContext): GqlQuery {
        return processLinearQueryStatement(ctx.linearQueryStatement())
    }

    private fun processLinearQueryStatement(ctx: GQLParser.LinearQueryStatementContext): GqlQuery {
        val focused = ctx.focusedLinearQueryStatement()
        if (focused != null) {
            return processFocusedLinearQueryStatement(focused)
        }
        val ambient = ctx.ambientLinearQueryStatement()
        if (ambient != null) {
            return processAmbientLinearQueryStatement(ambient)
        }
        throw GqlParseException("No linear query found")
    }

    private fun processFocusedLinearQueryStatement(ctx: GQLParser.FocusedLinearQueryStatementContext): GqlQuery {
        // Handle SELECT statement
        val selectStmt = ctx.selectStatement()
        if (selectStmt != null) {
            return processSelectStatement(selectStmt)
        }

        // Handle focused query parts
        val parts = ctx.focusedLinearQueryStatementPart()
        val andPart = ctx.focusedLinearQueryAndPrimitiveResultStatementPart()

        if (andPart != null) {
            val matchClauses = mutableListOf<MatchClause>()
            var whereClause: WhereClause? = null

            // Collect matches from parts
            for (part in parts) {
                val simpleLinear = part.simpleLinearQueryStatement()
                if (simpleLinear != null) {
                    val (matches, where) = extractMatchesAndWhere(simpleLinear)
                    matchClauses.addAll(matches)
                    if (where != null) whereClause = where
                }
            }

            // Process the and part
            val simpleLinear = andPart.simpleLinearQueryStatement()
            if (simpleLinear != null) {
                val (matches, where) = extractMatchesAndWhere(simpleLinear)
                matchClauses.addAll(matches)
                if (where != null) whereClause = where
            }

            val returnClause = processPrimitiveResultStatement(andPart.primitiveResultStatement())
            return GqlQuery(matchClauses, whereClause, returnClause)
        }

        throw GqlParseException("Unsupported focused linear query")
    }

    private fun processSelectStatement(ctx: GQLParser.SelectStatementContext): GqlQuery {
        val matchClauses = mutableListOf<MatchClause>()
        var whereClause: WhereClause? = null

        // Handle FROM clause with graph matches
        val selectBody = ctx.selectStatementBody()
        if (selectBody != null) {
            val graphMatchList = selectBody.selectGraphMatchList()
            if (graphMatchList != null) {
                for (graphMatch in graphMatchList.selectGraphMatch()) {
                    val matchStmt = graphMatch.matchStatement()
                    matchClauses.add(processMatchStatement(matchStmt))
                }
            }
        }

        // Handle WHERE clause
        val whereCtx = ctx.whereClause()
        if (whereCtx != null) {
            whereClause = processWhereClause(whereCtx)
        }

        // Handle SELECT items
        val items = mutableListOf<ReturnItem>()
        val distinct = ctx.setQuantifier()?.DISTINCT() != null
        val returnAll = ctx.ASTERISK() != null

        if (!returnAll) {
            val selectItemList = ctx.selectItemList()
            if (selectItemList != null) {
                for (item in selectItemList.selectItem()) {
                    val expr = processAggregatingValueExpression(item.aggregatingValueExpression())
                    val alias = item.selectItemAlias()?.identifier()?.text
                    items.add(ReturnItem(expr, alias))
                }
            }
        }

        return GqlQuery(matchClauses, whereClause, ReturnClause(items, distinct, returnAll))
    }

    private fun processAmbientLinearQueryStatement(ctx: GQLParser.AmbientLinearQueryStatementContext): GqlQuery {
        val simpleLinear = ctx.simpleLinearQueryStatement()
        val primitiveResult = ctx.primitiveResultStatement()

        val matchClauses = mutableListOf<MatchClause>()
        var whereClause: WhereClause? = null

        if (simpleLinear != null) {
            val (matches, where) = extractMatchesAndWhere(simpleLinear)
            matchClauses.addAll(matches)
            whereClause = where
        }

        val returnClause = if (primitiveResult != null) {
            processPrimitiveResultStatement(primitiveResult)
        } else {
            ReturnClause(emptyList())
        }

        return GqlQuery(matchClauses, whereClause, returnClause)
    }

    private fun extractMatchesAndWhere(ctx: GQLParser.SimpleLinearQueryStatementContext): Pair<List<MatchClause>, WhereClause?> {
        val matchClauses = mutableListOf<MatchClause>()
        var whereClause: WhereClause? = null

        for (simpleQuery in ctx.simpleQueryStatement()) {
            val primitiveQuery = simpleQuery.primitiveQueryStatement()
            if (primitiveQuery != null) {
                val matchStmt = primitiveQuery.matchStatement()
                if (matchStmt != null) {
                    matchClauses.add(processMatchStatement(matchStmt))
                }

                val filterStmt = primitiveQuery.filterStatement()
                if (filterStmt != null) {
                    whereClause = processFilterStatement(filterStmt)
                }
            }
        }

        return matchClauses to whereClause
    }

    private fun processMatchStatement(ctx: GQLParser.MatchStatementContext): MatchClause {
        val simpleMatch = ctx.simpleMatchStatement()
        if (simpleMatch != null) {
            return processSimpleMatchStatement(simpleMatch, false)
        }
        val optionalMatch = ctx.optionalMatchStatement()
        if (optionalMatch != null) {
            val operand = optionalMatch.optionalOperand()
            val simpleMatchInner = operand.simpleMatchStatement()
            if (simpleMatchInner != null) {
                return processSimpleMatchStatement(simpleMatchInner, true)
            }
        }
        throw GqlParseException("Unsupported match statement")
    }

    private fun processSimpleMatchStatement(
        ctx: GQLParser.SimpleMatchStatementContext,
        isOptional: Boolean
    ): MatchClause {
        val graphPatternBindingTable = ctx.graphPatternBindingTable()
        val graphPattern = graphPatternBindingTable.graphPattern()

        val patterns = processGraphPattern(graphPattern)

        return MatchClause(patterns, isOptional)
    }

    private fun processGraphPattern(ctx: GQLParser.GraphPatternContext): List<PathPattern> {
        val pathPatternList = ctx.pathPatternList()
        return pathPatternList.pathPattern().map { processPathPattern(it) }
    }

    private fun processPathPattern(ctx: GQLParser.PathPatternContext): PathPattern {
        val pathVar = ctx.pathVariableDeclaration()?.pathVariable()?.bindingVariable()?.text
        val pathPatternExpr = ctx.pathPatternExpression()

        val elements = processPathPatternExpression(pathPatternExpr)
        return PathPattern(elements, pathVar)
    }

    private fun processPathPatternExpression(ctx: GQLParser.PathPatternExpressionContext): List<GraphPattern> {
        // Handle different path pattern expression types
        return when (ctx) {
            is GQLParser.PpePathTermContext -> processPathTerm(ctx.pathTerm())
            is GQLParser.PpeMultisetAlternationContext -> {
                // For simplicity, take first path term
                processPathTerm(ctx.pathTerm(0))
            }

            is GQLParser.PpePatternUnionContext -> {
                // For simplicity, take first path term
                processPathTerm(ctx.pathTerm(0))
            }

            else -> throw GqlParseException("Unknown path pattern expression type")
        }
    }

    private fun processPathTerm(ctx: GQLParser.PathTermContext): List<GraphPattern> {
        val elements = mutableListOf<GraphPattern>()
        for (factor in ctx.pathFactor()) {
            elements.addAll(processPathFactor(factor))
        }
        return elements
    }

    private fun processPathFactor(ctx: GQLParser.PathFactorContext): List<GraphPattern> {
        return when (ctx) {
            is GQLParser.PfPathPrimaryContext -> processPathPrimary(ctx.pathPrimary(), null)
            is GQLParser.PfQuantifiedPathPrimaryContext -> {
                val quantifier = processGraphPatternQuantifier(ctx.graphPatternQuantifier())
                processPathPrimary(ctx.pathPrimary(), quantifier)
            }

            is GQLParser.PfQuestionedPathPrimaryContext -> {
                processPathPrimary(ctx.pathPrimary(), RangeQuantifier.OPTIONAL)
            }

            else -> throw GqlParseException("Unknown path factor type")
        }
    }

    private fun processPathPrimary(ctx: GQLParser.PathPrimaryContext, quantifier: PathQuantifier?): List<GraphPattern> {
        return when (ctx) {
            is GQLParser.PpElementPatternContext -> {
                listOf(processElementPattern(ctx.elementPattern(), quantifier))
            }

            is GQLParser.PpParenthesizedPathPatternExpressionContext -> {
                val inner = ctx.parenthesizedPathPatternExpression()
                processPathPatternExpression(inner.pathPatternExpression())
            }

            is GQLParser.PpSimplifiedPathPatternExpressionContext -> {
                // Simplified path patterns like -/Label/->
                processSimplifiedPathPatternExpression(ctx.simplifiedPathPatternExpression(), quantifier)
            }

            else -> throw GqlParseException("Unknown path primary type")
        }
    }

    private fun processSimplifiedPathPatternExpression(
        ctx: GQLParser.SimplifiedPathPatternExpressionContext,
        quantifier: PathQuantifier?
    ): List<GraphPattern> {
        // Handle simplified path patterns like -/Label/->
        val direction = when (ctx) {
            is GQLParser.SimplifiedDefaultingLeftContext -> EdgeDirection.LEFT
            is GQLParser.SimplifiedDefaultingRightContext -> EdgeDirection.RIGHT
            is GQLParser.SimplifiedDefaultingUndirectedContext -> EdgeDirection.UNDIRECTED
            is GQLParser.SimplifiedDefaultingAnyDirectionContext -> EdgeDirection.ANY
            is GQLParser.SimplifiedDefaultingLeftOrUndirectedContext -> EdgeDirection.LEFT_OR_UNDIRECTED
            is GQLParser.SimplifiedDefaultingUndirectedOrRightContext -> EdgeDirection.UNDIRECTED_OR_RIGHT
            is GQLParser.SimplifiedDefaultingLeftOrRightContext -> EdgeDirection.LEFT_OR_RIGHT
            else -> EdgeDirection.ANY
        }

        // Extract labels from simplified contents
        val labels = mutableListOf<String>()
        val simplifiedContents = when (ctx) {
            is GQLParser.SimplifiedDefaultingLeftContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingRightContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingUndirectedContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingAnyDirectionContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingLeftOrUndirectedContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingUndirectedOrRightContext -> ctx.simplifiedContents()
            is GQLParser.SimplifiedDefaultingLeftOrRightContext -> ctx.simplifiedContents()
            else -> null
        }

        if (simplifiedContents != null) {
            extractLabelsFromSimplifiedContents(simplifiedContents, labels)
        }

        return listOf(EdgePattern(null, labels, direction, null, quantifier))
    }

    private fun extractLabelsFromSimplifiedContents(
        ctx: GQLParser.SimplifiedContentsContext,
        labels: MutableList<String>
    ) {
        val term = ctx.simplifiedTerm()
        if (term != null) {
            extractLabelsFromSimplifiedTerm(term, labels)
        }
    }

    private fun extractLabelsFromSimplifiedTerm(ctx: GQLParser.SimplifiedTermContext, labels: MutableList<String>) {
        when (ctx) {
            is GQLParser.SimplifiedFactorLowLabelContext -> {
                extractLabelsFromSimplifiedFactorLow(ctx.simplifiedFactorLow(), labels)
            }

            is GQLParser.SimplifiedConcatenationLabelContext -> {
                extractLabelsFromSimplifiedTerm(ctx.simplifiedTerm(), labels)
                extractLabelsFromSimplifiedFactorLow(ctx.simplifiedFactorLow(), labels)
            }
        }
    }

    private fun extractLabelsFromSimplifiedFactorLow(
        ctx: GQLParser.SimplifiedFactorLowContext,
        labels: MutableList<String>
    ) {
        when (ctx) {
            is GQLParser.SimplifiedFactorHighLabelContext -> {
                extractLabelsFromSimplifiedFactorHigh(ctx.simplifiedFactorHigh(), labels)
            }

            is GQLParser.SimplifiedConjunctionLabelContext -> {
                extractLabelsFromSimplifiedFactorLow(ctx.simplifiedFactorLow(), labels)
                extractLabelsFromSimplifiedFactorHigh(ctx.simplifiedFactorHigh(), labels)
            }
        }
    }

    private fun extractLabelsFromSimplifiedFactorHigh(
        ctx: GQLParser.SimplifiedFactorHighContext,
        labels: MutableList<String>
    ) {
        val tertiary = ctx.simplifiedTertiary()
        if (tertiary != null) {
            extractLabelsFromSimplifiedTertiary(tertiary, labels)
        }
    }

    private fun extractLabelsFromSimplifiedTertiary(
        ctx: GQLParser.SimplifiedTertiaryContext,
        labels: MutableList<String>
    ) {
        val secondary = ctx.simplifiedSecondary()
        if (secondary != null) {
            extractLabelsFromSimplifiedSecondary(secondary, labels)
        }
    }

    private fun extractLabelsFromSimplifiedSecondary(
        ctx: GQLParser.SimplifiedSecondaryContext,
        labels: MutableList<String>
    ) {
        val primary = ctx.simplifiedPrimary()
        if (primary != null) {
            val labelName = primary.labelName()
            if (labelName != null) {
                labels.add(labelName.text)
            }
        }
    }

    private fun processElementPattern(ctx: GQLParser.ElementPatternContext, quantifier: PathQuantifier?): GraphPattern {
        val nodePattern = ctx.nodePattern()
        if (nodePattern != null) {
            return processNodePattern(nodePattern)
        }
        val edgePattern = ctx.edgePattern()
        if (edgePattern != null) {
            return processEdgePattern(edgePattern, quantifier)
        }
        throw GqlParseException("Unknown element pattern")
    }

    private fun processNodePattern(ctx: GQLParser.NodePatternContext): NodePattern {
        val filler = ctx.elementPatternFiller()

        val variable = filler.elementVariableDeclaration()?.elementVariable()?.bindingVariable()?.text
        val labels = extractLabels(filler.isLabelExpression())
        val properties = extractProperties(filler.elementPatternPredicate())

        return NodePattern(variable, labels, properties)
    }

    private fun processEdgePattern(ctx: GQLParser.EdgePatternContext, quantifier: PathQuantifier?): EdgePattern {
        val fullEdge = ctx.fullEdgePattern()
        if (fullEdge != null) {
            return processFullEdgePattern(fullEdge, quantifier)
        }

        val abbrevEdge = ctx.abbreviatedEdgePattern()
        if (abbrevEdge != null) {
            return processAbbreviatedEdgePattern(abbrevEdge, quantifier)
        }

        throw GqlParseException("Unknown edge pattern type")
    }

    private fun processFullEdgePattern(ctx: GQLParser.FullEdgePatternContext, quantifier: PathQuantifier?): EdgePattern {
        val (direction, filler) = when {
            ctx.fullEdgePointingLeft() != null -> EdgeDirection.LEFT to ctx.fullEdgePointingLeft()
                .elementPatternFiller()

            ctx.fullEdgePointingRight() != null -> EdgeDirection.RIGHT to ctx.fullEdgePointingRight()
                .elementPatternFiller()

            ctx.fullEdgeUndirected() != null -> EdgeDirection.UNDIRECTED to ctx.fullEdgeUndirected()
                .elementPatternFiller()

            ctx.fullEdgeLeftOrUndirected() != null -> EdgeDirection.LEFT_OR_UNDIRECTED to ctx.fullEdgeLeftOrUndirected()
                .elementPatternFiller()

            ctx.fullEdgeUndirectedOrRight() != null -> EdgeDirection.UNDIRECTED_OR_RIGHT to ctx.fullEdgeUndirectedOrRight()
                .elementPatternFiller()

            ctx.fullEdgeLeftOrRight() != null -> EdgeDirection.LEFT_OR_RIGHT to ctx.fullEdgeLeftOrRight()
                .elementPatternFiller()

            ctx.fullEdgeAnyDirection() != null -> EdgeDirection.ANY to ctx.fullEdgeAnyDirection().elementPatternFiller()
            else -> throw GqlParseException("Unknown full edge pattern")
        }

        val variable = filler.elementVariableDeclaration()?.elementVariable()?.bindingVariable()?.text
        val labels = extractLabels(filler.isLabelExpression())
        val properties = extractProperties(filler.elementPatternPredicate())

        return EdgePattern(variable, labels, direction, properties, quantifier)
    }

    private fun processAbbreviatedEdgePattern(
        ctx: GQLParser.AbbreviatedEdgePatternContext,
        quantifier: PathQuantifier?
    ): EdgePattern {
        val direction = when {
            ctx.LEFT_ARROW() != null -> EdgeDirection.LEFT
            ctx.RIGHT_ARROW() != null -> EdgeDirection.RIGHT
            ctx.TILDE() != null -> EdgeDirection.UNDIRECTED
            ctx.LEFT_ARROW_TILDE() != null -> EdgeDirection.LEFT_OR_UNDIRECTED
            ctx.TILDE_RIGHT_ARROW() != null -> EdgeDirection.UNDIRECTED_OR_RIGHT
            ctx.LEFT_MINUS_RIGHT() != null -> EdgeDirection.LEFT_OR_RIGHT
            ctx.MINUS_SIGN() != null -> EdgeDirection.ANY
            else -> EdgeDirection.ANY
        }

        return EdgePattern(null, emptyList(), direction, null, quantifier)
    }

    private fun processGraphPatternQuantifier(ctx: GQLParser.GraphPatternQuantifierContext): PathQuantifier {
        return when {
            ctx.ASTERISK() != null -> RangeQuantifier.STAR
            ctx.PLUS_SIGN() != null -> RangeQuantifier.PLUS
            ctx.fixedQuantifier() != null -> {
                val count = ctx.fixedQuantifier().unsignedInteger().text.toInt()
                FixedQuantifier(count)
            }

            ctx.generalQuantifier() != null -> {
                val lower = ctx.generalQuantifier().lowerBound()?.text?.toIntOrNull()
                val upper = ctx.generalQuantifier().upperBound()?.text?.toIntOrNull()
                RangeQuantifier(lower, upper)
            }

            else -> RangeQuantifier.STAR
        }
    }

    private fun extractLabels(ctx: GQLParser.IsLabelExpressionContext?): List<String> {
        if (ctx == null) return emptyList()
        return extractLabelsFromExpression(ctx.labelExpression())
    }

    private fun extractLabelsFromExpression(ctx: GQLParser.LabelExpressionContext): List<String> {
        return when (ctx) {
            is GQLParser.LabelExpressionNameContext -> listOf(ctx.labelName().text)
            is GQLParser.LabelExpressionDisjunctionContext -> {
                ctx.labelExpression().flatMap { extractLabelsFromExpression(it) }
            }

            is GQLParser.LabelExpressionConjunctionContext -> {
                ctx.labelExpression().flatMap { extractLabelsFromExpression(it) }
            }

            is GQLParser.LabelExpressionParenthesizedContext -> {
                extractLabelsFromExpression(ctx.labelExpression())
            }

            is GQLParser.LabelExpressionWildcardContext -> emptyList()  // % matches any
            is GQLParser.LabelExpressionNegationContext -> {
                // Negation is complex, skip for now
                emptyList()
            }

            else -> emptyList()
        }
    }

    private fun extractProperties(ctx: GQLParser.ElementPatternPredicateContext?): Map<String, GqlExpression>? {
        if (ctx == null) return null

        val propSpec = ctx.elementPropertySpecification() ?: return null
        val propList = propSpec.propertyKeyValuePairList() ?: return null

        val props = mutableMapOf<String, GqlExpression>()
        for (pair in propList.propertyKeyValuePair()) {
            val name = pair.propertyName().text
            val value = processValueExpression(pair.valueExpression())
            props[name] = value
        }
        return props.ifEmpty { null }
    }

    private fun processFilterStatement(ctx: GQLParser.FilterStatementContext): WhereClause {
        val whereCtx = ctx.whereClause()
        if (whereCtx != null) {
            return processWhereClause(whereCtx)
        }
        val searchCondition = ctx.searchCondition()
        if (searchCondition != null) {
            return WhereClause(processSearchCondition(searchCondition))
        }
        throw GqlParseException("Invalid filter statement")
    }

    private fun processWhereClause(ctx: GQLParser.WhereClauseContext): WhereClause {
        return WhereClause(processSearchCondition(ctx.searchCondition()))
    }

    private fun processSearchCondition(ctx: GQLParser.SearchConditionContext): GqlExpression {
        return processBooleanValueExpression(ctx.booleanValueExpression())
    }

    private fun processBooleanValueExpression(ctx: GQLParser.BooleanValueExpressionContext): GqlExpression {
        return processValueExpression(ctx.valueExpression())
    }

    private fun processPrimitiveResultStatement(ctx: GQLParser.PrimitiveResultStatementContext): ReturnClause {
        val returnStmt = ctx.returnStatement() ?: return ReturnClause(emptyList())
        return processReturnStatement(returnStmt)
    }

    private fun processReturnStatement(ctx: GQLParser.ReturnStatementContext): ReturnClause {
        val body = ctx.returnStatementBody()

        val distinct = body.setQuantifier()?.DISTINCT() != null
        val returnAll = body.ASTERISK() != null

        val items = mutableListOf<ReturnItem>()
        if (!returnAll) {
            val itemList = body.returnItemList()
            if (itemList != null) {
                for (item in itemList.returnItem()) {
                    val expr = processAggregatingValueExpression(item.aggregatingValueExpression())
                    val alias = item.returnItemAlias()?.identifier()?.text
                    items.add(ReturnItem(expr, alias))
                }
            }
        }

        return ReturnClause(items, distinct, returnAll)
    }

    private fun processAggregatingValueExpression(ctx: GQLParser.AggregatingValueExpressionContext): GqlExpression {
        return processValueExpression(ctx.valueExpression())
    }

    private fun processValueExpression(ctx: GQLParser.ValueExpressionContext): GqlExpression {
        return when (ctx) {
            is GQLParser.SignedExprAltContext -> {
                val operand = processValueExpression(ctx.valueExpression())
                val op = if (ctx.sign.text == "-") UnaryOperator.NEGATE else UnaryOperator.POSITIVE
                UnaryOp(op, operand)
            }

            is GQLParser.MultDivExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                val op = if (ctx.operator.text == "*") BinaryOperator.MULTIPLY else BinaryOperator.DIVIDE
                BinaryOp(left, op, right)
            }

            is GQLParser.AddSubtractExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                val op = if (ctx.operator.text == "+") BinaryOperator.PLUS else BinaryOperator.MINUS
                BinaryOp(left, op, right)
            }

            is GQLParser.ConcatenationExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                BinaryOp(left, BinaryOperator.CONCATENATION, right)
            }

            is GQLParser.ComparisonExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                val op = processCompOp(ctx.compOp())
                BinaryOp(left, op, right)
            }

            is GQLParser.PredicateExprAltContext -> {
                processPredicate(ctx.predicate())
            }

            is GQLParser.NotExprAltContext -> {
                UnaryOp(UnaryOperator.NOT, processValueExpression(ctx.valueExpression()))
            }

            is GQLParser.IsNotExprAltContext -> {
                val value = processValueExpression(ctx.valueExpression())
                val truthValue = ctx.truthValue().text.uppercase()
                val isNot = ctx.NOT() != null
                val targetValue = truthValue == "TRUE"
                if (isNot) {
                    BinaryOp(value, BinaryOperator.NOT_EQUALS, Literal(targetValue))
                } else {
                    BinaryOp(value, BinaryOperator.EQUALS, Literal(targetValue))
                }
            }

            is GQLParser.ConjunctiveExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                BinaryOp(left, BinaryOperator.AND, right)
            }

            is GQLParser.DisjunctiveExprAltContext -> {
                val left = processValueExpression(ctx.valueExpression(0))
                val right = processValueExpression(ctx.valueExpression(1))
                val op = if (ctx.operator.text.uppercase() == "XOR") BinaryOperator.XOR else BinaryOperator.OR
                BinaryOp(left, op, right)
            }

            is GQLParser.PrimaryExprAltContext -> {
                processValueExpressionPrimary(ctx.valueExpressionPrimary())
            }

            is GQLParser.NormalizedPredicateExprAltContext -> {
                // Normalized predicate - treat as unsupported for now
                processValueExpression(ctx.valueExpression())
            }

            is GQLParser.ValueFunctionExprAltContext -> {
                processValueFunction(ctx.valueFunction())
            }

            else -> throw GqlParseException("Unsupported value expression type: ${ctx::class.simpleName}")
        }
    }

    private fun processCompOp(ctx: GQLParser.CompOpContext): BinaryOperator {
        return when {
            ctx.EQUALS_OPERATOR() != null -> BinaryOperator.EQUALS
            ctx.NOT_EQUALS_OPERATOR() != null -> BinaryOperator.NOT_EQUALS
            ctx.LEFT_ANGLE_BRACKET() != null -> BinaryOperator.LESS_THAN
            ctx.RIGHT_ANGLE_BRACKET() != null -> BinaryOperator.GREATER_THAN
            ctx.LESS_THAN_OR_EQUALS_OPERATOR() != null -> BinaryOperator.LESS_THAN_OR_EQUALS
            ctx.GREATER_THAN_OR_EQUALS_OPERATOR() != null -> BinaryOperator.GREATER_THAN_OR_EQUALS
            else -> throw GqlParseException("Unknown comparison operator")
        }
    }

    private fun processValueFunction(ctx: GQLParser.ValueFunctionContext): GqlExpression {
        // Handle character/string functions (UPPER, LOWER, etc.)
        if (ctx.characterOrByteStringFunction() != null) {
            return processCharacterFunction(ctx.characterOrByteStringFunction())
        }

        // Handle numeric value functions (SIZE, LENGTH, ABS, etc.)
        if (ctx.numericValueFunction() != null) {
            return processNumericValueFunction(ctx.numericValueFunction())
        }

        // Handle list value functions
        if (ctx.listValueFunction() != null) {
            // Simplified list function handling
            return FunctionCall("LIST_FUNC", emptyList())
        }

        // Handle datetime functions
        if (ctx.datetimeValueFunction() != null) {
            return FunctionCall("DATETIME", emptyList())
        }

        // Handle duration functions
        if (ctx.durationValueFunction() != null) {
            return FunctionCall("DURATION", emptyList())
        }

        throw GqlParseException("Unsupported value function: ${ctx.text}")
    }

    private fun processNumericValueFunction(ctx: GQLParser.NumericValueFunctionContext): GqlExpression {
        // Handle length expressions via their text representation
        if (ctx.lengthExpression() != null) {
            val lengthExpr = ctx.lengthExpression()
            // Extract function name and argument from text
            val text = lengthExpr.text
            val funcName = when {
                text.contains("CHAR_LENGTH", ignoreCase = true) -> "CHAR_LENGTH"
                text.contains("CHARACTER_LENGTH", ignoreCase = true) -> "CHARACTER_LENGTH"
                text.contains("BYTE_LENGTH", ignoreCase = true) -> "BYTE_LENGTH"
                text.contains("OCTET_LENGTH", ignoreCase = true) -> "OCTET_LENGTH"
                text.contains("PATH_LENGTH", ignoreCase = true) -> "PATH_LENGTH"
                text.contains("SIZE", ignoreCase = true) -> "SIZE"
                else -> "LENGTH"
            }
            val arg = extractFunctionArgExpression(text)
            return FunctionCall(funcName, listOf(arg))
        }

        // Handle cardinality expression
        if (ctx.cardinalityExpression() != null) {
            val cardExpr = ctx.cardinalityExpression()
            val arg = extractFunctionArgExpression(cardExpr.text)
            return FunctionCall("CARDINALITY", listOf(arg))
        }

        // Handle absolute value
        if (ctx.absoluteValueExpression() != null) {
            val absExpr = ctx.absoluteValueExpression()
            val arg = extractFunctionArgExpression(absExpr.text)
            return FunctionCall("ABS", listOf(arg))
        }

        // Handle floor/ceiling
        if (ctx.floorFunction() != null) {
            val arg = extractFunctionArgExpression(ctx.floorFunction().text)
            return FunctionCall("FLOOR", listOf(arg))
        }
        if (ctx.ceilingFunction() != null) {
            val arg = extractFunctionArgExpression(ctx.ceilingFunction().text)
            return FunctionCall("CEILING", listOf(arg))
        }

        // Fallback: parse from text
        return parseFunctionFromText(ctx.text)
    }

    private fun processCharacterFunction(ctx: GQLParser.CharacterOrByteStringFunctionContext): GqlExpression {
        // Handle UPPER/LOWER (fold character string)
        if (ctx.foldCharacterString() != null) {
            val foldCtx = ctx.foldCharacterString()
            val arg = processValueExpression(foldCtx.valueExpression())
            val funcName = when {
                foldCtx.UPPER() != null -> "UPPER"
                foldCtx.LOWER() != null -> "LOWER"
                else -> throw GqlParseException("Unknown fold function")
            }
            return FunctionCall(funcName, listOf(arg))
        }

        // Handle TRIM functions
        if (ctx.trimMultiCharacterCharacterString() != null) {
            val trimCtx = ctx.trimMultiCharacterCharacterString()
            val arg = processValueExpression(trimCtx.valueExpression(0))
            val funcName = when {
                trimCtx.BTRIM() != null -> "BTRIM"
                trimCtx.LTRIM() != null -> "LTRIM"
                trimCtx.RTRIM() != null -> "RTRIM"
                else -> "TRIM"
            }
            return FunctionCall(funcName, listOf(arg))
        }

        // Handle substring functions (LEFT, RIGHT)
        if (ctx.subCharacterOrByteString() != null) {
            val subCtx = ctx.subCharacterOrByteString()
            val arg = processValueExpression(subCtx.valueExpression())
            val funcName = when {
                subCtx.LEFT() != null -> "LEFT"
                subCtx.RIGHT() != null -> "RIGHT"
                else -> "SUBSTRING"
            }
            return FunctionCall(funcName, listOf(arg))
        }

        // Handle single character trim
        if (ctx.trimSingleCharacterOrByteString() != null) {
            return FunctionCall("TRIM", emptyList())
        }

        // Handle NORMALIZE
        if (ctx.normalizeCharacterString() != null) {
            val normCtx = ctx.normalizeCharacterString()
            val arg = processValueExpression(normCtx.valueExpression())
            return FunctionCall("NORMALIZE", listOf(arg))
        }

        throw GqlParseException("Unsupported character function: ${ctx.text}")
    }

    /**
     * Extract function argument from text like "SIZE(c.name)" -> c.name expression
     */
    private fun extractFunctionArgExpression(text: String): GqlExpression {
        val start = text.indexOf('(')
        val end = text.lastIndexOf(')')
        if (start >= 0 && end > start) {
            val argText = text.substring(start + 1, end).trim()
            return parseSimpleExpression(argText)
        }
        return Literal(null)
    }

    /**
     * Parse a function call from its text representation
     */
    private fun parseFunctionFromText(text: String): GqlExpression {
        val parenIndex = text.indexOf('(')
        if (parenIndex > 0) {
            val funcName = text.substring(0, parenIndex).trim().uppercase()
            val arg = extractFunctionArgExpression(text)
            return FunctionCall(funcName, listOf(arg))
        }
        return FunctionCall(text.uppercase(), emptyList())
    }

    private fun processPredicate(ctx: GQLParser.PredicateContext): GqlExpression {
        // Handle different predicate types using proper contexts
        return when {
            ctx.nullPredicate() != null -> {
                val nullPred = ctx.nullPredicate()
                val value = processValueExpressionPrimary(nullPred.valueExpressionPrimary())
                val isNot = nullPred.nullPredicatePart2().NOT() != null
                IsNullExpr(value, isNot)
            }
            ctx.existsPredicate() != null -> {
                // EXISTS predicate - for subqueries
                Literal(true)
            }
            ctx.valueTypePredicate() != null -> {
                // Value type predicate (IS TYPED, etc.)
                val valueTypePred = ctx.valueTypePredicate()
                val value = processValueExpressionPrimary(valueTypePred.valueExpressionPrimary())
                value
            }
            ctx.directedPredicate() != null -> {
                // Directed predicate
                Literal(true)
            }
            ctx.labeledPredicate() != null -> {
                // Labeled predicate (IS LABELED)
                Literal(true)
            }
            ctx.sourceDestinationPredicate() != null -> {
                // Source/destination predicate
                Literal(true)
            }
            ctx.all_differentPredicate() != null -> {
                // ALL DIFFERENT predicate
                Literal(true)
            }
            ctx.samePredicate() != null -> {
                // SAME predicate
                Literal(true)
            }
            ctx.property_existsPredicate() != null -> {
                // Property exists predicate
                Literal(true)
            }
            else -> throw GqlParseException("Unsupported predicate type: ${ctx.text}")
        }
    }

    private fun processValueExpressionPrimary(ctx: GQLParser.ValueExpressionPrimaryContext): GqlExpression {
        // Handle property access: valueExpressionPrimary PERIOD propertyName (recursive rule)
        if (ctx.propertyName() != null && ctx.valueExpressionPrimary() != null) {
            val base = processValueExpressionPrimary(ctx.valueExpressionPrimary())
            val propName = ctx.propertyName().text
            return PropertyAccess(base, propName)
        }

        // Handle parenthesized expression
        if (ctx.parenthesizedValueExpression() != null) {
            return processValueExpression(ctx.parenthesizedValueExpression().valueExpression())
        }

        // Handle aggregate functions (COUNT, SUM, AVG, etc.)
        if (ctx.aggregateFunction() != null) {
            return processAggregateFunction(ctx.aggregateFunction())
        }

        // Handle literals and value specifications
        if (ctx.unsignedValueSpecification() != null) {
            return processUnsignedValueSpecification(ctx.unsignedValueSpecification())
        }

        // Handle ELEMENT_ID function
        if (ctx.element_idFunction() != null) {
            val elemIdFunc = ctx.element_idFunction()
            val varRef = elemIdFunc.elementVariableReference()
            val varName = varRef.text
            return FunctionCall("ELEMENT_ID", listOf(VariableRef(varName)))
        }

        // Handle binding variable reference (simple variable like 'c', 'n', etc.)
        if (ctx.bindingVariableReference() != null) {
            val varName = ctx.bindingVariableReference().bindingVariable().text
            return VariableRef(varName)
        }

        // Handle case expression (includes COALESCE, NULLIF)
        if (ctx.caseExpression() != null) {
            return processCaseExpression(ctx.caseExpression())
        }

        // Handle let expression
        if (ctx.letValueExpression() != null) {
            // Simplified let handling
            return Literal(null)
        }

        throw GqlParseException("Unsupported value expression primary: ${ctx.text}")
    }

    private fun processCaseExpression(ctx: GQLParser.CaseExpressionContext): GqlExpression {
        // Handle case abbreviations (COALESCE, NULLIF)
        val caseAbbrev = ctx.caseAbbreviation()
        if (caseAbbrev != null) {
            // COALESCE(expr, expr, ...)
            if (caseAbbrev.COALESCE() != null) {
                val args = caseAbbrev.valueExpression().map { processValueExpression(it) }
                return FunctionCall("COALESCE", args)
            }
            // NULLIF(expr, expr)
            if (caseAbbrev.NULLIF() != null) {
                val args = caseAbbrev.valueExpression().map { processValueExpression(it) }
                return FunctionCall("NULLIF", args)
            }
        }

        // Handle CASE ... WHEN ... THEN ... ELSE ... END
        val caseSpec = ctx.caseSpecification()
        if (caseSpec != null) {
            // For now, return a simplified CaseExpr
            // Full implementation would parse whenClauses and elseClause
            return Literal(null)
        }

        throw GqlParseException("Unsupported case expression: ${ctx.text}")
    }

    private fun processAggregateFunction(ctx: GQLParser.AggregateFunctionContext): GqlExpression {
        // Parse aggregate function from text
        val text = ctx.text.uppercase()
        return when {
            text.startsWith("COUNT(*)") -> FunctionCall("COUNT", listOf(Literal("*")))
            text.startsWith("COUNT(") -> FunctionCall("COUNT", listOf(Literal("*")))
            text.startsWith("SUM(") -> {
                val inner = extractFunctionArg(ctx.text)
                FunctionCall("SUM", listOf(parseSimpleExpression(inner)))
            }

            text.startsWith("AVG(") -> {
                val inner = extractFunctionArg(ctx.text)
                FunctionCall("AVG", listOf(parseSimpleExpression(inner)))
            }

            text.startsWith("MIN(") -> {
                val inner = extractFunctionArg(ctx.text)
                FunctionCall("MIN", listOf(parseSimpleExpression(inner)))
            }

            text.startsWith("MAX(") -> {
                val inner = extractFunctionArg(ctx.text)
                FunctionCall("MAX", listOf(parseSimpleExpression(inner)))
            }

            else -> FunctionCall("COUNT", listOf(Literal("*")))
        }
    }

    private fun extractFunctionArg(funcCall: String): String {
        val start = funcCall.indexOf('(')
        val end = funcCall.lastIndexOf(')')
        return if (start >= 0 && end > start) funcCall.substring(start + 1, end) else ""
    }

    private fun parseSimpleExpression(text: String): GqlExpression {
        val trimmed = text.trim()
        if (trimmed.contains(".")) {
            val parts = trimmed.split(".")
            var base: GqlExpression = VariableRef(parts[0])
            for (i in 1 until parts.size) {
                base = PropertyAccess(base, parts[i])
            }
            return base
        }
        return VariableRef(trimmed)
    }

    private fun processUnsignedValueSpecification(ctx: GQLParser.UnsignedValueSpecificationContext): GqlExpression {
        // Try to get child contexts
        for (i in 0 until ctx.childCount) {
            val child = ctx.getChild(i)
            if (child is GQLParser.UnsignedLiteralContext) {
                return processUnsignedLiteral(child)
            }
        }
        // Fallback to text parsing
        return parseLiteralFromText(ctx.text)
    }

    private fun processUnsignedLiteral(ctx: GQLParser.UnsignedLiteralContext): GqlExpression {
        // Try to get child contexts
        for (i in 0 until ctx.childCount) {
            val child = ctx.getChild(i)
            if (child is GQLParser.UnsignedNumericLiteralContext) {
                return processUnsignedNumericLiteral(child)
            }
            if (child is GQLParser.GeneralLiteralContext) {
                return processGeneralLiteral(child)
            }
        }
        // Fallback to text parsing
        return parseLiteralFromText(ctx.text)
    }

    private fun processUnsignedNumericLiteral(ctx: GQLParser.UnsignedNumericLiteralContext): GqlExpression {
        return parseLiteralFromText(ctx.text)
    }

    private fun processGeneralLiteral(ctx: GQLParser.GeneralLiteralContext): GqlExpression {
        return parseLiteralFromText(ctx.text)
    }

    private fun parseLiteralFromText(text: String): GqlExpression {
        val trimmed = text.trim().removeSuffix("m").removeSuffix("M").removeSuffix("f").removeSuffix("F")
        return when {
            trimmed.equals("true", ignoreCase = true) -> Literal(true)
            trimmed.equals("false", ignoreCase = true) -> Literal(false)
            trimmed.equals("null", ignoreCase = true) -> Literal.NULL
            trimmed.startsWith("'") -> Literal(unescapeString(trimmed.removeSurrounding("'")))
            trimmed.startsWith("\"") -> Literal(unescapeString(trimmed.removeSurrounding("\"")))
            trimmed.startsWith("[") -> ListExpr(emptyList())
            trimmed.contains(".") || trimmed.lowercase().contains("e") -> {
                try {
                    Literal(trimmed.toDouble())
                } catch (e: NumberFormatException) {
                    Literal(0.0)
                }
            }

            else -> {
                try {
                    Literal(trimmed.toLong())
                } catch (e: NumberFormatException) {
                    Literal(trimmed)
                }
            }
        }
    }

    private fun unescapeString(s: String): String {
        val sb = StringBuilder()
        var i = 0
        while (i < s.length) {
            if (s[i] == '\\' && i + 1 < s.length) {
                i++
                sb.append(
                    when (s[i]) {
                        'n' -> '\n'
                        't' -> '\t'
                        'r' -> '\r'
                        'b' -> '\b'
                        'f' -> '\u000C'
                        '\'' -> '\''
                        '"' -> '"'
                        '\\' -> '\\'
                        else -> s[i]
                    }
                )
            } else {
                sb.append(s[i])
            }
            i++
        }
        return sb.toString()
    }
}
