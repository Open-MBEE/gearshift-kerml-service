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
package org.openmbee.gearshift.kerml.docgen

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Expose
import org.openmbee.gearshift.generated.interfaces.Rendering
import org.openmbee.gearshift.generated.interfaces.RenderingFeature
import org.openmbee.gearshift.generated.interfaces.View
import org.openmbee.gearshift.generated.interfaces.ViewRenderingMembership
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Tests for [ViewResolverService].
 *
 * Verifies that the service correctly resolves View elements using the
 * generated typed interfaces and their OCL-derived properties.
 */
class ViewResolverServiceTest : KerMLTestSpec({

    describe("ViewResolverService") {

        context("resolveView with a simple View") {

            it("should resolve a View with no exposes, falling back to View itself") {
                val model = freshModel()
                model.parseString(
                    """
                    package TestPkg {
                        class Vehicle {
                            feature wheels : ScalarValues::Integer;
                        }
                        view VehicleView;
                    }
                    """.trimIndent()
                )

                val view = model.findByName<View>("VehicleView")
                view.shouldNotBeNull()

                val service = ViewResolverService(model.engine)
                val resolved = service.resolveView((view as MDMObject).id!!)

                resolved.view shouldBe view
                // No exposes, so falls back to the View itself
                resolved.exposedElements shouldHaveSize 1
                resolved.exposedElements.first() shouldBe view
                resolved.renderingFeature.shouldBeNull()
                resolved.viewpoints shouldHaveSize 0
                resolved.subviews shouldHaveSize 0
            }
        }

        context("resolveView with expose") {

            it("should resolve View with expose to the exposed element") {
                val model = freshModel()
                model.parseString(
                    """
                    package TestPkg {
                        class Vehicle;

                        view VehicleView {
                            expose Vehicle;
                        }
                    }
                    """.trimIndent()
                )

                val view = model.findByName<View>("VehicleView")
                view.shouldNotBeNull()

                val vehicle = model.findByName<Element>("Vehicle")
                vehicle.shouldNotBeNull()

                // Verify the Expose element was created by the parser
                val exposes = model.allOfType<Expose>()
                exposes.shouldHaveSize(1)

                val service = ViewResolverService(model.engine)
                val resolved = service.resolveView((view as MDMObject).id!!)

                resolved.view shouldBe view
                // Expose is now properly wired — importedMembership resolves
                // to Vehicle's owningMembership, and importedElement returns Vehicle
                resolved.exposedElements shouldHaveSize 1
                resolved.exposedElements.first() shouldBe vehicle
            }
        }

        context("resolveView with rendering") {

            it("should resolve View with render member to a RenderingFeature") {
                val model = freshModel()
                model.parseString(
                    """
                    package TestPkg {
                        class Vehicle;

                        rendering MyRendering;

                        view VehicleView {
                            expose Vehicle;
                            render MyRendering;
                        }
                    }
                    """.trimIndent()
                )

                val view = model.findByName<View>("VehicleView")
                view.shouldNotBeNull()

                // Verify the Rendering and ViewRenderingMembership were created
                val renderings = model.allOfType<Rendering>()
                renderings.shouldHaveSize(1)
                renderings.first().declaredName shouldBe "MyRendering"

                val vrms = model.allOfType<ViewRenderingMembership>()
                vrms.shouldHaveSize(1)

                // Verify a RenderingFeature was created as the owned member
                val renderingFeatures = model.allOfType<RenderingFeature>()
                renderingFeatures.shouldHaveSize(1)

                val service = ViewResolverService(model.engine)
                val resolved = service.resolveView((view as MDMObject).id!!)

                resolved.view shouldBe view
                // RenderingFeature is now properly wired via ownedMemberElement
                resolved.renderingFeature.shouldNotBeNull()
                resolved.renderingFeature shouldBe renderingFeatures.first()
            }
        }

        context("error cases") {

            it("should throw ViewNotFoundException for unknown element ID") {
                val model = freshModel()
                val service = ViewResolverService(model.engine)

                shouldThrow<ViewNotFoundException> {
                    service.resolveView("nonexistent-id")
                }
            }

            it("should throw NotAViewException for a non-View element") {
                val model = freshModel()
                model.parseString(
                    """
                    package TestPkg {
                        class Vehicle;
                    }
                    """.trimIndent()
                )

                val vehicle = model.findByName<Element>("Vehicle")
                vehicle.shouldNotBeNull()

                val service = ViewResolverService(model.engine)

                shouldThrow<NotAViewException> {
                    service.resolveView((vehicle as MDMObject).id!!)
                }
            }
        }
    }
})
