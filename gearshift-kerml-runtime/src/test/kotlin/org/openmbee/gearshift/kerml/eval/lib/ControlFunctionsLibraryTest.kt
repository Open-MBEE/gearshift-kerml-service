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

class ControlFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        ControlFunctionsLibrary().register(registry)
    }

    describe("if") {
        it("should return then value for true condition") {
            val result = registry.apply("if", listOf(true, "yes", "no"))!!
            (result as FunctionResult.Value).value shouldBe "yes"
        }

        it("should return else value for false condition") {
            val result = registry.apply("if", listOf(false, "yes", "no"))!!
            (result as FunctionResult.Value).value shouldBe "no"
        }
    }

    describe("null coalesce") {
        it("should return value when not null") {
            val result = registry.apply("??", listOf("present", "default"))!!
            (result as FunctionResult.Value).value shouldBe "present"
        }

        it("should return default when null") {
            val result = registry.apply("??", listOf(null, "default"))!!
            (result as FunctionResult.Value).value shouldBe "default"
        }
    }

    describe("boolean logic") {
        it("should compute and") {
            val result = registry.apply("and", listOf(true, false))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should short-circuit and on false") {
            val result = registry.apply("and", listOf(false, null))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should compute or") {
            val result = registry.apply("or", listOf(false, true))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should compute implies") {
            val result = registry.apply("implies", listOf(true, false))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should short-circuit implies on false antecedent") {
            val result = registry.apply("implies", listOf(false, null))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }

    describe("allTrue/anyTrue") {
        it("should return true when all are true") {
            val result = registry.apply("allTrue", listOf(listOf(true, true, true)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false when not all are true") {
            val result = registry.apply("allTrue", listOf(listOf(true, false, true)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }

        it("should return true when any is true") {
            val result = registry.apply("anyTrue", listOf(listOf(false, true, false)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should return false when none are true") {
            val result = registry.apply("anyTrue", listOf(listOf(false, false)))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe false
        }
    }
})
