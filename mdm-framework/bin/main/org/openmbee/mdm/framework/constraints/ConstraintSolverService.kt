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
package org.openmbee.mdm.framework.constraints

import com.microsoft.z3.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.query.ocl.OclParser
import org.openmbee.mdm.framework.query.ocl.OclToZ3Translator

private val logger = KotlinLogging.logger {}

/**
 * The sort (type) of a Z3 variable.
 */
enum class Z3Sort {
    INT, REAL, BOOL
}

/**
 * Declaration of a Z3 variable with its name and sort.
 */
data class Z3Variable(
    val name: String,
    val sort: Z3Sort,
    val lowerBound: Number? = null,
    val upperBound: Number? = null
)

/**
 * Result of a constraint solving operation.
 */
data class SolverResult(
    /** Whether the constraints are satisfiable. */
    val satisfiable: Boolean,

    /** Variable assignments that satisfy the constraints (if satisfiable). */
    val assignments: Map<String, Any?> = emptyMap(),

    /** Error message if solving failed. */
    val errorMessage: String? = null
)

/**
 * Result of an optimization operation.
 */
data class OptimizationResult(
    /** Whether a solution was found. */
    val satisfiable: Boolean,

    /** Variable assignments at the optimal point. */
    val assignments: Map<String, Any?> = emptyMap(),

    /** The optimal value of the objective function. */
    val objectiveValue: Any? = null,

    /** Error message if optimization failed. */
    val errorMessage: String? = null
)

/**
 * Result of a conflict detection operation.
 */
data class ConflictResult(
    /** Whether the constraints are consistent (satisfiable). */
    val consistent: Boolean,

    /** Minimal set of conflicting constraint names (if inconsistent). */
    val conflictingConstraints: List<String> = emptyList(),

    /** Error message if detection failed. */
    val errorMessage: String? = null
)

/**
 * Service for solving constraint systems using the Z3 SMT solver.
 *
 * Parses OCL constraint expressions, translates them to Z3 formulas,
 * and uses Z3 to find satisfying assignments, optimize objectives,
 * or detect conflicts.
 *
 * Thread safety: Creates a new Z3 Context per call and disposes it
 * after use, so this service is thread-safe.
 */
class ConstraintSolverService {

    /**
     * Solve a constraint system: find variable assignments that satisfy all constraints.
     *
     * @param variables Variable declarations with sorts
     * @param constraints OCL constraint expressions
     * @return Solver result with satisfiability and assignments
     */
    fun solve(variables: List<Z3Variable>, constraints: List<String>): SolverResult {
        val ctx = Context()
        try {
            val z3Vars = declareVariables(ctx, variables)
            val translator = OclToZ3Translator(ctx, z3Vars)
            val solver = ctx.mkSolver()

            // Add bound constraints
            addBoundConstraints(ctx, solver, variables, z3Vars)

            // Translate and add each OCL constraint
            for (constraint in constraints) {
                try {
                    val ast = OclParser.parse(constraint)
                    val z3Expr = ast.accept(translator) as? BoolExpr
                        ?: throw IllegalArgumentException("Constraint must be a boolean expression: $constraint")
                    solver.add(z3Expr)
                } catch (e: Exception) {
                    logger.error(e) { "Failed to translate constraint: $constraint" }
                    return SolverResult(
                        satisfiable = false,
                        errorMessage = "Translation error for '$constraint': ${e.message}"
                    )
                }
            }

            // Solve
            val status = solver.check()
            return when (status) {
                Status.SATISFIABLE -> {
                    val model = solver.model
                    val assignments = extractAssignments(model, z3Vars, variables)
                    SolverResult(satisfiable = true, assignments = assignments)
                }
                Status.UNSATISFIABLE -> SolverResult(satisfiable = false)
                Status.UNKNOWN -> SolverResult(
                    satisfiable = false,
                    errorMessage = "Solver returned UNKNOWN: ${solver.reasonUnknown}"
                )
                else -> SolverResult(
                    satisfiable = false,
                    errorMessage = "Unexpected solver status: $status"
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Solver error" }
            return SolverResult(satisfiable = false, errorMessage = e.message)
        } finally {
            ctx.close()
        }
    }

    /**
     * Check if a constraint system is satisfiable (without computing assignments).
     */
    fun isSatisfiable(variables: List<Z3Variable>, constraints: List<String>): Boolean {
        return solve(variables, constraints).satisfiable
    }

    /**
     * Optimize an objective function subject to constraints.
     *
     * @param variables Variable declarations
     * @param constraints OCL constraint expressions
     * @param objective OCL expression for the objective function
     * @param minimize true to minimize, false to maximize
     * @return Optimization result
     */
    fun optimize(
        variables: List<Z3Variable>,
        constraints: List<String>,
        objective: String,
        minimize: Boolean = true
    ): OptimizationResult {
        val ctx = Context()
        try {
            val z3Vars = declareVariables(ctx, variables)
            val translator = OclToZ3Translator(ctx, z3Vars)
            val optimizer = ctx.mkOptimize()

            // Add bound constraints
            addBoundConstraintsToOptimize(ctx, optimizer, variables, z3Vars)

            // Add constraints
            for (constraint in constraints) {
                try {
                    val ast = OclParser.parse(constraint)
                    val z3Expr = ast.accept(translator) as? BoolExpr
                        ?: throw IllegalArgumentException("Constraint must be boolean: $constraint")
                    optimizer.Add(z3Expr)
                } catch (e: Exception) {
                    return OptimizationResult(
                        satisfiable = false,
                        errorMessage = "Translation error: ${e.message}"
                    )
                }
            }

            // Add objective
            val objectiveAst = OclParser.parse(objective)
            val objectiveExpr = objectiveAst.accept(translator)

            @Suppress("UNCHECKED_CAST")
            val arithObjective = objectiveExpr as? ArithExpr<out ArithSort>
                ?: throw IllegalArgumentException("Objective must be an arithmetic expression")

            if (minimize) {
                optimizer.MkMinimize(arithObjective)
            } else {
                optimizer.MkMaximize(arithObjective)
            }

            val status = optimizer.Check()
            return when (status) {
                Status.SATISFIABLE -> {
                    val model = optimizer.model
                    val assignments = extractAssignments(model, z3Vars, variables)
                    val objValue = model.evaluate(objectiveExpr, true)
                    OptimizationResult(
                        satisfiable = true,
                        assignments = assignments,
                        objectiveValue = extractValue(objValue)
                    )
                }
                else -> OptimizationResult(
                    satisfiable = false,
                    errorMessage = "Optimization returned: $status"
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Optimization error" }
            return OptimizationResult(satisfiable = false, errorMessage = e.message)
        } finally {
            ctx.close()
        }
    }

    /**
     * Find conflicting constraints using unsat core extraction.
     *
     * @param variables Variable declarations
     * @param constraints Named OCL constraints (constraint text)
     * @return Conflict result with minimal conflicting constraint indices
     */
    fun findConflicts(
        variables: List<Z3Variable>,
        constraints: List<String>
    ): ConflictResult {
        val ctx = Context()
        try {
            val z3Vars = declareVariables(ctx, variables)
            val translator = OclToZ3Translator(ctx, z3Vars)
            val solver = ctx.mkSolver()

            // Add bound constraints (not tracked for conflicts)
            addBoundConstraints(ctx, solver, variables, z3Vars)

            // Track each constraint with a boolean indicator for unsat core
            val trackingVars = mutableMapOf<BoolExpr, String>()

            for ((index, constraint) in constraints.withIndex()) {
                try {
                    val ast = OclParser.parse(constraint)
                    val z3Expr = ast.accept(translator) as? BoolExpr
                        ?: throw IllegalArgumentException("Constraint must be boolean: $constraint")

                    val trackVar = ctx.mkBoolConst("__track_$index")
                    trackingVars[trackVar] = constraint
                    solver.assertAndTrack(z3Expr, trackVar)
                } catch (e: Exception) {
                    return ConflictResult(
                        consistent = false,
                        errorMessage = "Translation error: ${e.message}"
                    )
                }
            }

            val status = solver.check()
            return when (status) {
                Status.SATISFIABLE -> ConflictResult(consistent = true)
                Status.UNSATISFIABLE -> {
                    val unsatCore = solver.unsatCore
                    val conflicting = unsatCore.mapNotNull { trackingVars[it] }
                    ConflictResult(consistent = false, conflictingConstraints = conflicting)
                }
                else -> ConflictResult(
                    consistent = false,
                    errorMessage = "Solver returned: $status"
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Conflict detection error" }
            return ConflictResult(consistent = false, errorMessage = e.message)
        } finally {
            ctx.close()
        }
    }

    // === Private Helpers ===

    private fun declareVariables(ctx: Context, variables: List<Z3Variable>): Map<String, Expr<*>> {
        return variables.associate { variable ->
            val expr: Expr<*> = when (variable.sort) {
                Z3Sort.INT -> ctx.mkIntConst(variable.name)
                Z3Sort.REAL -> ctx.mkRealConst(variable.name)
                Z3Sort.BOOL -> ctx.mkBoolConst(variable.name)
            }
            variable.name to expr
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addBoundConstraints(
        ctx: Context,
        solver: Solver,
        variables: List<Z3Variable>,
        z3Vars: Map<String, Expr<*>>
    ) {
        for (variable in variables) {
            val z3Var = z3Vars[variable.name] ?: continue
            if (z3Var !is ArithExpr<*>) continue

            val arithVar = z3Var as ArithExpr<out ArithSort>
            variable.lowerBound?.let { lb ->
                solver.add(ctx.mkGe(arithVar, toArithConst(ctx, lb, variable.sort)))
            }
            variable.upperBound?.let { ub ->
                solver.add(ctx.mkLe(arithVar, toArithConst(ctx, ub, variable.sort)))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addBoundConstraintsToOptimize(
        ctx: Context,
        optimizer: Optimize,
        variables: List<Z3Variable>,
        z3Vars: Map<String, Expr<*>>
    ) {
        for (variable in variables) {
            val z3Var = z3Vars[variable.name] ?: continue
            if (z3Var !is ArithExpr<*>) continue

            val arithVar = z3Var as ArithExpr<out ArithSort>
            variable.lowerBound?.let { lb ->
                optimizer.Add(ctx.mkGe(arithVar, toArithConst(ctx, lb, variable.sort)))
            }
            variable.upperBound?.let { ub ->
                optimizer.Add(ctx.mkLe(arithVar, toArithConst(ctx, ub, variable.sort)))
            }
        }
    }

    private fun toArithConst(ctx: Context, value: Number, sort: Z3Sort): ArithExpr<out ArithSort> {
        return when (sort) {
            Z3Sort.INT -> ctx.mkInt(value.toLong())
            Z3Sort.REAL -> ctx.mkReal(value.toString())
            else -> ctx.mkInt(value.toLong())
        }
    }

    private fun extractAssignments(
        model: Model,
        z3Vars: Map<String, Expr<*>>,
        variables: List<Z3Variable>
    ): Map<String, Any?> {
        val assignments = mutableMapOf<String, Any?>()
        for (variable in variables) {
            val z3Var = z3Vars[variable.name] ?: continue
            val value = model.evaluate(z3Var, true)
            assignments[variable.name] = extractValue(value)
        }
        return assignments
    }

    private fun extractValue(expr: Expr<*>): Any? {
        return when (expr) {
            is IntNum -> expr.int64
            is RatNum -> {
                val num = expr.numerator.int64
                val den = expr.denominator.int64
                if (den == 1L) num else num.toDouble() / den.toDouble()
            }
            is BoolExpr -> expr.isTrue
            else -> expr.toString()
        }
    }
}
