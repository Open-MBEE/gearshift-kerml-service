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
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.eval.FunctionRegistry
import org.openmbee.gearshift.kerml.eval.FunctionResult
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class RealFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        RealFunctionsLibrary().register(registry)
    }

    describe("sqrt") {
        it("should compute square root") {
            val result = registry.apply("sqrt", listOf(9.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 3.0
        }

        it("should return null for negative input") {
            val result = registry.apply("sqrt", listOf(-1.0))!!
            result.shouldBe(FunctionResult.Value(null))
        }
    }

    describe("floor") {
        it("should floor a rational") {
            val result = registry.apply("floor", listOf(3.7))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3L
        }

        it("should floor a negative rational") {
            val result = registry.apply("floor", listOf(-2.3))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe -3L
        }
    }

    describe("round") {
        it("should round up") {
            val result = registry.apply("round", listOf(3.7))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 4L
        }

        it("should round down") {
            val result = registry.apply("round", listOf(3.2))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3L
        }
    }

    describe("ToReal") {
        it("should convert integer to real") {
            val result = registry.apply("ToReal", listOf(42L))!!
            val element = (result as FunctionResult.LiteralElement).element
            element.className shouldBe "LiteralRational"
            registry.extractNumber(element) shouldBe 42.0
        }

        it("should parse string to real") {
            val result = registry.apply("ToReal", listOf("3.14"))!!
            val n = registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble()
            n shouldBeGreaterThan 3.13
            n shouldBeLessThan 3.15
        }
    }
})
