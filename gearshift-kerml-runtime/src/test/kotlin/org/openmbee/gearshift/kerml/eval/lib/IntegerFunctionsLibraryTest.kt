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
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.eval.FunctionRegistry
import org.openmbee.gearshift.kerml.eval.FunctionResult
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class IntegerFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        IntegerFunctionsLibrary().register(registry)
    }

    describe("ToInteger") {
        it("should convert rational to integer") {
            val result = registry.apply("ToInteger", listOf(3.7))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3L
        }

        it("should parse string to integer") {
            val result = registry.apply("ToInteger", listOf("42"))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 42L
        }

        it("should return null for non-numeric string") {
            val result = registry.apply("ToInteger", listOf("abc"))!!
            (result as FunctionResult.Value).value.shouldBeNull()
        }
    }

    describe("ToNatural") {
        it("should convert positive integer") {
            val result = registry.apply("ToNatural", listOf(5L))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 5L
        }

        it("should return null for negative integer") {
            val result = registry.apply("ToNatural", listOf(-3L))!!
            (result as FunctionResult.Value).value.shouldBeNull()
        }
    }
})
