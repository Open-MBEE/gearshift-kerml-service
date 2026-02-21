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
package org.openmbee.mdm.framework.runtime

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint

/**
 * Tests for #opposite sentinel derivation constraint on non-navigable association ends.
 */
class OppositeEndNavigationTest : DescribeSpec({

    describe("opposite end resolution with stored links") {

        it("should resolve non-navigable sourceEnd via graph reverse lookup") {
            val registry = MetamodelRegistry()

            val defClass = MetaClass(name = "ActionDefinition")
            val usageClass = MetaClass(name = "ActionUsage")
            registry.registerClass(defClass)
            registry.registerClass(usageClass)

            val assoc = MetaAssociation(
                name = "featuringDefActionAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "featuringDef",
                    type = "ActionDefinition",
                    lowerBound = 0,
                    upperBound = -1,
                    isNavigable = false,
                    isDerived = true,
                    derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                ),
                targetEnd = MetaAssociationEnd(
                    name = "action",
                    type = "ActionUsage",
                    lowerBound = 0,
                    upperBound = -1
                )
            )
            registry.registerAssociation(assoc)
            registry.buildIndexes()

            val engine = MDMEngine(registry)
            val (defId, _) = engine.createInstance("ActionDefinition")
            val (usageId, usage) = engine.createInstance("ActionUsage")

            // Store a forward link: ActionDefinition -> ActionUsage
            engine.createLink("featuringDefActionAssoc", defId, usageId)

            // Navigate the #opposite sourceEnd from ActionUsage → should find ActionDefinition
            val result = engine.getPropertyValue(usage, "featuringDef")

            @Suppress("UNCHECKED_CAST")
            val resultList = result as List<MDMObject>
            resultList shouldHaveSize 1
            resultList[0].id shouldBe defId
        }

        it("should resolve multiple opposite results") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(name = "Definition"))
            registry.registerClass(MetaClass(name = "Usage"))

            val assoc = MetaAssociation(
                name = "defUsageAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "owningDef",
                    type = "Definition",
                    lowerBound = 0,
                    upperBound = -1,
                    isNavigable = false,
                    isDerived = true,
                    derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                ),
                targetEnd = MetaAssociationEnd(
                    name = "usage",
                    type = "Usage",
                    lowerBound = 0,
                    upperBound = -1
                )
            )
            registry.registerAssociation(assoc)
            registry.buildIndexes()

            val engine = MDMEngine(registry)
            val (def1Id, _) = engine.createInstance("Definition")
            val (def2Id, _) = engine.createInstance("Definition")
            val (usageId, usage) = engine.createInstance("Usage")

            engine.createLink("defUsageAssoc", def1Id, usageId)
            engine.createLink("defUsageAssoc", def2Id, usageId)

            @Suppress("UNCHECKED_CAST")
            val result = engine.getPropertyValue(usage, "owningDef") as List<MDMObject>
            result shouldHaveSize 2
            result.map { it.id } shouldContainExactlyInAnyOrder listOf(def1Id, def2Id)
        }

        it("should return empty list when no links exist") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(name = "A"))
            registry.registerClass(MetaClass(name = "B"))

            val assoc = MetaAssociation(
                name = "abAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "aEnd",
                    type = "A",
                    lowerBound = 0,
                    upperBound = -1,
                    isNavigable = false,
                    isDerived = true,
                    derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                ),
                targetEnd = MetaAssociationEnd(
                    name = "bEnd",
                    type = "B",
                    lowerBound = 0,
                    upperBound = -1
                )
            )
            registry.registerAssociation(assoc)
            registry.buildIndexes()

            val engine = MDMEngine(registry)
            val (_, b) = engine.createInstance("B")

            @Suppress("UNCHECKED_CAST")
            val result = engine.getPropertyValue(b, "aEnd") as List<MDMObject>
            result.shouldBeEmpty()
        }
    }

    describe("opposite end resolution with derived opposite end") {

        it("should resolve via instance scanning when opposite end is derived") {
            val registry = MetamodelRegistry()

            // Parent owns children via a derived property computed by OCL
            val parentClass = MetaClass(
                name = "Parent",
                constraints = listOf(
                    MetaConstraint(
                        name = "deriveParentChild",
                        type = ConstraintType.DERIVATION,
                        expression = "ownedMember->select(m | m.oclIsKindOf(Child))"
                    )
                )
            )
            val childClass = MetaClass(
                name = "Child",
                superclasses = listOf("Parent")
            )
            val memberClass = MetaClass(name = "Member")

            registry.registerClass(parentClass)
            registry.registerClass(childClass)
            registry.registerClass(memberClass)

            // ownedMember stored association
            val ownedMemberAssoc = MetaAssociation(
                name = "parentMemberAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "owningParent",
                    type = "Parent",
                    lowerBound = 0,
                    upperBound = 1
                ),
                targetEnd = MetaAssociationEnd(
                    name = "ownedMember",
                    type = "Member",
                    lowerBound = 0,
                    upperBound = -1
                )
            )

            // The association with #opposite sourceEnd and derived targetEnd
            val derivedAssoc = MetaAssociation(
                name = "owningParentChildAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "owningParentOfChild",
                    type = "Parent",
                    lowerBound = 0,
                    upperBound = -1,
                    isNavigable = false,
                    isDerived = true,
                    derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                ),
                targetEnd = MetaAssociationEnd(
                    name = "childDerived",
                    type = "Child",
                    lowerBound = 0,
                    upperBound = -1,
                    isDerived = true,
                    derivationConstraint = "deriveParentChild"
                )
            )

            registry.registerAssociation(ownedMemberAssoc)
            registry.registerAssociation(derivedAssoc)
            registry.buildIndexes()

            val engine = MDMEngine(registry)
            val (parentId, _) = engine.createInstance("Parent")
            val (childId, child) = engine.createInstance("Child")

            // Store parent -> child as ownedMember
            engine.createLink("parentMemberAssoc", parentId, childId)

            // Navigate the #opposite sourceEnd: from Child -> Parent
            // Since the targetEnd "childDerived" is derived, the slow path scans instances
            @Suppress("UNCHECKED_CAST")
            val result = engine.getPropertyValue(child, "owningParentOfChild") as List<MDMObject>
            result shouldHaveSize 1
            result[0].id shouldBe parentId
        }
    }

    describe("opposite end caching") {

        it("should cache results in derivedCache") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(name = "Source"))
            registry.registerClass(MetaClass(name = "Target"))

            val assoc = MetaAssociation(
                name = "stAssoc",
                sourceEnd = MetaAssociationEnd(
                    name = "sourceEnd",
                    type = "Source",
                    lowerBound = 0,
                    upperBound = -1,
                    isNavigable = false,
                    isDerived = true,
                    derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                ),
                targetEnd = MetaAssociationEnd(
                    name = "targetEnd",
                    type = "Target",
                    lowerBound = 0,
                    upperBound = -1
                )
            )
            registry.registerAssociation(assoc)
            registry.buildIndexes()

            val engine = MDMEngine(registry)
            val (srcId, _) = engine.createInstance("Source")
            val (tgtId, tgt) = engine.createInstance("Target")
            engine.createLink("stAssoc", srcId, tgtId)

            // First call populates cache
            engine.getPropertyValue(tgt, "sourceEnd")
            tgt.derivedCache.containsKey("assoc:sourceEnd") shouldBe true

            // Second call returns cached value
            @Suppress("UNCHECKED_CAST")
            val result = engine.getPropertyValue(tgt, "sourceEnd") as List<MDMObject>
            result shouldHaveSize 1
        }
    }

    describe("opposite end validation") {

        it("should reject #opposite on a navigable end") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(name = "A"))
            registry.registerClass(MetaClass(name = "B"))

            shouldThrow<IllegalArgumentException> {
                registry.registerAssociation(
                    MetaAssociation(
                        name = "badAssoc",
                        sourceEnd = MetaAssociationEnd(
                            name = "aEnd",
                            type = "A",
                            isNavigable = true,
                            isDerived = true,
                            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                        ),
                        targetEnd = MetaAssociationEnd(
                            name = "bEnd",
                            type = "B"
                        )
                    )
                )
            }.message shouldBe "Association 'badAssoc': #opposite must not be used on a navigable end (sourceEnd 'aEnd')"
        }

        it("should reject #opposite on both ends") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(name = "A"))
            registry.registerClass(MetaClass(name = "B"))

            shouldThrow<IllegalArgumentException> {
                registry.registerAssociation(
                    MetaAssociation(
                        name = "badAssoc",
                        sourceEnd = MetaAssociationEnd(
                            name = "aEnd",
                            type = "A",
                            isNavigable = false,
                            isDerived = true,
                            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                        ),
                        targetEnd = MetaAssociationEnd(
                            name = "bEnd",
                            type = "B",
                            isNavigable = false,
                            isDerived = true,
                            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
                        )
                    )
                )
            }.message shouldBe "Association 'badAssoc': both ends cannot use #opposite (would create circular derivation)"
        }
    }
})
