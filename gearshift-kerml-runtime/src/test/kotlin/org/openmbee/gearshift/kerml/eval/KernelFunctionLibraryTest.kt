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
package org.openmbee.gearshift.kerml.eval

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class KernelFunctionLibraryTest : DescribeSpec({

    lateinit var engine: MDMEngine
    lateinit var library: KernelFunctionLibrary

    beforeEach {
        val registry = MetamodelRegistry()
        registry.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(registry)
        engine = MDMEngine(registry)
        library = KernelFunctionLibrary(engine)
    }

    describe("arithmetic operations") {

        it("should add two integers") {
            val result = library.apply("+", listOf(3L, 5L))
            result.shouldNotBeNull()
            result.shouldBeInstanceOf<FunctionResult.LiteralElement>()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralInteger"
            engine.getPropertyValue(literal, "value") shouldBe 8L
        }

        it("should add integer and rational") {
            val result = library.apply("+", listOf(3L, 2.5))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralRational"
            engine.getPropertyValue(literal, "value") shouldBe 5.5
        }

        it("should subtract two integers") {
            val result = library.apply("-", listOf(10L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralInteger"
            engine.getPropertyValue(literal, "value") shouldBe 7L
        }

        it("should handle unary minus") {
            val result = library.apply("-", listOf(5L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe -5L
        }

        it("should multiply two integers") {
            val result = library.apply("*", listOf(4L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe 12L
        }

        it("should divide evenly") {
            val result = library.apply("/", listOf(10L, 2L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralInteger"
            engine.getPropertyValue(literal, "value") shouldBe 5L
        }

        it("should divide with remainder producing rational") {
            val result = library.apply("/", listOf(7L, 2L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralRational"
            engine.getPropertyValue(literal, "value") shouldBe 3.5
        }

        it("should return null for division by zero") {
            val result = library.apply("/", listOf(5L, 0L))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value.shouldBeNull()
        }

        it("should compute modulo") {
            val result = library.apply("%", listOf(10L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe 1L
        }

        it("should compute power") {
            val result = library.apply("**", listOf(2L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralInteger"
            engine.getPropertyValue(literal, "value") shouldBe 8L
        }
    }

    describe("comparison operations") {

        it("should compare less than") {
            val result = library.apply("<", listOf(3L, 5L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralBoolean"
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compare greater than") {
            val result = library.apply(">", listOf(5L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compare less equal") {
            val result = library.apply("<=", listOf(5L, 5L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compare greater equal") {
            val result = library.apply(">=", listOf(3L, 5L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }
    }

    describe("equality operations") {

        it("should test equal integers") {
            val result = library.apply("=", listOf(5L, 5L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should test unequal integers") {
            val result = library.apply("=", listOf(5L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }

        it("should test not-equal") {
            val result = library.apply("<>", listOf(5L, 3L))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compare across numeric types") {
            val result = library.apply("=", listOf(5L, 5.0))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compare strings") {
            val result = library.apply("=", listOf("hello", "hello"))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }
    }

    describe("boolean operations") {

        it("should compute and") {
            val result = library.apply("and", listOf(true, false))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }

        it("should short-circuit and on false") {
            val result = library.apply("and", listOf(false, null))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }

        it("should compute or") {
            val result = library.apply("or", listOf(false, true))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should short-circuit or on true") {
            val result = library.apply("or", listOf(true, null))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compute not") {
            val result = library.apply("not", listOf(true))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }

        it("should compute xor") {
            val result = library.apply("xor", listOf(true, false))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }

        it("should compute implies") {
            val result = library.apply("implies", listOf(true, false))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe false
        }

        it("should short-circuit implies on false antecedent") {
            val result = library.apply("implies", listOf(false, null))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe true
        }
    }

    describe("string operations") {

        it("should concatenate strings with +") {
            val result = library.apply("+", listOf("hello", " world"))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralString"
            engine.getPropertyValue(literal, "value") shouldBe "hello world"
        }

        it("should compute string size") {
            val result = library.apply("Size", listOf("hello"))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            engine.getPropertyValue(literal, "value") shouldBe 5L
        }
    }

    describe("control functions") {

        it("should return then value for if with true condition") {
            val result = library.apply("if", listOf(true, "yes", "no"))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value shouldBe "yes"
        }

        it("should return else value for if with false condition") {
            val result = library.apply("if", listOf(false, "yes", "no"))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value shouldBe "no"
        }

        it("should coalesce null with ??") {
            val result = library.apply("??", listOf(null, "default"))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value shouldBe "default"
        }

        it("should return value when not null with ??") {
            val result = library.apply("??", listOf("present", "default"))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value shouldBe "present"
        }

        it("should index into a list with #") {
            val result = library.apply("#", listOf(listOf("a", "b", "c"), 2L))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value shouldBe "b"
        }

        it("should return null for out-of-bounds index") {
            val result = library.apply("#", listOf(listOf("a", "b"), 5L))
            result.shouldNotBeNull()
            (result as FunctionResult.Value).value.shouldBeNull()
        }
    }

    describe("MDMObject literal extraction") {

        it("should extract value from LiteralInteger MDMObject") {
            val literal = library.createLiteralInteger(42)
            library.extractNumber(literal) shouldBe 42L
        }

        it("should extract value from LiteralBoolean MDMObject") {
            val literal = library.createLiteralBoolean(true)
            library.extractBoolean(literal) shouldBe true
        }

        it("should extract value from LiteralString MDMObject") {
            val literal = library.createLiteralString("hello")
            library.extractString(literal) shouldBe "hello"
        }

        it("should apply operator to LiteralInteger MDMObjects") {
            val a = library.createLiteralInteger(10)
            val b = library.createLiteralInteger(3)
            val result = library.apply("+", listOf(a, b))
            result.shouldNotBeNull()
            val literal = (result as FunctionResult.LiteralElement).element
            literal.className shouldBe "LiteralInteger"
            engine.getPropertyValue(literal, "value") shouldBe 13L
        }
    }

    describe("unknown operators") {

        it("should return null for unknown operators") {
            library.apply("unknown_op", listOf(1, 2)).shouldBeNull()
        }

        it("should report unknown operators via hasOperator") {
            library.hasOperator("+") shouldBe true
            library.hasOperator("unknown_op") shouldBe false
        }
    }
})
