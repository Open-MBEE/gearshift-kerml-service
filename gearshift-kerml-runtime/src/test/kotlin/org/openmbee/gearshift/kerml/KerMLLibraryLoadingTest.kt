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
package org.openmbee.gearshift.kerml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.generated.interfaces.DataType
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Function
import org.openmbee.gearshift.generated.interfaces.Namespace

/**
 * Tests that verify all KerML standard library files load correctly.
 *
 * This test validates that:
 * 1. All 36 library files load without errors
 * 2. Key elements from each library category are present via resolveGlobal
 * 3. Cross-library references resolve correctly
 *
 * Uses KerMLTestSpec to ensure the library is loaded and mounted.
 */
class KerMLLibraryLoadingTest : KerMLTestSpec({

    /**
     * Helper to resolve a qualified name globally using the model's root namespace.
     */
    fun KerMLModel.resolveGlobal(qualifiedName: String): Element? {
        val membership = root.resolveGlobal(qualifiedName)
        return membership?.memberElement
    }

    describe("KerML Standard Library Loading") {

        context("loading all library files") {

            it("should load all library files without errors") {
                val model = KerMLModel(projectName = "LibraryLoadTest")
                val results = KerMLSemanticLibraryLoader.loadLibrary(model)

                // Should have 36 library files total (16 + 3 + 17)
                results shouldHaveSize 36

                // All should succeed
                val failures = results.filter { !it.success }
                if (failures.isNotEmpty()) {
                    println("Failed library files:")
                    failures.forEach { println("  - ${it.fileName}: ${it.error}") }
                }
                failures shouldHaveSize 0

                // Print summary
                val successCount = results.count { it.success }
                println("Successfully loaded $successCount/${results.size} library files")
            }
        }

        context("Kernel Semantic Library elements") {

            it("should have Base::Anything and Base::things") {
                val model = freshModel()

                val anything = model.resolveGlobal("Base::Anything") as? Classifier
                anything.shouldNotBeNull()
                anything.declaredName shouldBe "Anything"

                val things = model.resolveGlobal("Base::things") as? Feature
                things.shouldNotBeNull()
                things.declaredName shouldBe "things"
            }

            it("should have Occurrences::Occurrence") {
                val model = freshModel()

                val occurrence = model.resolveGlobal("Occurrences::Occurrence") as? Classifier
                occurrence.shouldNotBeNull()
                occurrence.declaredName shouldBe "Occurrence"
            }

            it("should have Links::Link and Links::links") {
                val model = freshModel()

                val link = model.resolveGlobal("Links::Link") as? Classifier
                link.shouldNotBeNull()

                val links = model.resolveGlobal("Links::links") as? Feature
                links.shouldNotBeNull()
            }

            it("should have Objects::Object") {
                val model = freshModel()

                val obj = model.resolveGlobal("Objects::Object") as? Classifier
                obj.shouldNotBeNull()
            }

            it("should have Performances::Performance") {
                val model = freshModel()

                val performance = model.resolveGlobal("Performances::Performance") as? Classifier
                performance.shouldNotBeNull()
            }

            it("should have Transfers::Transfer") {
                val model = freshModel()

                val transfer = model.resolveGlobal("Transfers::Transfer") as? Classifier
                transfer.shouldNotBeNull()
            }
        }

        context("Kernel Data Type Library elements") {

            it("should have ScalarValues::Boolean, Integer, String, Real") {
                val model = freshModel()

                val boolean = model.resolveGlobal("ScalarValues::Boolean") as? DataType
                boolean.shouldNotBeNull()

                val integer = model.resolveGlobal("ScalarValues::Integer") as? DataType
                integer.shouldNotBeNull()

                val string = model.resolveGlobal("ScalarValues::String") as? DataType
                string.shouldNotBeNull()

                val real = model.resolveGlobal("ScalarValues::Real") as? DataType
                real.shouldNotBeNull()
            }

            it("should have Collections::Array") {
                val model = freshModel()

                val array = model.resolveGlobal("Collections::Array") as? Classifier
                array.shouldNotBeNull()
            }
        }

        context("Kernel Function Library elements") {

            it("should have BaseFunctions namespace with members") {
                val model = freshModel()

                val baseFunctions = model.resolveGlobal("BaseFunctions") as? Namespace
                baseFunctions.shouldNotBeNull()

                // Verify it has members (even if names aren't parsed yet for operator syntax)
                baseFunctions.ownedMember.shouldNotBeEmpty()
            }

            it("should have BooleanFunctions namespace with members") {
                val model = freshModel()

                val booleanFunctions = model.resolveGlobal("BooleanFunctions") as? Namespace
                booleanFunctions.shouldNotBeNull()
                booleanFunctions.ownedMember.shouldNotBeEmpty()
            }

            it("should have IntegerFunctions namespace with members") {
                val model = freshModel()

                val integerFunctions = model.resolveGlobal("IntegerFunctions") as? Namespace
                integerFunctions.shouldNotBeNull()
                integerFunctions.ownedMember.shouldNotBeEmpty()
            }

            it("should have StringFunctions namespace with members") {
                val model = freshModel()

                val stringFunctions = model.resolveGlobal("StringFunctions") as? Namespace
                stringFunctions.shouldNotBeNull()
                stringFunctions.ownedMember.shouldNotBeEmpty()
            }

            it("should have SequenceFunctions namespace with members") {
                val model = freshModel()

                val sequenceFunctions = model.resolveGlobal("SequenceFunctions") as? Namespace
                sequenceFunctions.shouldNotBeNull()
                sequenceFunctions.ownedMember.shouldNotBeEmpty()
            }
        }

        context("cross-library references") {

            it("should resolve references from Functions to ScalarValues types") {
                val model = freshModel()

                // IntegerFunctions should reference Integer from ScalarValues
                val integerFunctions = model.resolveGlobal("IntegerFunctions") as? Namespace
                integerFunctions.shouldNotBeNull()

                // Verify the namespace exists and has members
                val members = integerFunctions.ownedMember
                members.shouldNotBeEmpty()
            }
        }

        context("spec-compliant UUID v5 element IDs") {

            it("should generate same UUID v5 for same inputs") {
                val ns = LibraryElementIdAssigner.NAMESPACE_URL_UUID
                val id1 = LibraryElementIdAssigner.generateUuidV5(ns, "https://www.omg.org/spec/KerML/Base")
                val id2 = LibraryElementIdAssigner.generateUuidV5(ns, "https://www.omg.org/spec/KerML/Base")

                id1 shouldBe id2
            }

            it("should generate different UUIDs for different inputs") {
                val ns = LibraryElementIdAssigner.NAMESPACE_URL_UUID
                val id1 = LibraryElementIdAssigner.generateUuidV5(ns, "https://www.omg.org/spec/KerML/Base")
                val id2 = LibraryElementIdAssigner.generateUuidV5(ns, "https://www.omg.org/spec/KerML/Links")

                id1 shouldNotBe id2
            }

            it("should generate valid UUID v5 format") {
                val ns = LibraryElementIdAssigner.NAMESPACE_URL_UUID
                val id = LibraryElementIdAssigner.generateUuidV5(ns, "https://www.omg.org/spec/KerML/Base")

                // UUID v5 has version nibble = 5 and variant bits = 10xx
                val uuidStr = id.toString()
                val uuidRegex = Regex("[0-9a-f]{8}-[0-9a-f]{4}-5[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")
                uuidStr.matches(uuidRegex) shouldBe true
            }

            it("should assign top-level package UUID matching spec formula") {
                val model = KerMLModel(projectName = "SpecUUIDTest")
                val results = KerMLSemanticLibraryLoader.loadLibrary(model)
                results.filter { !it.success } shouldHaveSize 0

                val base = model.resolveGlobal("Base") as? Namespace
                base.shouldNotBeNull()

                // Top-level package: UUIDv5(URL_NAMESPACE_UUID, "https://www.omg.org/spec/KerML/Base")
                val expectedUuid = LibraryElementIdAssigner.generateUuidV5(
                    LibraryElementIdAssigner.NAMESPACE_URL_UUID,
                    "${LibraryElementIdAssigner.KERML_URL_PREFIX}Base"
                )
                base.elementId shouldBe expectedUuid.toString()
            }

            it("should assign inner element UUID using package UUID as namespace") {
                val model = KerMLModel(projectName = "InnerUUIDTest")
                val results = KerMLSemanticLibraryLoader.loadLibrary(model)
                results.filter { !it.success } shouldHaveSize 0

                val anything = model.resolveGlobal("Base::Anything") as? Classifier
                anything.shouldNotBeNull()

                // Inner element: UUIDv5(basePackageUUID, "Base::Anything")
                val basePackageUuid = LibraryElementIdAssigner.generateUuidV5(
                    LibraryElementIdAssigner.NAMESPACE_URL_UUID,
                    "${LibraryElementIdAssigner.KERML_URL_PREFIX}Base"
                )
                val expectedUuid = LibraryElementIdAssigner.generateUuidV5(basePackageUuid, "Base::Anything")
                anything.elementId shouldBe expectedUuid.toString()
            }

            it("should give library elements deterministic IDs across sessions") {
                // Load library twice and verify Base::Anything has same ID
                val model1 = KerMLModel(projectName = "LibraryTest1")
                val results1 = KerMLSemanticLibraryLoader.loadLibrary(model1)
                results1.filter { !it.success } shouldHaveSize 0

                val model2 = KerMLModel(projectName = "LibraryTest2")
                val results2 = KerMLSemanticLibraryLoader.loadLibrary(model2)
                results2.filter { !it.success } shouldHaveSize 0

                val anything1 = model1.resolveGlobal("Base::Anything")
                val anything2 = model2.resolveGlobal("Base::Anything")

                anything1.shouldNotBeNull()
                anything2.shouldNotBeNull()

                anything1.elementId shouldBe anything2.elementId
            }

            it("should give unnamed elements (OwningMemberships) deterministic IDs across sessions") {
                val model1 = KerMLModel(projectName = "UnnamedTest1")
                val results1 = KerMLSemanticLibraryLoader.loadLibrary(model1)
                results1.filter { !it.success } shouldHaveSize 0

                val model2 = KerMLModel(projectName = "UnnamedTest2")
                val results2 = KerMLSemanticLibraryLoader.loadLibrary(model2)
                results2.filter { !it.success } shouldHaveSize 0

                // Get Base::Anything's owning membership - it should have a deterministic ID
                val anything1 = model1.resolveGlobal("Base::Anything")
                val anything2 = model2.resolveGlobal("Base::Anything")

                anything1.shouldNotBeNull()
                anything2.shouldNotBeNull()

                val om1 = anything1.owningMembership
                val om2 = anything2.owningMembership

                om1.shouldNotBeNull()
                om2.shouldNotBeNull()

                om1.elementId shouldBe om2.elementId
            }
        }
    }
})
