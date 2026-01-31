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
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaProperty
import org.openmbee.gearshift.framework.storage.LinkRepository
import org.openmbee.gearshift.framework.storage.ModelRepository

/**
 * Tests for core MDMEngine functionality.
 */
class MDMEngineTest : DescribeSpec({

    describe("MDMEngine instance management") {

        context("creating instances") {

            it("should create instance of a registered class") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(name = "Element")
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                instance shouldNotBe null
                instance.className shouldBe "Element"
            }

            it("should throw when creating instance of non-existent class") {
                val registry = MetamodelRegistry()
                val engine = MDMEngine(registry)

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.createInstance("NonExistent")
                }

                exception.message shouldContain "Unknown class: NonExistent"
            }

            it("should throw when creating instance of abstract class") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "AbstractElement",
                    isAbstract = true
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.createInstance("AbstractElement")
                }

                exception.message shouldContain "Cannot instantiate abstract class"
            }
        }

        context("property management") {

            it("should set and get property values") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "name", type = "String", lowerBound = 0)
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                engine.setPropertyValue(instance, "name", "TestElement")
                val value = engine.getProperty(instance, "name")

                value shouldBe "TestElement"
            }

            it("should throw when setting non-existent property") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(name = "Element")
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.setPropertyValue(instance, "nonExistent", "value")
                }

                exception.message shouldContain "'nonExistent' not found"
            }

            it("should throw when setting read-only property") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "qualifiedName",
                            type = "String",
                            lowerBound = 0,
                            isReadOnly = true
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                val exception = shouldThrow<IllegalStateException> {
                    engine.setPropertyValue(instance, "qualifiedName", "value")
                }

                exception.message shouldContain "Cannot set read-only property"
            }

            it("should handle null property values") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "name", type = "String", lowerBound = 0)
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                engine.setPropertyValue(instance, "name", null)
                val value = engine.getProperty(instance, "name")

                value shouldBe null
            }
        }

        context("property inheritance") {

            it("should access properties from superclasses") {
                val registry = MetamodelRegistry()

                val baseClass = MetaClass(
                    name = "Base",
                    attributes = listOf(
                        MetaProperty(name = "baseProp", type = "String", lowerBound = 0)
                    )
                )

                val derivedClass = MetaClass(
                    name = "Derived",
                    superclasses = listOf("Base"),
                    attributes = listOf(
                        MetaProperty(name = "derivedProp", type = "String", lowerBound = 0)
                    )
                )

                registry.registerClass(baseClass)
                registry.registerClass(derivedClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Derived")

                engine.setPropertyValue(instance, "baseProp", "baseValue")
                engine.setPropertyValue(instance, "derivedProp", "derivedValue")

                engine.getProperty(instance, "baseProp") shouldBe "baseValue"
                engine.getProperty(instance, "derivedProp") shouldBe "derivedValue"
            }
        }

        context("validation") {

            it("should validate required properties") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "elementId",
                            type = "String",
                            // Required (default lowerBound = 1)
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")

                val errors = engine.validate(instance)

                errors shouldHaveSize 1
                errors.first().message shouldContain "Required property 'elementId' is not set"
            }

            it("should pass validation when all required properties are set") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "elementId",
                            type = "String"
                            // default lowerBound = 1 means required
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Element")
                engine.setPropertyValue(instance, "elementId", "elem-123")

                val errors = engine.validate(instance)

                errors.shouldBeEmpty()
            }

            it("should validate unique collections") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(
                    name = "Container",
                    attributes = listOf(
                        MetaProperty(
                            name = "items",
                            type = "String",
                            lowerBound = 0,
                            upperBound = -1,
                            isUnique = true
                        )
                    )
                )
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                val (_, instance) = engine.createInstance("Container")

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.setPropertyValue(instance, "items", listOf("A", "B", "A"))
                }

                exception.message shouldContain "must contain unique values"
            }
        }

        context("instance tracking") {

            it("should track all created instances") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(name = "Element")
                registry.registerClass(metaClass)

                val engine = MDMEngine(registry)
                engine.createInstance("Element")
                engine.createInstance("Element")
                engine.createInstance("Element")

                val instances = engine.getAllInstances()

                instances shouldHaveSize 3
            }
        }
    }
})
