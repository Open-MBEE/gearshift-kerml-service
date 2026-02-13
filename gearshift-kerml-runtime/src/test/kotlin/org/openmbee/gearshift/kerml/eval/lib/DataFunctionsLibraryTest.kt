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
package org.openmbee.gearshift.kerml.eval.lib

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.eval.FunctionRegistry
import org.openmbee.gearshift.kerml.eval.FunctionResult
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class DataFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        DataFunctionsLibrary().register(registry)
    }

    describe("arithmetic") {
        it("should add integers") {
            val result = registry.apply("+", listOf(3L, 5L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 8L
        }

        it("should concatenate strings via +") {
            val result = registry.apply("+", listOf("hello", " world"))!!
            registry.extractString((result as FunctionResult.LiteralElement).element) shouldBe "hello world"
        }

        it("should subtract") {
            val result = registry.apply("-", listOf(10L, 3L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 7L
        }

        it("should handle unary minus") {
            val result = registry.apply("-", listOf(5L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe -5L
        }

        it("should multiply") {
            val result = registry.apply("*", listOf(4L, 3L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 12L
        }

        it("should divide evenly") {
            val result = registry.apply("/", listOf(10L, 2L))!!
            val element = (result as FunctionResult.LiteralElement).element
            element.className shouldBe "LiteralInteger"
            registry.extractNumber(element) shouldBe 5L
        }

        it("should divide with remainder") {
            val result = registry.apply("/", listOf(7L, 2L))!!
            val element = (result as FunctionResult.LiteralElement).element
            element.className shouldBe "LiteralRational"
            registry.extractNumber(element) shouldBe 3.5
        }

        it("should return null for division by zero") {
            val result = registry.apply("/", listOf(5L, 0L))!!
            (result as FunctionResult.Value).value.shouldBeNull()
        }

        it("should compute modulo") {
            val result = registry.apply("%", listOf(10L, 3L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 1L
        }

        it("should compute power") {
            val result = registry.apply("**", listOf(2L, 3L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 8L
        }
    }

    describe("comparison") {
        it("should compare less than") {
            val result = registry.apply("<", listOf(3L, 5L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should compare greater than") {
            val result = registry.apply(">", listOf(5L, 3L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("max/min") {
        it("should return max of two numbers") {
            val result = registry.apply("max", listOf(3L, 7L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 7L
        }

        it("should return min of two numbers") {
            val result = registry.apply("min", listOf(3L, 7L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3L
        }
    }

    describe("range") {
        it("should create a range of integers") {
            val result = registry.apply("..", listOf(1L, 3L))!!
            val list = (result as FunctionResult.Value).value
            list.shouldBeInstanceOf<List<MDMObject>>()
            list.size shouldBe 3
        }
    }

    describe("bitwise") {
        it("should compute bitwise XOR") {
            val result = registry.apply("^", listOf(5L, 3L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 6L
        }

        it("should compute bitwise complement") {
            val result = registry.apply("~", listOf(0L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe -1L
        }
    }
})
