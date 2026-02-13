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
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class BaseFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        BaseFunctionsLibrary().register(registry)
    }

    describe("equality") {
        it("should test equal integers") {
            val result = registry.apply("=", listOf(5L, 5L))!!
            result.shouldBeInstanceOf<FunctionResult.LiteralElement>()
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should test unequal integers") {
            val result = registry.apply("=", listOf(5L, 3L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should test not-equal") {
            val result = registry.apply("!=", listOf(5L, 3L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("identity") {
        it("should test identical references") {
            val obj = "same"
            val result = registry.apply("===", listOf(obj, obj))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should test non-identical references") {
            val result = registry.apply("!==", listOf("a", "b"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("ToString") {
        it("should convert number to string") {
            val result = registry.apply("ToString", listOf(42L))!!
            registry.extractString((result as FunctionResult.LiteralElement).element) shouldBe "42"
        }

        it("should convert null to 'null'") {
            val result = registry.apply("ToString", listOf(null))!!
            registry.extractString((result as FunctionResult.LiteralElement).element) shouldBe "null"
        }
    }

    describe("index (#)") {
        it("should get element at 1-based index") {
            val result = registry.apply("#", listOf(listOf("a", "b", "c"), 2L))!!
            (result as FunctionResult.Value).value shouldBe "b"
        }

        it("should return null for out-of-bounds") {
            val result = registry.apply("#", listOf(listOf("a"), 5L))!!
            (result as FunctionResult.Value).value.shouldBeNull()
        }
    }

    describe("sequence concat (,)") {
        it("should concatenate two lists") {
            val result = registry.apply(",", listOf(listOf(1, 2), listOf(3, 4)))!!
            (result as FunctionResult.Value).value shouldBe listOf(1, 2, 3, 4)
        }

        it("should handle single elements") {
            val result = registry.apply(",", listOf(1, 2))!!
            (result as FunctionResult.Value).value shouldBe listOf(1, 2)
        }
    }
})
