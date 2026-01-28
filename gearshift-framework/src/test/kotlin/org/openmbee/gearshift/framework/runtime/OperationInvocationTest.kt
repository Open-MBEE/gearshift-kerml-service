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
package org.openmbee.gearshift.framework.runtime

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.framework.meta.BodyLanguage
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaOperation
import org.openmbee.gearshift.framework.meta.MetaParameter
import org.openmbee.gearshift.framework.meta.MetaProperty
import org.openmbee.gearshift.framework.storage.LinkRepository
import org.openmbee.gearshift.framework.storage.ModelRepository

/**
 * Tests for operation invocation functionality in MDMEngine.
 */
class OperationInvocationTest : DescribeSpec({

    describe("MDMEngine operation invocation") {

        context("invoking operations with simple property access bodies") {

            it("should invoke operation that returns a property value") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "declaredName",
                            type = "String",
                            lowerBound = 0
                        )
                    ),
                    operations = listOf(
                        MetaOperation(
                            name = "effectiveName",
                            returnType = "String",
                            body = "declaredName",
                            isQuery = true
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")
                engine.setProperty(instance, "declaredName", "TestElement")

                val result = engine.invokeOperation(instance, "effectiveName")

                result shouldBe "TestElement"
            }

            it("should return null when property is null") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "declaredName",
                            type = "String",
                            lowerBound = 0
                        )
                    ),
                    operations = listOf(
                        MetaOperation(
                            name = "effectiveName",
                            returnType = "String",
                            body = "declaredName",
                            isQuery = true
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")
                // Don't set declaredName, leave it null

                val result = engine.invokeOperation(instance, "effectiveName")

                result shouldBe null
            }

            it("should invoke operation without body") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    operations = listOf(
                        MetaOperation(
                            name = "doNothing",
                            returnType = "String"
                            // No body
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")

                val result = engine.invokeOperation(instance, "doNothing")

                result shouldBe null
            }
        }

        context("operation inheritance") {

            it("should invoke inherited operations") {
                val registry = MetamodelRegistry()

                val baseClass = MetaClass(
                    name = "Base",
                    attributes = listOf(
                        MetaProperty(name = "value", type = "String", lowerBound = 0)
                    ),
                    operations = listOf(
                        MetaOperation(
                            name = "getValue",
                            returnType = "String",
                            body = "value"
                        )
                    )
                )

                val derivedClass = MetaClass(
                    name = "Derived",
                    superclasses = listOf("Base"),
                    attributes = listOf(
                        MetaProperty(name = "extraValue", type = "String", lowerBound = 0)
                    )
                )

                registry.registerClass(baseClass)
                registry.registerClass(derivedClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Derived")
                engine.setProperty(instance, "value", "inherited")

                val result = engine.invokeOperation(instance, "getValue")

                result shouldBe "inherited"
            }

            it("should find operations in deep inheritance hierarchies") {
                val registry = MetamodelRegistry()

                val grandparent = MetaClass(
                    name = "Grandparent",
                    attributes = listOf(
                        MetaProperty(name = "data", type = "String", lowerBound = 0)
                    ),
                    operations = listOf(
                        MetaOperation(name = "getData", returnType = "String", body = "data")
                    )
                )

                val parent = MetaClass(
                    name = "Parent",
                    superclasses = listOf("Grandparent")
                )

                val child = MetaClass(
                    name = "Child",
                    superclasses = listOf("Parent")
                )

                registry.registerClass(grandparent)
                registry.registerClass(parent)
                registry.registerClass(child)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Child")
                engine.setProperty(instance, "data", "deep")

                val result = engine.invokeOperation(instance, "getData")

                result shouldBe "deep"
            }
        }

        context("operation parameters") {

            it("should accept operations with parameters") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "add",
                            returnType = "Int",
                            parameters = listOf(
                                MetaParameter(name = "a", type = "Int"),
                                MetaParameter(name = "b", type = "Int")
                            )
                            // Note: Complex body evaluation not yet implemented
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                // Should not throw for valid parameters
                val result = engine.invokeOperation(
                    instance,
                    "add",
                    mapOf("a" to 5, "b" to 3)
                )

                // Will return null since complex body evaluation not implemented
                result shouldBe null
            }

            it("should throw when required parameter is missing") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "add",
                            returnType = "Int",
                            parameters = listOf(
                                MetaParameter(name = "a", type = "Int"),
                                MetaParameter(name = "b", type = "Int")
                            )
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.invokeOperation(instance, "add", mapOf("a" to 5))
                }

                exception.message shouldContain "Missing required parameter 'b'"
            }

            it("should throw when unknown parameter is provided") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "add",
                            returnType = "Int",
                            parameters = listOf(
                                MetaParameter(name = "a", type = "Int"),
                                MetaParameter(name = "b", type = "Int")
                            )
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.invokeOperation(
                        instance,
                        "add",
                        mapOf("a" to 5, "b" to 3, "c" to 7)
                    )
                }

                exception.message shouldContain "Unknown parameter 'c'"
            }

            it("should allow optional parameters with defaults") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "increment",
                            returnType = "Int",
                            parameters = listOf(
                                MetaParameter(name = "value", type = "Int"),
                                MetaParameter(name = "step", type = "Int", defaultValue = "1")
                            )
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                // Should not throw - step has default value
                val result = engine.invokeOperation(
                    instance,
                    "increment",
                    mapOf("value" to 10)
                )

                result shouldBe null // Complex evaluation not yet implemented
            }
        }

        context("error handling") {

            it("should throw when operation does not exist") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(name = "Element")
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.invokeOperation(instance, "nonExistent")
                }

                exception.message shouldContain "Operation 'nonExistent' not found"
                exception.message shouldContain "Element"
            }

            // Kotlin null safety prevents calling invokeOperation with null
            // This would be a compile-time error, so no test needed
        }

        context("query operations") {

            it("should mark operations as query") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "name", type = "String", lowerBound = 0)
                    ),
                    operations = listOf(
                        MetaOperation(
                            name = "getName",
                            returnType = "String",
                            body = "name",
                            isQuery = true
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")
                engine.setProperty(instance, "name", "Test")

                // Query operations should work like any other operation
                val result = engine.invokeOperation(instance, "getName")

                result shouldBe "Test"
            }
        }

        context("Kotlin DSL operations") {

            it("should execute simple Kotlin expression") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "getAnswer",
                            returnType = "Int",
                            body = MetaOperation.kotlinBody("42"),
                            bodyLanguage = BodyLanguage.KOTLIN_DSL
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                val result = engine.invokeOperation(instance, "getAnswer")

                result shouldBe 42
            }

            it("should access self properties from Kotlin DSL") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "value", type = "Int", lowerBound = 0)
                    ),
                    operations = listOf(
                        MetaOperation(
                            name = "doubled",
                            returnType = "Int",
                            body = MetaOperation.kotlinBody("""
                                val v = self.getProperty("value") as? Long ?: 0L
                                v * 2
                            """.trimIndent()),
                            bodyLanguage = BodyLanguage.KOTLIN_DSL
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Element")
                engine.setProperty(instance, "value", 21L)

                val result = engine.invokeOperation(instance, "doubled")

                result shouldBe 42L
            }

            it("should access operation arguments from Kotlin DSL") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Calculator",
                    operations = listOf(
                        MetaOperation(
                            name = "add",
                            returnType = "Int",
                            parameters = listOf(
                                MetaParameter(name = "a", type = "Int"),
                                MetaParameter(name = "b", type = "Int")
                            ),
                            body = MetaOperation.kotlinBody("""
                                val a = args["a"] as Int
                                val b = args["b"] as Int
                                a + b
                            """.trimIndent()),
                            bodyLanguage = BodyLanguage.KOTLIN_DSL
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("Calculator")

                val result = engine.invokeOperation(instance, "add", mapOf("a" to 17, "b" to 25))

                result shouldBe 42
            }

            it("should return collections from Kotlin DSL") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "ListMaker",
                    operations = listOf(
                        MetaOperation(
                            name = "makeList",
                            returnType = "String",
                            body = MetaOperation.kotlinBody("""
                                listOf("a", "b", "c")
                            """.trimIndent()),
                            bodyLanguage = BodyLanguage.KOTLIN_DSL
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
                val instance = engine.createInstance("ListMaker")

                val result = engine.invokeOperation(instance, "makeList")

                result shouldBe listOf("a", "b", "c")
            }
        }
    }
})
