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
package org.openmbee.gearshift.metamodel

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain as shouldContainElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.engine.MetamodelRegistry

/**
 * Tests for MetamodelRegistry.
 */
class MetamodelRegistryTest : DescribeSpec({

    describe("MetamodelRegistry") {

        context("class registration") {

            it("should register and retrieve a metaclass") {
                val registry = MetamodelRegistry()
                val metaClass = MetaClass(name = "Element")

                registry.registerClass(metaClass)
                val retrieved = registry.getClass("Element")

                retrieved shouldNotBe null
                retrieved?.name shouldBe "Element"
            }

            it("should return null for non-existent class") {
                val registry = MetamodelRegistry()

                val retrieved = registry.getClass("NonExistent")

                retrieved shouldBe null
            }

            it("should list all registered classes") {
                val registry = MetamodelRegistry()

                registry.registerClass(MetaClass(name = "Element"))
                registry.registerClass(MetaClass(name = "Relationship"))
                registry.registerClass(MetaClass(name = "Namespace"))

                val allClasses = registry.getAllClasses()

                allClasses shouldHaveSize 3
                allClasses.map { it.name } shouldContainElement "Element"
                allClasses.map { it.name } shouldContainElement "Relationship"
                allClasses.map { it.name } shouldContainElement "Namespace"
            }
        }

        context("association registration") {

            it("should register and retrieve associations") {
                val registry = MetamodelRegistry()

                val association = MetaAssociation(
                    name = "Ownership",
                    sourceEnd = MetaAssociationEnd(
                        name = "owner",
                        type = "Element",
                        lowerBound = 0
                    ),
                    targetEnd = MetaAssociationEnd(
                        name = "ownedElement",
                        type = "Element",
                        lowerBound = 0, upperBound = -1
                    )
                )

                registry.registerAssociation(association)
                val retrieved = registry.getAssociation("Ownership")

                retrieved shouldNotBe null
                retrieved?.name shouldBe "Ownership"
            }

            it("should list all registered associations") {
                val registry = MetamodelRegistry()

                val assoc1 = MetaAssociation(
                    name = "Assoc1",
                    sourceEnd = MetaAssociationEnd(name = "s1", type = "T1"),
                    targetEnd = MetaAssociationEnd(name = "t1", type = "T2")
                )
                val assoc2 = MetaAssociation(
                    name = "Assoc2",
                    sourceEnd = MetaAssociationEnd(name = "s2", type = "T1"),
                    targetEnd = MetaAssociationEnd(name = "t2", type = "T2")
                )

                registry.registerAssociation(assoc1)
                registry.registerAssociation(assoc2)

                val allAssociations = registry.getAllAssociations()

                allAssociations shouldHaveSize 2
                allAssociations.map { it.name } shouldContainElement "Assoc1"
                allAssociations.map { it.name } shouldContainElement "Assoc2"
            }
        }

        context("metamodel validation") {

            it("should validate valid metamodel") {
                val registry = MetamodelRegistry()

                val baseClass = MetaClass(
                    name = "Base",
                    attributes = listOf(
                        MetaProperty(name = "prop", type = "String", lowerBound = 0)
                    )
                )
                val derivedClass = MetaClass(
                    name = "Derived",
                    superclasses = listOf("Base")
                )

                registry.registerClass(baseClass)
                registry.registerClass(derivedClass)

                val errors = registry.validate()

                errors.shouldBeEmpty()
            }

            it("should detect missing superclass") {
                val registry = MetamodelRegistry()

                val derivedClass = MetaClass(
                    name = "Derived",
                    superclasses = listOf("NonExistent")
                )

                registry.registerClass(derivedClass)

                val errors = registry.validate()

                errors shouldHaveSize 1
                errors.first() shouldContain "Derived"
                errors.first() shouldContain "NonExistent"
            }
        }

        context("clearing registry") {

            it("should clear all registered classes and associations") {
                val registry = MetamodelRegistry()

                registry.registerClass(MetaClass(name = "Element"))
                registry.registerAssociation(
                    MetaAssociation(
                        name = "Assoc",
                        sourceEnd = MetaAssociationEnd(name = "s", type = "T"),
                        targetEnd = MetaAssociationEnd(name = "t", type = "T")
                    )
                )

                registry.clear()

                registry.getAllClasses().shouldBeEmpty()
                registry.getAllAssociations().shouldBeEmpty()
            }
        }
    }
})
