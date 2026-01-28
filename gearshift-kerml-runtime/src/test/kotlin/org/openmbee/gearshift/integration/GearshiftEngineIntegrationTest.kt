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
package org.openmbee.gearshift.integration

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.meta.*

/**
 * Integration tests for the complete GearshiftEngine workflow.
 * Tests the full stack: metamodel registration, instance creation, operations, and repository.
 */
class GearshiftEngineIntegrationTest : DescribeSpec({

    describe("GearshiftEngine full workflow") {

        context("basic metamodel to instance workflow") {

            it("should support complete lifecycle: register, create, set properties, invoke operations") {
                val engine = GearshiftEngine()

                // 1. Register metamodel
                val elementMetaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(
                            name = "elementId",
                            type = "String"
                            // Required (default lowerBound = 1)
                        ),
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
                            isQuery = true,
                            description = "Returns the effective name"
                        )
                    )
                )
                engine.registerMetaClass(elementMetaClass)

                // 2. Create instance
                val (elementId, element) = engine.createInstance("Element")
                elementId shouldNotBe null
                element shouldNotBe null

                // 3. Set properties
                engine.setProperty(elementId, "elementId", "elem-001")
                engine.setProperty(elementId, "declaredName", "MyElement")

                // 4. Get properties
                engine.getProperty(elementId, "elementId") shouldBe "elem-001"
                engine.getProperty(elementId, "declaredName") shouldBe "MyElement"

                // 5. Invoke operation
                val effectiveName = engine.invokeOperation(elementId, "effectiveName")
                effectiveName shouldBe "MyElement"

                // 6. Validate instance
                val errors = engine.validateInstance(elementId)
                errors.shouldBeEmpty()
            }
        }

        context("repository integration") {

            it("should store and retrieve instances from repository") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(
                    name = "Feature",
                    attributes = listOf(
                        MetaProperty(name = "name", type = "String", lowerBound = 0),
                        MetaProperty(name = "isAbstract", type = "Boolean")
                    )
                )
                engine.registerMetaClass(metaClass)

                // Create multiple instances
                val (id1, _) = engine.createInstance("Feature")
                val (id2, _) = engine.createInstance("Feature")
                val (id3, _) = engine.createInstance("Feature")

                engine.setProperty(id1, "name", "Feature1")
                engine.setProperty(id2, "name", "Feature2")
                engine.setProperty(id3, "name", "Feature3")

                // Retrieve by type
                val features = engine.getInstancesByType("Feature")
                features shouldHaveSize 3

                // Retrieve by ID
                val feature1 = engine.getInstance(id1)
                feature1 shouldNotBe null
                feature1?.getProperty("name") shouldBe "Feature1"
            }

            it("should query instances by property value") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "type", type = "String", lowerBound = 0)
                    )
                )
                engine.registerMetaClass(metaClass)

                val (id1, _) = engine.createInstance("Element")
                val (id2, _) = engine.createInstance("Element")
                val (id3, _) = engine.createInstance("Element")

                engine.setProperty(id1, "type", "TypeA")
                engine.setProperty(id2, "type", "TypeB")
                engine.setProperty(id3, "type", "TypeA")

                val typeAElements = engine.getInstancesByProperty("type", "TypeA")
                typeAElements shouldHaveSize 2
            }

            it("should delete instances") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(name = "Element")
                engine.registerMetaClass(metaClass)

                val (id, _) = engine.createInstance("Element")

                // Verify it exists
                engine.getInstance(id) shouldNotBe null

                // Delete it
                val deleted = engine.deleteInstance(id)
                deleted shouldBe true

                // Verify it's gone
                engine.getInstance(id) shouldBe null
            }
        }

        context("inheritance and operations") {

            it("should invoke inherited operations") {
                val engine = GearshiftEngine()

                // Register base class with operation
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
                engine.registerMetaClass(baseClass)

                // Register derived class without its own operations
                val derivedClass = MetaClass(
                    name = "Derived",
                    superclasses = listOf("Base"),
                    attributes = listOf(
                        MetaProperty(name = "extra", type = "String", lowerBound = 0)
                    )
                )
                engine.registerMetaClass(derivedClass)

                // Create instance of derived class
                val (id, _) = engine.createInstance("Derived")
                engine.setProperty(id, "value", "inherited value")

                // Invoke inherited operation
                val result = engine.invokeOperation(id, "getValue")
                result shouldBe "inherited value"
            }
        }

        context("metamodel validation") {

            it("should validate metamodel consistency") {
                val engine = GearshiftEngine()

                val validClass = MetaClass(
                    name = "Element",
                    attributes = listOf(
                        MetaProperty(name = "name", type = "String", lowerBound = 0)
                    )
                )
                engine.registerMetaClass(validClass)

                val errors = engine.validateMetamodel()
                errors.shouldBeEmpty()
            }
        }

        context("statistics and monitoring") {

            it("should provide repository statistics") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(name = "Element")
                engine.registerMetaClass(metaClass)

                engine.createInstance("Element")
                engine.createInstance("Element")
                engine.createInstance("Element")

                val stats = engine.getStatistics()
                stats.objects.totalObjects shouldBe 3
                stats.objects.typeDistribution["Element"] shouldBe 3
            }
        }

        context("clearing data") {

            it("should clear instances but keep metamodel") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(name = "Element")
                engine.registerMetaClass(metaClass)
                engine.createInstance("Element")

                engine.clearInstances()

                engine.getInstancesByType("Element").shouldBeEmpty()
                engine.getMetaClass("Element") shouldNotBe null
            }

            it("should clear everything") {
                val engine = GearshiftEngine()

                val metaClass = MetaClass(name = "Element")
                engine.registerMetaClass(metaClass)
                engine.createInstance("Element")

                engine.clearAll()

                engine.getInstancesByType("Element").shouldBeEmpty()
                engine.getMetaClass("Element") shouldBe null
            }
        }
    }

    describe("KerML Element operations") {

        context("effectiveName operation") {

            it("should invoke effectiveName on Element instances") {
                val engine = GearshiftEngine()

                // Simplified Element metaclass for testing
                val elementMetaClass = MetaClass(
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
                            description = "Return effective name for this Element"
                        )
                    )
                )
                engine.registerMetaClass(elementMetaClass)

                val (id, _) = engine.createInstance("Element")
                engine.setProperty(id, "declaredName", "TestElement")

                val effectiveName = engine.invokeOperation(id, "effectiveName")
                effectiveName shouldBe "TestElement"
            }

            it("should handle null declaredName") {
                val engine = GearshiftEngine()

                val elementMetaClass = MetaClass(
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
                            body = "declaredName"
                        )
                    )
                )
                engine.registerMetaClass(elementMetaClass)

                val (id, _) = engine.createInstance("Element")
                // Don't set declaredName

                val effectiveName = engine.invokeOperation(id, "effectiveName")
                effectiveName shouldBe null
            }
        }
    }
})
