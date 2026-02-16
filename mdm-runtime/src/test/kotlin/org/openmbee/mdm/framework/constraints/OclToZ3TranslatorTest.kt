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
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.openmbee.mdm.framework.query.ocl.OclToZ3Translator

class OclToZ3TranslatorTest : DescribeSpec({

    lateinit var ctx: Context

    beforeEach {
        ctx = Context()
    }

    afterEach {
        ctx.close()
    }

    describe("literal translation") {

        it("should translate integer literals") {
            val expr = OclToZ3Translator.translate("42", ctx, emptyMap())
            expr.shouldBeInstanceOf<IntNum>()
            (expr as IntNum).int shouldBe 42
        }

        it("should translate boolean literals") {
            val trueExpr = OclToZ3Translator.translate("true", ctx, emptyMap())
            trueExpr.shouldBeInstanceOf<BoolExpr>()

            val falseExpr = OclToZ3Translator.translate("false", ctx, emptyMap())
            falseExpr.shouldBeInstanceOf<BoolExpr>()
        }
    }

    describe("variable translation") {

        it("should translate variables to Z3 constants") {
            val x = ctx.mkIntConst("x")
            val variables = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x", ctx, variables)
            expr shouldBe x
        }

        it("should throw for undeclared variables") {
            shouldThrow<IllegalArgumentException> {
                OclToZ3Translator.translate("y", ctx, emptyMap())
            }
        }
    }

    describe("arithmetic translation") {

        it("should translate addition") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x + 1", ctx, vars)
            expr.shouldBeInstanceOf<ArithExpr<*>>()
        }

        it("should translate subtraction") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x - 1", ctx, vars)
            expr.shouldBeInstanceOf<ArithExpr<*>>()
        }

        it("should translate multiplication") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x * 2", ctx, vars)
            expr.shouldBeInstanceOf<ArithExpr<*>>()
        }

        it("should translate unary minus") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("-x", ctx, vars)
            expr.shouldBeInstanceOf<ArithExpr<*>>()
        }
    }

    describe("comparison translation") {

        it("should translate less than") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x < 10", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }

        it("should translate equality") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x = 5", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }

        it("should translate inequality") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("x <> 5", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }
    }

    describe("boolean translation") {

        it("should translate and") {
            val x = ctx.mkBoolConst("x")
            val y = ctx.mkBoolConst("y")
            val vars = mapOf("x" to x as Expr<*>, "y" to y as Expr<*>)

            val expr = OclToZ3Translator.translate("x and y", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }

        it("should translate or") {
            val x = ctx.mkBoolConst("x")
            val y = ctx.mkBoolConst("y")
            val vars = mapOf("x" to x as Expr<*>, "y" to y as Expr<*>)

            val expr = OclToZ3Translator.translate("x or y", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }

        it("should translate not") {
            val x = ctx.mkBoolConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("not x", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }

        it("should translate implies") {
            val x = ctx.mkBoolConst("x")
            val y = ctx.mkBoolConst("y")
            val vars = mapOf("x" to x as Expr<*>, "y" to y as Expr<*>)

            val expr = OclToZ3Translator.translate("x implies y", ctx, vars)
            expr.shouldBeInstanceOf<BoolExpr>()
        }
    }

    describe("if-then-else translation") {

        it("should translate if-then-else") {
            val x = ctx.mkIntConst("x")
            val b = ctx.mkBoolConst("b")
            val vars = mapOf("x" to x as Expr<*>, "b" to b as Expr<*>)

            val expr = OclToZ3Translator.translate("if b then x else 0 endif", ctx, vars)
            expr.shouldBeInstanceOf<Expr<*>>()
        }
    }

    describe("let expression translation") {

        it("should translate let expressions") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            val expr = OclToZ3Translator.translate("let y : Integer = x + 1 in y * 2", ctx, vars)
            expr.shouldBeInstanceOf<ArithExpr<*>>()
        }
    }

    describe("unsupported operations") {

        it("should throw for string literals") {
            shouldThrow<UnsupportedOperationException> {
                OclToZ3Translator.translate("'hello'", ctx, emptyMap())
            }
        }

        it("should throw for property access") {
            val x = ctx.mkIntConst("x")
            val vars = mapOf("x" to x as Expr<*>)

            shouldThrow<UnsupportedOperationException> {
                OclToZ3Translator.translate("x.size()", ctx, vars)
            }
        }
    }
})
