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
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.eval.FunctionRegistry
import org.openmbee.gearshift.kerml.eval.FunctionResult
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class NumericalFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        NumericalFunctionsLibrary().register(registry)
    }

    describe("abs") {
        it("should return absolute value of negative integer") {
            val result = registry.apply("abs", listOf(-5L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 5L
        }

        it("should return absolute value of positive integer") {
            val result = registry.apply("abs", listOf(5L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 5L
        }

        it("should return absolute value of negative rational") {
            val result = registry.apply("abs", listOf(-3.14))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3.14
        }
    }

    describe("isZero") {
        it("should return true for zero") {
            val result = registry.apply("isZero", listOf(0L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false for non-zero") {
            val result = registry.apply("isZero", listOf(1L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }

    describe("isUnit") {
        it("should return true for 1") {
            val result = registry.apply("isUnit", listOf(1L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return true for 1.0") {
            val result = registry.apply("isUnit", listOf(1.0))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("sum") {
        it("should sum a list of integers") {
            val result = registry.apply("sum", listOf(listOf(
                registry.createLiteralInteger(10),
                registry.createLiteralInteger(20),
                registry.createLiteralInteger(30)
            )))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 60L
        }

        it("should return 0 for empty sum") {
            val result = registry.apply("sum", listOf(emptyList<Any>()))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 0L
        }
    }

    describe("product") {
        it("should multiply a list of integers") {
            val result = registry.apply("product", listOf(listOf(
                registry.createLiteralInteger(2),
                registry.createLiteralInteger(3),
                registry.createLiteralInteger(5)
            )))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 30L
        }
    }
})
