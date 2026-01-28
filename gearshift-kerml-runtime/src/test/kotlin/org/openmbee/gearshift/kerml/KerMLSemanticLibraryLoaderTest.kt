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
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

/**
 * Tests for loading the KerML Kernel Semantic Library.
 */
class KerMLSemanticLibraryLoaderTest : DescribeSpec({

    describe("KerML Semantic Library Loading") {

        describe("library availability") {
            it("should detect library availability") {
                val available = KerMLSemanticLibraryLoader.isLibraryAvailable()
                // Library should be available in the references directory
                available shouldBe true
            }
        }

        describe("Base library loading") {
            lateinit var factory: KerMLModelFactory

            beforeEach {
                factory = KerMLModelFactory()
            }

            it("should load Base.kerml successfully") {
                val result = KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parsing should have no syntax errors (cross-library refs are expected to be unresolved)
                result.fileName shouldBe "Base.kerml"
                result.parseResult shouldNotBe null
                result.parseResult!!.errors shouldBe emptyList()
                result.error shouldBe null
            }

            it("should parse Base package as LibraryPackage") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val engine = factory.engine
                val libraryPackages = engine.getInstancesByType("LibraryPackage")

                libraryPackages shouldHaveAtLeastSize 1

                // Find the Base package
                val basePackage = libraryPackages.find {
                    engine.getProperty(it.id!!, "declaredName") == "Base"
                }
                basePackage shouldNotBe null
            }

            it("should parse Anything classifier") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val engine = factory.engine
                val classifiers = engine.getInstancesByType("Classifier")

                // Find Anything
                val anything = classifiers.find {
                    engine.getProperty(it.id!!, "declaredName") == "Anything"
                }
                anything shouldNotBe null
                engine.getProperty(anything!!.id!!, "isAbstract") shouldBe true
            }

            it("should parse things feature") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val engine = factory.engine
                val features = engine.getInstancesByType("Feature")

                // Find things
                val things = features.find {
                    engine.getProperty(it.id!!, "declaredName") == "things"
                }
                things shouldNotBe null
                engine.getProperty(things!!.id!!, "isAbstract") shouldBe true
            }

            it("should parse DataValue datatype") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val engine = factory.engine
                val datatypes = engine.getInstancesByType("DataType")

                // Find DataValue
                val dataValue = datatypes.find {
                    engine.getProperty(it.id!!, "declaredName") == "DataValue"
                }
                dataValue shouldNotBe null
                engine.getProperty(dataValue!!.id!!, "isAbstract") shouldBe true
            }

            it("should parse multiplicity elements") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val engine = factory.engine
                val multiplicities = engine.getInstancesByType("MultiplicityRange")

                // Should have exactlyOne, zeroOrOne, oneToMany, zeroToMany
                val names = multiplicities.mapNotNull {
                    engine.getProperty(it.id!!, "declaredName") as? String
                }

                names shouldContain "exactlyOne"
                names shouldContain "zeroOrOne"
                names shouldContain "oneToMany"
                names shouldContain "zeroToMany"
            }

            it("should register elements with qualified names for resolution") {
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                val coordinator = factory.getCoordinator()
                val parsedElements = coordinator.getParsedElements()

                // Should have Base::Anything registered
                parsedElements.keys shouldContain "Base::Anything"
                parsedElements.keys shouldContain "Base::things"
                parsedElements.keys shouldContain "Base::DataValue"
            }
        }

        describe("Full library loading") {
            lateinit var factory: KerMLModelFactory

            beforeEach {
                factory = KerMLModelFactory()
            }

            it("should load all library files") {
                val results = KerMLSemanticLibraryLoader.loadLibrary(factory)

                // Should have loaded multiple files
                results shouldHaveAtLeastSize 1

                // At minimum, Base.kerml should parse without errors
                val baseResult = results.find { it.fileName == "Base.kerml" }
                baseResult shouldNotBe null
                baseResult!!.parseResult?.errors shouldBe emptyList()

                // Note: Other library files may have constructs not yet fully supported
                // (constraints, expression bodies, etc.) - those are tracked separately
            }
        }
    }
})
