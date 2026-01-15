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
package org.openmbee.gearshift.constraints.parsers.ocl

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.constraints.EngineAccessor
import org.openmbee.gearshift.engine.MDMObject
import org.openmbee.gearshift.metamodel.MetaClass

/**
 * Tests for OCL string operations including KerML-specific operations.
 */
class OclStringOperationsTest : DescribeSpec({

    // Simple stub for testing
    val stubAccessor = object : EngineAccessor {
        override fun getInstance(id: String): MDMObject? = null
        override fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> = emptyList()
        override fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> = emptyList()
        override fun getProperty(instanceId: String, propertyName: String): Any? = null
        override fun isSubclassOf(subclass: String, superclass: String): Boolean = false
    }

    fun createExecutor(): OclExecutor {
        val metaClass = MetaClass(name = "TestClass")
        val obj = MDMObject("TestClass", metaClass)
        obj.id = "test-id"
        return OclExecutor(stubAccessor, obj, "test-id")
    }

    describe("isBasicName operation") {

        it("should return true for simple identifier") {
            val executor = createExecutor()
            val ast = OclParser.parse("'myName'.isBasicName()")
            executor.evaluate(ast) shouldBe true
        }

        it("should return true for identifier starting with underscore") {
            val executor = createExecutor()
            val ast = OclParser.parse("'_privateName'.isBasicName()")
            executor.evaluate(ast) shouldBe true
        }

        it("should return true for identifier with numbers") {
            val executor = createExecutor()
            val ast = OclParser.parse("'element123'.isBasicName()")
            executor.evaluate(ast) shouldBe true
        }

        it("should return false for name with spaces") {
            val executor = createExecutor()
            val ast = OclParser.parse("'my name'.isBasicName()")
            executor.evaluate(ast) shouldBe false
        }

        it("should return false for name starting with number") {
            val executor = createExecutor()
            val ast = OclParser.parse("'123abc'.isBasicName()")
            executor.evaluate(ast) shouldBe false
        }

        it("should return false for name with special characters") {
            val executor = createExecutor()
            val ast = OclParser.parse("'my-name'.isBasicName()")
            executor.evaluate(ast) shouldBe false
        }

        it("should return false for empty string") {
            val executor = createExecutor()
            val ast = OclParser.parse("''.isBasicName()")
            executor.evaluate(ast) shouldBe false
        }
    }

    describe("asRestrictedName operation") {

        it("should wrap name in single quotes") {
            val executor = createExecutor()
            val ast = OclParser.parse("'my name'.asRestrictedName()")
            executor.evaluate(ast) shouldBe "'my name'"
        }

        it("should wrap basic name in single quotes") {
            val executor = createExecutor()
            val ast = OclParser.parse("'simpleName'.asRestrictedName()")
            executor.evaluate(ast) shouldBe "'simpleName'"
        }
    }

    describe("matches operation") {

        it("should match simple pattern") {
            val executor = createExecutor()
            val ast = OclParser.parse("'hello'.matches('hello')")
            executor.evaluate(ast) shouldBe true
        }

        it("should match regex pattern") {
            val executor = createExecutor()
            val ast = OclParser.parse("'abc123'.matches('[a-z]+[0-9]+')")
            executor.evaluate(ast) shouldBe true
        }

        it("should return false for non-matching pattern") {
            val executor = createExecutor()
            val ast = OclParser.parse("'hello'.matches('[0-9]+')")
            executor.evaluate(ast) shouldBe false
        }
    }
})
