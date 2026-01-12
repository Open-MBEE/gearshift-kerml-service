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
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.GearshiftEngine

/**
 * Tests for GearshiftEngine with the KerML metamodel.
 * Focuses on MDM basics: property retrieval, association traversal, and operation invocation.
 */
class KerMLGearshiftEngineTest : DescribeSpec({

    describe("GearshiftEngine with KerML metamodel") {

        lateinit var engine: GearshiftEngine

        beforeEach {
            engine = GearshiftEngine()
            KerMLMetamodelLoader.initialize(engine)
        }

        context("metamodel initialization") {

            it("should load all KerML metaclasses") {
                val stats = KerMLMetamodelLoader.getStatistics(engine)
                stats["total"] shouldNotBe 0
                stats["root"] shouldNotBe 0
                stats["core"] shouldNotBe 0
                stats["kernel"] shouldNotBe 0
            }

            it("should register associations") {
                val associations = engine.metamodelRegistry.getAllAssociations()
                associations.size shouldNotBe 0
            }

            it("should validate metamodel without errors") {
                val errors = engine.validateMetamodel()
                errors.shouldBeEmpty()
            }
        }

        context("property retrieval - direct properties") {

            it("should set and get properties on Package") {
                val (pkgId, pkg) = engine.createInstance("Package")

                engine.setProperty(pkgId, "elementId", "pkg-001")
                engine.setProperty(pkgId, "declaredName", "MyPackage")

                engine.getProperty(pkgId, "elementId") shouldBe "pkg-001"
                engine.getProperty(pkgId, "declaredName") shouldBe "MyPackage"
            }

            it("should set and get properties on Comment") {
                val (commentId, _) = engine.createInstance("Comment")

                engine.setProperty(commentId, "body", "This is a test comment")
                engine.setProperty(commentId, "locale", "en-US")
                engine.setProperty(commentId, "elementId", "comment-001")

                engine.getProperty(commentId, "body") shouldBe "This is a test comment"
                engine.getProperty(commentId, "locale") shouldBe "en-US"
                engine.getProperty(commentId, "elementId") shouldBe "comment-001"
            }

            it("should set and get properties on Namespace") {
                val (nsId, _) = engine.createInstance("Namespace")

                engine.setProperty(nsId, "elementId", "ns-001")
                engine.setProperty(nsId, "declaredName", "MyNamespace")

                engine.getProperty(nsId, "elementId") shouldBe "ns-001"
                engine.getProperty(nsId, "declaredName") shouldBe "MyNamespace"
            }
        }

        context("property retrieval - inherited properties") {

            it("should access Element properties from Package (via Namespace)") {
                // Package -> Namespace -> Element
                val (pkgId, _) = engine.createInstance("Package")

                // These are inherited from Element
                engine.setProperty(pkgId, "elementId", "pkg-inherited-001")
                engine.setProperty(pkgId, "declaredName", "InheritedPackage")
                engine.setProperty(pkgId, "declaredShortName", "IP")
                engine.setProperty(pkgId, "isImpliedIncluded", false)

                engine.getProperty(pkgId, "elementId") shouldBe "pkg-inherited-001"
                engine.getProperty(pkgId, "declaredName") shouldBe "InheritedPackage"
                engine.getProperty(pkgId, "declaredShortName") shouldBe "IP"
                engine.getProperty(pkgId, "isImpliedIncluded") shouldBe false
            }

            it("should access Element properties from Comment (via AnnotatingElement)") {
                // Comment -> AnnotatingElement -> Element
                val (commentId, _) = engine.createInstance("Comment")

                // Direct property
                engine.setProperty(commentId, "body", "A comment")

                // Inherited from Element via AnnotatingElement
                engine.setProperty(commentId, "elementId", "comment-inherited-001")
                engine.setProperty(commentId, "declaredName", "MyComment")

                engine.getProperty(commentId, "body") shouldBe "A comment"
                engine.getProperty(commentId, "elementId") shouldBe "comment-inherited-001"
                engine.getProperty(commentId, "declaredName") shouldBe "MyComment"
            }

            it("should handle multi-valued properties") {
                val (pkgId, _) = engine.createInstance("Package")

                val aliases = listOf("alias1", "alias2", "alias3")
                engine.setProperty(pkgId, "aliasIds", aliases)

                val retrieved = engine.getProperty(pkgId, "aliasIds")
                retrieved shouldBe aliases
            }
        }

        context("association traversal") {

            it("should store and retrieve object references as property values") {
                // Create a Package (which is an Element)
                val (pkgId, pkg) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "ParentPackage")

                // Create a Membership (which is a Relationship)
                val (membershipId, membership) = engine.createInstance("Membership")
                engine.setProperty(membershipId, "elementId", "membership-001")

                // Store direct object reference - simulates what association traversal would do
                // when association ends are properly implemented
                membership.setProperty("_owner", pkg)
                pkg.setProperty("_ownedRelationships", listOf(membership))

                // Retrieve the stored references
                val owner = membership.getProperty("_owner")
                owner shouldBe pkg

                val ownedRels = pkg.getProperty("_ownedRelationships") as? List<*>
                ownedRels shouldNotBe null
                ownedRels!! shouldHaveSize 1
                ownedRels shouldContain membership
            }

            it("should verify associations are registered in the metamodel") {
                val associations = engine.metamodelRegistry.getAllAssociations()

                // Verify key element associations exist
                val assocNames = associations.map { it.name }
                assocNames shouldContain "ownedElementOwnerAssociation"
                assocNames shouldContain "relationshipRelatedElementAssociation"
            }

            it("should get association by name") {
                val ownershipAssoc = engine.metamodelRegistry.getAssociation("ownedElementOwnerAssociation")

                ownershipAssoc shouldNotBe null
                ownershipAssoc!!.sourceEnd.name shouldBe "ownedElement"
                ownershipAssoc.targetEnd.name shouldBe "owner"
            }
        }

        context("operation invocation") {

            it("should invoke effectiveName on Package") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "TestPackage")

                val effectiveName = engine.invokeOperation(pkgId, "effectiveName")
                effectiveName shouldBe "TestPackage"
            }

            it("should invoke effectiveName on Namespace") {
                val (nsId, _) = engine.createInstance("Namespace")
                engine.setProperty(nsId, "declaredName", "TestNamespace")

                val effectiveName = engine.invokeOperation(nsId, "effectiveName")
                effectiveName shouldBe "TestNamespace"
            }

            it("should invoke effectiveName on Comment") {
                val (commentId, _) = engine.createInstance("Comment")
                engine.setProperty(commentId, "declaredName", "TestComment")

                val effectiveName = engine.invokeOperation(commentId, "effectiveName")
                effectiveName shouldBe "TestComment"
            }

            it("should return null for effectiveName when declaredName is not set") {
                val (pkgId, _) = engine.createInstance("Package")
                // Don't set declaredName

                val effectiveName = engine.invokeOperation(pkgId, "effectiveName")
                effectiveName shouldBe null
            }

            it("should invoke inherited operations from Element on subclasses") {
                // Package inherits effectiveName from Element via Namespace
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "InheritedOpTest")

                // This operation is defined on Element, inherited through Namespace
                val result = engine.invokeOperation(pkgId, "effectiveName")
                result shouldBe "InheritedOpTest"
            }
        }

        context("instance validation") {

            it("should validate Package instance with required properties") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "elementId", "pkg-valid-001")
                engine.setProperty(pkgId, "isImpliedIncluded", false)

                val errors = engine.validateInstance(pkgId)
                errors.shouldBeEmpty()
            }

            it("should report missing required properties") {
                val (pkgId, _) = engine.createInstance("Package")
                // Don't set elementId (required) or isImpliedIncluded (required)

                val errors = engine.validateInstance(pkgId)
                errors.size shouldNotBe 0
            }
        }

        context("querying instances") {

            it("should find instances by type") {
                engine.createInstance("Package")
                engine.createInstance("Package")
                engine.createInstance("Namespace")
                engine.createInstance("Comment")

                val packages = engine.getInstancesByType("Package")
                packages shouldHaveSize 2

                val namespaces = engine.getInstancesByType("Namespace")
                namespaces shouldHaveSize 1

                val comments = engine.getInstancesByType("Comment")
                comments shouldHaveSize 1
            }

            it("should find instances by property value") {
                val (pkg1Id, _) = engine.createInstance("Package")
                val (pkg2Id, _) = engine.createInstance("Package")
                val (pkg3Id, _) = engine.createInstance("Package")

                engine.setProperty(pkg1Id, "declaredName", "Alpha")
                engine.setProperty(pkg2Id, "declaredName", "Beta")
                engine.setProperty(pkg3Id, "declaredName", "Alpha")

                val alphaPackages = engine.getInstancesByProperty("declaredName", "Alpha")
                alphaPackages shouldHaveSize 2
            }
        }

        context("abstract class handling") {

            it("should prevent instantiation of abstract Element") {
                try {
                    engine.createInstance("Element")
                    throw AssertionError("Should have thrown exception for abstract class")
                } catch (e: IllegalArgumentException) {
                    e.message shouldBe "Cannot instantiate abstract class: Element"
                }
            }

            it("should prevent instantiation of abstract Relationship") {
                try {
                    engine.createInstance("Relationship")
                    throw AssertionError("Should have thrown exception for abstract class")
                } catch (e: IllegalArgumentException) {
                    e.message shouldBe "Cannot instantiate abstract class: Relationship"
                }
            }
        }

        context("inheritance hierarchy") {

            it("should verify Package inheritance chain") {
                val registry = engine.metamodelRegistry

                registry.isSubclassOf("Package", "Namespace") shouldBe true
                registry.isSubclassOf("Package", "Element") shouldBe true
            }

            it("should verify Comment inheritance chain") {
                val registry = engine.metamodelRegistry

                registry.isSubclassOf("Comment", "AnnotatingElement") shouldBe true
                registry.isSubclassOf("Comment", "Element") shouldBe true
            }

            it("should get all superclasses transitively") {
                val registry = engine.metamodelRegistry

                val packageSuperclasses = registry.getAllSuperclasses("Package")
                packageSuperclasses shouldContain "Namespace"
                packageSuperclasses shouldContain "Element"

                val commentSuperclasses = registry.getAllSuperclasses("Comment")
                commentSuperclasses shouldContain "AnnotatingElement"
                commentSuperclasses shouldContain "Element"
            }
        }
    }
})
