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
import kotlin.math.PI

class TrigFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        TrigFunctionsLibrary().register(registry)
    }

    describe("sin/cos/tan") {
        it("should compute sin(0) = 0") {
            val result = registry.apply("sin", listOf(0.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 0.0
        }

        it("should compute cos(0) = 1") {
            val result = registry.apply("cos", listOf(0.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 1.0
        }

        it("should compute tan(0) = 0") {
            val result = registry.apply("tan", listOf(0.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 0.0
        }
    }

    describe("arcsin/arccos/arctan") {
        it("should compute arcsin(0) = 0") {
            val result = registry.apply("arcsin", listOf(0.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 0.0
        }

        it("should compute arccos(1) = 0") {
            val result = registry.apply("arccos", listOf(1.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 0.0
        }

        it("should compute arctan(0) = 0") {
            val result = registry.apply("arctan", listOf(0.0))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble() shouldBe 0.0
        }
    }

    describe("deg/rad conversion") {
        it("should convert radians to degrees") {
            val result = registry.apply("deg", listOf(PI))!!
            val degrees = registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble()
            degrees shouldBeGreaterThan 179.99
            degrees shouldBeLessThan 180.01
        }

        it("should convert degrees to radians") {
            val result = registry.apply("rad", listOf(180.0))!!
            val radians = registry.extractNumber((result as FunctionResult.LiteralElement).element)!!.toDouble()
            radians shouldBeGreaterThan PI - 0.01
            radians shouldBeLessThan PI + 0.01
        }
    }
})
