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
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.GearshiftEngine

/**
 * Tests for KerML Element operations.
 */
class ElementOperationsTest : DescribeSpec({

    describe("Element operations") {

        lateinit var engine: GearshiftEngine

        beforeEach {
            engine = GearshiftEngine()
            KerMLMetamodelLoader.initialize(engine)
        }

        context("libraryNamespace operation") {

            it("should return null when element has no owningRelationship") {
                val (pkgId, _) = engine.createInstance("Package")

                val result = engine.invokeOperation(pkgId, "libraryNamespace")

                result shouldBe null
            }

            it("should return self when element is a LibraryPackage") {
                val (libPkgId, libPkg) = engine.createInstance("LibraryPackage")
                engine.setProperty(libPkgId, "isStandard", true)

                val result = engine.invokeOperation(libPkgId, "libraryNamespace")

                result shouldBe libPkg
            }
        }
    }
})
