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

class StringFunctionsLibraryTest : DescribeSpec({

    lateinit var registry: FunctionRegistry

    beforeEach {
        val metamodel = MetamodelRegistry()
        metamodel.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(metamodel)
        val engine = MDMEngine(metamodel)
        registry = FunctionRegistry(engine)
        StringFunctionsLibrary().register(registry)
    }

    describe("Concat") {
        it("should concatenate two strings") {
            val result = registry.apply("Concat", listOf("hello", " world"))!!
            registry.extractString((result as FunctionResult.LiteralElement).element) shouldBe "hello world"
        }
    }

    describe("Length") {
        it("should return string length") {
            val result = registry.apply("Length", listOf("hello"))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 5L
        }
    }

    describe("Size") {
        it("should return string size (same as Length)") {
            val result = registry.apply("Size", listOf("test"))!!
            registry.extractNumber((result as FunctionResult.LiteralElement).element) shouldBe 4L
        }
    }

    describe("Substring") {
        it("should extract substring") {
            val result = registry.apply("Substring", listOf("hello world", 0L, 5L))!!
            registry.extractString((result as FunctionResult.LiteralElement).element) shouldBe "hello"
        }
    }

    describe("string comparison") {
        it("should compare strings less-than") {
            val result = registry.apply("String<", listOf("apple", "banana"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should compare strings greater-than") {
            val result = registry.apply("String>", listOf("banana", "apple"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should compare strings less-equal") {
            val result = registry.apply("String<=", listOf("apple", "apple"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }

        it("should compare strings greater-equal") {
            val result = registry.apply("String>=", listOf("banana", "apple"))!!
            registry.extractBoolean((result as FunctionResult.LiteralElement).element) shouldBe true
        }
    }
})
