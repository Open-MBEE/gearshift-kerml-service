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

class CollectionFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        CollectionFunctionsLibrary().register(registry)
    }

    describe("contains") {
        it("should return true when collection contains element") {
            val result = registry.apply("contains", listOf(listOf(1L, 2L, 3L), 2L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false when collection does not contain element") {
            val result = registry.apply("contains", listOf(listOf(1L, 2L, 3L), 5L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }

    describe("containsAll") {
        it("should return true when collection contains all elements") {
            val result = registry.apply("containsAll", listOf(listOf(1L, 2L, 3L), listOf(1L, 3L)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false when collection is missing elements") {
            val result = registry.apply("containsAll", listOf(listOf(1L, 2L), listOf(1L, 3L)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }
})
