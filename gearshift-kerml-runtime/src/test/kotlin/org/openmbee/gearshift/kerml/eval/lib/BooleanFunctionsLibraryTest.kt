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

class BooleanFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        BooleanFunctionsLibrary().register(registry)
    }

    describe("not") {
        it("should negate true") {
            val result = registry.apply("not", listOf(true))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should negate false") {
            val result = registry.apply("not", listOf(false))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("xor") {
        it("should xor true and false") {
            val result = registry.apply("xor", listOf(true, false))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should xor true and true") {
            val result = registry.apply("xor", listOf(true, true))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }

    describe("ToBoolean") {
        it("should convert boolean") {
            val result = registry.apply("ToBoolean", listOf(true))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should convert string 'true'") {
            val result = registry.apply("ToBoolean", listOf("true"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should convert string 'false'") {
            val result = registry.apply("ToBoolean", listOf("false"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }
})
