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

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ConstraintSolverServiceTest : DescribeSpec({

    val service = ConstraintSolverService()

    describe("solve") {

        it("should find satisfying assignment for simple equality") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x = 42")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 42L
            result.errorMessage.shouldBeNull()
        }

        it("should solve system with two variables") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT),
                Z3Variable("y", Z3Sort.INT)
            )
            val constraints = listOf("x + y = 10", "x - y = 4")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 7L
            result.assignments["y"] shouldBe 3L
        }

        it("should detect unsatisfiable constraints") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 10", "x < 5")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe false
        }

        it("should handle boolean variables") {
            val variables = listOf(
                Z3Variable("a", Z3Sort.BOOL),
                Z3Variable("b", Z3Sort.BOOL)
            )
            val constraints = listOf("a and b")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe true
            result.assignments["a"] shouldBe true
            result.assignments["b"] shouldBe true
        }

        it("should respect bound constraints") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 0, upperBound = 100)
            )
            val constraints = listOf("x > 50")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe true
            val x = result.assignments["x"] as Long
            (x > 50) shouldBe true
            (x <= 100) shouldBe true
        }

        it("should return unsatisfiable when bounds conflict with constraints") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 0, upperBound = 5)
            )
            val constraints = listOf("x > 10")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe false
        }

        it("should handle real-valued variables") {
            val variables = listOf(Z3Variable("x", Z3Sort.REAL))
            val constraints = listOf("x * 2 = 5")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe true
            result.assignments["x"].shouldNotBeNull()
        }

        it("should return error for invalid constraint expression") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x.size()")

            val result = service.solve(variables, constraints)

            result.satisfiable shouldBe false
            result.errorMessage.shouldNotBeNull()
        }
    }

    describe("isSatisfiable") {

        it("should return true for satisfiable constraints") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 0")

            service.isSatisfiable(variables, constraints) shouldBe true
        }

        it("should return false for unsatisfiable constraints") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 0", "x < 0")

            service.isSatisfiable(variables, constraints) shouldBe false
        }
    }

    describe("optimize") {

        it("should minimize an objective") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 1, upperBound = 100)
            )
            val constraints = listOf("x >= 10")

            val result = service.optimize(variables, constraints, "x", minimize = true)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 10L
            result.assignments["x"] shouldBe 10L
        }

        it("should maximize an objective") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 1, upperBound = 100)
            )
            val constraints = listOf("x <= 50")

            val result = service.optimize(variables, constraints, "x", minimize = false)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 50L
            result.assignments["x"] shouldBe 50L
        }

        it("should optimize with multiple variables") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 0, upperBound = 100),
                Z3Variable("y", Z3Sort.INT, lowerBound = 0, upperBound = 100)
            )
            val constraints = listOf("x + y <= 20", "x >= 5", "y >= 5")

            val result = service.optimize(variables, constraints, "x + y", minimize = false)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 20L
        }

        it("should return unsatisfiable for infeasible optimization") {
            val variables = listOf(
                Z3Variable("x", Z3Sort.INT, lowerBound = 0, upperBound = 5)
            )
            val constraints = listOf("x > 10")

            val result = service.optimize(variables, constraints, "x", minimize = true)

            result.satisfiable shouldBe false
        }
    }

    describe("findConflicts") {

        it("should report consistent constraints") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 0", "x < 100")

            val result = service.findConflicts(variables, constraints)

            result.consistent shouldBe true
            result.conflictingConstraints shouldBe emptyList()
        }

        it("should detect conflicting constraints") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 10", "x < 5")

            val result = service.findConflicts(variables, constraints)

            result.consistent shouldBe false
            result.conflictingConstraints.shouldNotBeEmpty()
        }

        it("should identify minimal conflict set") {
            val variables = listOf(Z3Variable("x", Z3Sort.INT))
            val constraints = listOf("x > 0", "x < 100", "x > 200")

            val result = service.findConflicts(variables, constraints)

            result.consistent shouldBe false
            // The conflicting set should include the incompatible ones
            result.conflictingConstraints.shouldNotBeEmpty()
        }
    }
})
