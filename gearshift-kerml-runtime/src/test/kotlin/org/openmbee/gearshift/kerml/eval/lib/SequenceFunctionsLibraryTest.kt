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

class SequenceFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        SequenceFunctionsLibrary().register(registry)
    }

    describe("size") {
        it("should return size of a list") {
            val result = registry.apply("size", listOf(listOf(1, 2, 3)))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 3L
        }

        it("should return 0 for null") {
            val result = registry.apply("size", listOf(null))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 0L
        }

        it("should return 1 for a single element") {
            val result = registry.apply("size", listOf("single"))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 1L
        }
    }

    describe("isEmpty/notEmpty") {
        it("should return true for empty list") {
            val result = registry.apply("isEmpty", listOf(emptyList<Any>()))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false for non-empty list") {
            val result = registry.apply("isEmpty", listOf(listOf(1)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should return true for non-empty list via notEmpty") {
            val result = registry.apply("notEmpty", listOf(listOf(1)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("includes/excludes") {
        it("should return true when list includes element") {
            val result = registry.apply("includes", listOf(listOf(1L, 2L, 3L), 2L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return true when list excludes element") {
            val result = registry.apply("excludes", listOf(listOf(1L, 2L, 3L), 5L))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("head/tail/last") {
        it("should return first element") {
            val result = registry.apply("head", listOf(listOf("a", "b", "c")))!!
            (result as FunctionResult.Value).value shouldBe "a"
        }

        it("should return all but first") {
            val result = registry.apply("tail", listOf(listOf("a", "b", "c")))!!
            (result as FunctionResult.Value).value shouldBe listOf("b", "c")
        }

        it("should return last element") {
            val result = registry.apply("last", listOf(listOf("a", "b", "c")))!!
            (result as FunctionResult.Value).value shouldBe "c"
        }
    }

    describe("union/intersection") {
        it("should union two lists") {
            val result = registry.apply("union", listOf(listOf(1, 2), listOf(3, 4)))!!
            (result as FunctionResult.Value).value shouldBe listOf(1, 2, 3, 4)
        }

        it("should intersect two lists") {
            val result = registry.apply("intersection", listOf(listOf(1L, 2L, 3L), listOf(2L, 3L, 4L)))!!
            (result as FunctionResult.Value).value shouldBe listOf(2L, 3L)
        }
    }

    describe("including/excluding") {
        it("should include element in sequence") {
            val result = registry.apply("including", listOf(listOf(1, 2), 3))!!
            (result as FunctionResult.Value).value shouldBe listOf(1, 2, 3)
        }

        it("should exclude element from sequence") {
            val result = registry.apply("excluding", listOf(listOf(1L, 2L, 3L), 2L))!!
            (result as FunctionResult.Value).value shouldBe listOf(1L, 3L)
        }
    }

    describe("equals") {
        it("should return true for equal sequences") {
            val result = registry.apply("equals", listOf(listOf(1L, 2L), listOf(1L, 2L)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false for unequal sequences") {
            val result = registry.apply("equals", listOf(listOf(1L, 2L), listOf(1L, 3L)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }

    describe("subsequence") {
        it("should extract subsequence with 1-based indices") {
            val result = registry.apply("subsequence", listOf(listOf("a", "b", "c", "d"), 2L, 3L))!!
            (result as FunctionResult.Value).value shouldBe listOf("b", "c")
        }
    }
})
