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
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.framework.meta.AggregationKind
import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaAssociationEnd
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.storage.LinkRepository
import org.openmbee.gearshift.framework.storage.ModelRepository

/**
 * Tests for link (association instance) management in MDMEngine.
 * Validates the graph model where:
 * - MDMObject instances are nodes
 * - MDMLink instances are edges
 * - MetaAssociation defines edge types
 */
class LinkManagementTest : DescribeSpec({

    // Helper to create a test engine with common setup
    fun createTestEngine(): Triple<MDMEngine, ModelRepository, LinkRepository> {
        val registry = MetamodelRegistry()
        val objectRepo = ModelRepository()
        val linkRepo = LinkRepository()

        // Register test classes
        registry.registerClass(MetaClass(name = "Element"))
        registry.registerClass(MetaClass(name = "Namespace", superclasses = listOf("Element")))
        registry.registerClass(MetaClass(name = "Package", superclasses = listOf("Namespace")))
        registry.registerClass(MetaClass(name = "Feature", superclasses = listOf("Element")))

        // Register test association: owner <-> ownedElement
        // Note: sourceEnd upperBound = -1 allows multiple sources (owners) per target
        registry.registerAssociation(
            MetaAssociation(
                name = "OwnershipAssociation",
                sourceEnd = MetaAssociationEnd(
                    name = "owner",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1, // unbounded - allows multiple owners for testing
                    isNavigable = true
                ),
                targetEnd = MetaAssociationEnd(
                    name = "ownedElement",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1, // unbounded
                    isNavigable = true,
                    aggregation = AggregationKind.COMPOSITE
                )
            )
        )

        // Register association with multiplicity 1 (single target)
        registry.registerAssociation(
            MetaAssociation(
                name = "SingleTargetAssociation",
                sourceEnd = MetaAssociationEnd(
                    name = "source",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1
                ),
                targetEnd = MetaAssociationEnd(
                    name = "target",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = 1 // max 1 target per source
                )
            )
        )

        val engine = MDMEngine(registry, objectRepo, linkRepo)
        return Triple(engine, objectRepo, linkRepo)
    }

    describe("MDMEngine link management") {

        context("creating links") {

            it("should create a link between two instances") {
                val (engine, objectRepo, linkRepo) = createTestEngine()

                // Create instances
                val owner = engine.createInstance("Namespace")
                val ownerId = "owner-1"
                objectRepo.store(ownerId, owner)

                val child = engine.createInstance("Element")
                val childId = "child-1"
                objectRepo.store(childId, child)

                // Create link
                val link = engine.createLink("OwnershipAssociation", ownerId, childId)

                link shouldNotBe null
                link.associationName shouldBe "OwnershipAssociation"
                link.sourceId shouldBe ownerId
                link.targetId shouldBe childId
            }

            it("should throw when creating link with unknown association") {
                val (engine, objectRepo, _) = createTestEngine()

                val elem1 = engine.createInstance("Element")
                objectRepo.store("elem1", elem1)
                val elem2 = engine.createInstance("Element")
                objectRepo.store("elem2", elem2)

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.createLink("NonExistentAssociation", "elem1", "elem2")
                }

                exception.message shouldContain "Unknown association"
            }

            it("should throw when source instance not found") {
                val (engine, objectRepo, _) = createTestEngine()

                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.createLink("OwnershipAssociation", "nonexistent", "elem")
                }

                exception.message shouldContain "Source instance not found"
            }

            it("should throw when target instance not found") {
                val (engine, objectRepo, _) = createTestEngine()

                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)

                val exception = shouldThrow<IllegalArgumentException> {
                    engine.createLink("OwnershipAssociation", "elem", "nonexistent")
                }

                exception.message shouldContain "Target instance not found"
            }

            it("should throw when creating duplicate link") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)
                val child = engine.createInstance("Element")
                objectRepo.store("child", child)

                engine.createLink("OwnershipAssociation", "owner", "child")

                val exception = shouldThrow<IllegalStateException> {
                    engine.createLink("OwnershipAssociation", "owner", "child")
                }

                exception.message shouldContain "Link already exists"
            }
        }

        context("type validation") {

            it("should accept link with matching types") {
                val (engine, objectRepo, _) = createTestEngine()

                val ns = engine.createInstance("Namespace")
                objectRepo.store("ns", ns)
                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)

                // Namespace is a subclass of Element, so it's valid for an Element-typed end
                val link = engine.createLink("OwnershipAssociation", "ns", "elem")

                link shouldNotBe null
            }

            it("should accept link with subclass types") {
                val (engine, objectRepo, _) = createTestEngine()

                // Package extends Namespace extends Element
                val pkg = engine.createInstance("Package")
                objectRepo.store("pkg", pkg)
                val feature = engine.createInstance("Feature")
                objectRepo.store("feature", feature)

                // Both are subtypes of Element, so valid
                val link = engine.createLink("OwnershipAssociation", "pkg", "feature")

                link shouldNotBe null
            }
        }

        context("multiplicity validation") {

            it("should enforce upper bound on target end") {
                val (engine, objectRepo, _) = createTestEngine()

                val source = engine.createInstance("Element")
                objectRepo.store("source", source)
                val target1 = engine.createInstance("Element")
                objectRepo.store("target1", target1)
                val target2 = engine.createInstance("Element")
                objectRepo.store("target2", target2)

                // First link should succeed
                engine.createLink("SingleTargetAssociation", "source", "target1")

                // Second link should fail (upperBound = 1 on target end)
                val exception = shouldThrow<IllegalStateException> {
                    engine.createLink("SingleTargetAssociation", "source", "target2")
                }

                exception.message shouldContain "already has maximum"
            }

            it("should allow multiple targets when unbounded") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)

                // Create multiple children
                for (i in 1..5) {
                    val child = engine.createInstance("Element")
                    objectRepo.store("child$i", child)
                    engine.createLink("OwnershipAssociation", "owner", "child$i")
                }

                val links = engine.getOutgoingLinks("owner")
                links shouldHaveSize 5
            }
        }

        context("traversing links") {

            it("should get linked targets from source") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)
                val child1 = engine.createInstance("Element")
                objectRepo.store("child1", child1)
                val child2 = engine.createInstance("Element")
                objectRepo.store("child2", child2)

                engine.createLink("OwnershipAssociation", "owner", "child1")
                engine.createLink("OwnershipAssociation", "owner", "child2")

                val targets = engine.getLinkedTargets("OwnershipAssociation", "owner")

                targets shouldHaveSize 2
            }

            it("should get linked sources from target") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner1 = engine.createInstance("Namespace")
                objectRepo.store("owner1", owner1)
                val owner2 = engine.createInstance("Namespace")
                objectRepo.store("owner2", owner2)
                val child = engine.createInstance("Element")
                objectRepo.store("child", child)

                engine.createLink("OwnershipAssociation", "owner1", "child")
                engine.createLink("OwnershipAssociation", "owner2", "child")

                val sources = engine.getLinkedSources("OwnershipAssociation", "child")

                sources shouldHaveSize 2
            }

            it("should get all links for an instance") {
                val (engine, objectRepo, _) = createTestEngine()

                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)
                val other1 = engine.createInstance("Element")
                objectRepo.store("other1", other1)
                val other2 = engine.createInstance("Element")
                objectRepo.store("other2", other2)

                // elem as source
                engine.createLink("OwnershipAssociation", "elem", "other1")
                // elem as target
                engine.createLink("OwnershipAssociation", "other2", "elem")

                val allLinks = engine.getLinks("elem")
                allLinks shouldHaveSize 2
            }
        }

        context("removing links") {

            it("should remove a specific link") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)
                val child = engine.createInstance("Element")
                objectRepo.store("child", child)

                engine.createLink("OwnershipAssociation", "owner", "child")

                val removed = engine.removeLink("OwnershipAssociation", "owner", "child")

                removed shouldBe true
                engine.getLinks("owner").shouldBeEmpty()
            }

            it("should return false when link not found") {
                val (engine, objectRepo, _) = createTestEngine()

                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)

                val removed = engine.removeLink("OwnershipAssociation", "elem", "nonexistent")

                removed shouldBe false
            }

            it("should remove all links for an instance") {
                val (engine, objectRepo, _) = createTestEngine()

                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)
                val child1 = engine.createInstance("Element")
                objectRepo.store("child1", child1)
                val child2 = engine.createInstance("Element")
                objectRepo.store("child2", child2)

                engine.createLink("OwnershipAssociation", "owner", "child1")
                engine.createLink("OwnershipAssociation", "owner", "child2")

                val removed = engine.removeAllLinks("owner")

                removed shouldHaveSize 2
                engine.getLinks("owner").shouldBeEmpty()
            }
        }

        context("composite aggregation cascade delete") {

            it("should cascade delete composite parts") {
                val (engine, objectRepo, _) = createTestEngine()

                // Create owner with nested structure
                val owner = engine.createInstance("Namespace")
                objectRepo.store("owner", owner)
                val child1 = engine.createInstance("Element")
                objectRepo.store("child1", child1)
                val child2 = engine.createInstance("Element")
                objectRepo.store("child2", child2)
                val grandchild = engine.createInstance("Element")
                objectRepo.store("grandchild", grandchild)

                // owner owns child1, child2
                engine.createLink("OwnershipAssociation", "owner", "child1")
                engine.createLink("OwnershipAssociation", "owner", "child2")
                // child1 owns grandchild
                engine.createLink("OwnershipAssociation", "child1", "grandchild")

                // Delete owner - should cascade to children and grandchild
                val deleted = engine.deleteInstanceWithCascade("owner")

                deleted shouldContainExactlyInAnyOrder listOf("owner", "child1", "child2", "grandchild")

                // All instances should be deleted
                objectRepo.get("owner") shouldBe null
                objectRepo.get("child1") shouldBe null
                objectRepo.get("child2") shouldBe null
                objectRepo.get("grandchild") shouldBe null
            }

            it("should not cascade non-composite associations") {
                val registry = MetamodelRegistry()
                val objectRepo = ModelRepository()
                val linkRepo = LinkRepository()

                registry.registerClass(MetaClass(name = "Element"))

                // Non-composite association (SHARED aggregation)
                registry.registerAssociation(
                    MetaAssociation(
                        name = "SharedAssociation",
                        sourceEnd = MetaAssociationEnd(
                            name = "referrer",
                            type = "Element",
                            lowerBound = 0,
                            upperBound = -1
                        ),
                        targetEnd = MetaAssociationEnd(
                            name = "referred",
                            type = "Element",
                            lowerBound = 0,
                            upperBound = -1,
                            aggregation = AggregationKind.SHARED // not composite
                        )
                    )
                )

                val engine = MDMEngine(registry, objectRepo, linkRepo)

                val elem1 = engine.createInstance("Element")
                objectRepo.store("elem1", elem1)
                val elem2 = engine.createInstance("Element")
                objectRepo.store("elem2", elem2)

                engine.createLink("SharedAssociation", "elem1", "elem2")

                // Delete elem1 - should NOT cascade to elem2
                val deleted = engine.deleteInstanceWithCascade("elem1")

                deleted shouldContainExactlyInAnyOrder listOf("elem1")
                objectRepo.get("elem2") shouldNotBe null // elem2 still exists
            }
        }

        context("link validation") {

            it("should validate required associations") {
                val registry = MetamodelRegistry()
                val objectRepo = ModelRepository()
                val linkRepo = LinkRepository()

                registry.registerClass(MetaClass(name = "Element"))

                // Association where target is required (lowerBound = 1)
                registry.registerAssociation(
                    MetaAssociation(
                        name = "RequiredTargetAssociation",
                        sourceEnd = MetaAssociationEnd(
                            name = "source",
                            type = "Element",
                            lowerBound = 0,
                            upperBound = -1
                        ),
                        targetEnd = MetaAssociationEnd(
                            name = "requiredTarget",
                            type = "Element",
                            lowerBound = 1, // required
                            upperBound = 1
                        )
                    )
                )

                val engine = MDMEngine(registry, objectRepo, linkRepo)

                val elem = engine.createInstance("Element")
                objectRepo.store("elem", elem)

                // Instance has no link, but association requires one
                val errors = engine.validateLinks("elem")

                errors shouldHaveSize 1
                errors.first() shouldContain "requires at least 1 target"
            }

            it("should pass validation when required links present") {
                val registry = MetamodelRegistry()
                val objectRepo = ModelRepository()
                val linkRepo = LinkRepository()

                registry.registerClass(MetaClass(name = "Element"))

                registry.registerAssociation(
                    MetaAssociation(
                        name = "RequiredTargetAssociation",
                        sourceEnd = MetaAssociationEnd(
                            name = "source",
                            type = "Element",
                            lowerBound = 0,
                            upperBound = -1
                        ),
                        targetEnd = MetaAssociationEnd(
                            name = "requiredTarget",
                            type = "Element",
                            lowerBound = 1,
                            upperBound = 1
                        )
                    )
                )

                val engine = MDMEngine(registry, objectRepo, linkRepo)

                val source = engine.createInstance("Element")
                objectRepo.store("source", source)
                val target = engine.createInstance("Element")
                objectRepo.store("target", target)

                engine.createLink("RequiredTargetAssociation", "source", "target")

                val errors = engine.validateLinks("source")

                errors.shouldBeEmpty()
            }
        }
    }

    describe("LinkRepository") {

        it("should index links by association") {
            val linkRepo = LinkRepository()
            val registry = MetamodelRegistry()

            registry.registerClass(MetaClass(name = "Element"))
            val assoc = MetaAssociation(
                name = "TestAssoc",
                sourceEnd = MetaAssociationEnd(name = "src", type = "Element"),
                targetEnd = MetaAssociationEnd(name = "tgt", type = "Element")
            )
            registry.registerAssociation(assoc)

            val link1 = MDMLink("link1", assoc, "a", "b")
            val link2 = MDMLink("link2", assoc, "c", "d")

            linkRepo.store(link1)
            linkRepo.store(link2)

            val links = linkRepo.getByAssociation("TestAssoc")
            links shouldHaveSize 2
        }

        it("should find link between specific instances") {
            val linkRepo = LinkRepository()
            val registry = MetamodelRegistry()

            registry.registerClass(MetaClass(name = "Element"))
            val assoc = MetaAssociation(
                name = "TestAssoc",
                sourceEnd = MetaAssociationEnd(name = "src", type = "Element"),
                targetEnd = MetaAssociationEnd(name = "tgt", type = "Element")
            )

            val link = MDMLink("link1", assoc, "source", "target")
            linkRepo.store(link)

            val found = linkRepo.findLink("TestAssoc", "source", "target")

            found shouldNotBe null
            found?.id shouldBe "link1"
        }

        it("should provide accurate statistics") {
            val linkRepo = LinkRepository()
            val assoc1 = MetaAssociation(
                name = "Assoc1",
                sourceEnd = MetaAssociationEnd(name = "src", type = "Element"),
                targetEnd = MetaAssociationEnd(name = "tgt", type = "Element")
            )
            val assoc2 = MetaAssociation(
                name = "Assoc2",
                sourceEnd = MetaAssociationEnd(name = "src", type = "Element"),
                targetEnd = MetaAssociationEnd(name = "tgt", type = "Element")
            )

            linkRepo.store(MDMLink("1", assoc1, "a", "b"))
            linkRepo.store(MDMLink("2", assoc1, "c", "d"))
            linkRepo.store(MDMLink("3", assoc2, "e", "f"))

            val stats = linkRepo.getStatistics()

            stats.totalLinks shouldBe 3
            stats.associationDistribution["Assoc1"] shouldBe 2
            stats.associationDistribution["Assoc2"] shouldBe 1
        }
    }
})
