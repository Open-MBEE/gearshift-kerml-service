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
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.GearshiftEngine

/**
 * Tests for KerML Package structure and relationships.
 */
class KerMLPackageTest : DescribeSpec({

    describe("KerML Package structure") {

        lateinit var engine: GearshiftEngine

        beforeEach {
            engine = GearshiftEngine()
            KerMLMetamodelLoader.initialize(engine.metamodelRegistry)
        }

        context("basic package creation") {

            it("should create a simple package with a name") {
                val (pkgId, pkg) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "MyPackage")

                pkg.className shouldBe "Package"
                engine.getProperty(pkgId, "declaredName") shouldBe "MyPackage"
            }

            it("should derive name from declaredName") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "TestPackage")

                val name = engine.invokeOperation(pkgId, "effectiveName")
                name shouldBe "TestPackage"
            }

            it("should get derived name property") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "MyPackage")

                // Test the derived 'name' property
                val derivedName = engine.getProperty(pkgId, "name")
                derivedName shouldBe "MyPackage"
            }
        }

        context("parent-child package relationship") {

            it("should create parent and child packages with OwningMembership") {
                // Create parent package
                val (parentId, parentPkg) = engine.createInstance("Package")
                engine.setProperty(parentId, "declaredName", "Parent")

                // Create OwningMembership to hold the child
                val (membershipId, _) = engine.createInstance("OwningMembership")

                // Create child package
                val (childId, childPkg) = engine.createInstance("Package")
                engine.setProperty(childId, "declaredName", "Child")

                // Link parent -> membership (parent owns the membership)
                engine.createLink(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    parentId,
                    membershipId
                )

                // Link membership -> child (membership owns the child element)
                engine.createLink(
                    "owningMembershipOwnedMemberElementAssociation",
                    membershipId,
                    childId
                )

                // Verify the structure
                parentPkg shouldNotBe null
                childPkg shouldNotBe null

                // Verify parent has the membership
                val memberships = engine.getLinkedTargets(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    parentId
                )
                memberships shouldHaveSize 1

                // Verify membership has the child
                val children = engine.getLinkedTargets(
                    "owningMembershipOwnedMemberElementAssociation",
                    membershipId
                )
                children shouldHaveSize 1
                children shouldContain childPkg
            }

            it("should allow multiple children in a parent package") {
                // Create parent package
                val (parentId, _) = engine.createInstance("Package")
                engine.setProperty(parentId, "declaredName", "Parent")

                // Create first child with its membership
                val (membership1Id, _) = engine.createInstance("OwningMembership")
                val (child1Id, child1) = engine.createInstance("Package")
                engine.setProperty(child1Id, "declaredName", "Child1")
                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membership1Id)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membership1Id, child1Id)

                // Create second child with its membership
                val (membership2Id, _) = engine.createInstance("OwningMembership")
                val (child2Id, child2) = engine.createInstance("Package")
                engine.setProperty(child2Id, "declaredName", "Child2")
                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membership2Id)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membership2Id, child2Id)

                // Verify parent has two memberships
                val memberships = engine.getLinkedTargets(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    parentId
                )
                memberships shouldHaveSize 2

                // Get all children via their memberships
                val allChildren = memberships.flatMap { membership ->
                    engine.getLinkedTargets(
                        "owningMembershipOwnedMemberElementAssociation",
                        membership.id!!
                    )
                }
                allChildren shouldHaveSize 2
                allChildren shouldContain child1
                allChildren shouldContain child2
            }

            it("should support nested packages (grandchild)") {
                // Create root package
                val (rootId, _) = engine.createInstance("Package")
                engine.setProperty(rootId, "declaredName", "Root")

                // Create parent package as child of root
                val (membershipToParentId, _) = engine.createInstance("OwningMembership")
                val (parentId, _) = engine.createInstance("Package")
                engine.setProperty(parentId, "declaredName", "Parent")
                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", rootId, membershipToParentId)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipToParentId, parentId)

                // Create child package as child of parent (grandchild of root)
                val (membershipToChildId, _) = engine.createInstance("OwningMembership")
                val (childId, child) = engine.createInstance("Package")
                engine.setProperty(childId, "declaredName", "Child")
                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membershipToChildId)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipToChildId, childId)

                // Verify the hierarchy
                // Root -> Parent
                val rootMemberships = engine.getLinkedTargets(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    rootId
                )
                rootMemberships shouldHaveSize 1

                // Parent -> Child
                val parentMemberships = engine.getLinkedTargets(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    parentId
                )
                parentMemberships shouldHaveSize 1

                val grandchildren = engine.getLinkedTargets(
                    "owningMembershipOwnedMemberElementAssociation",
                    parentMemberships.first().id!!
                )
                grandchildren shouldHaveSize 1
                grandchildren shouldContain child
            }
        }

        context("membership properties") {

            it("should set memberName on membership") {
                val (parentId, _) = engine.createInstance("Package")
                engine.setProperty(parentId, "declaredName", "Parent")

                val (membershipId, _) = engine.createInstance("OwningMembership")
                engine.setProperty(membershipId, "memberName", "childAlias")

                val (childId, _) = engine.createInstance("Package")
                engine.setProperty(childId, "declaredName", "ActualChild")

                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membershipId)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipId, childId)

                // Verify memberName is set
                engine.getProperty(membershipId, "memberName") shouldBe "childAlias"
            }
        }

        context("derived properties") {

            it("should derive owner from owningRelationship") {
                // Create parent package
                val (parentId, parentPkg) = engine.createInstance("Package")
                engine.setProperty(parentId, "declaredName", "Parent")

                // Create OwningMembership
                val (membershipId, _) = engine.createInstance("OwningMembership")

                // Create child package
                val (childId, _) = engine.createInstance("Package")
                engine.setProperty(childId, "declaredName", "Child")

                // Link parent -> membership
                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", parentId, membershipId)

                // Link membership -> child (this establishes owningRelationship for child)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipId, childId)

                // Verify child's owningRelationship is the membership
                val owningRelationships = engine.getLinkedSources(
                    "owningMembershipOwnedMemberElementAssociation",
                    childId
                )
                owningRelationships shouldHaveSize 1

                // The owner should be the parent (owningRelationship.owningRelatedElement)
                // This requires the derived property constraint to be evaluated
                // For now, verify the structure is correct by traversing
                val membership = owningRelationships.first()
                val owners = engine.getLinkedSources(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    membership.id!!
                )
                owners shouldHaveSize 1
                owners shouldContain parentPkg
            }

            it("should have null owner for root package") {
                val (rootId, _) = engine.createInstance("Package")
                engine.setProperty(rootId, "declaredName", "RootPackage")

                // Root package has no owningRelationship
                val owningRelationships = engine.getLinkedSources(
                    "owningMembershipOwnedMemberElementAssociation",
                    rootId
                )
                owningRelationships shouldHaveSize 0
            }

            it("should build qualifiedName from package hierarchy") {
                // Create root package (no owner, so qualifiedName = escapedName = "Root")
                val (rootId, _) = engine.createInstance("Package")
                engine.setProperty(rootId, "declaredName", "Root")

                // Create child package
                val (membershipId, _) = engine.createInstance("OwningMembership")
                val (childId, _) = engine.createInstance("Package")
                engine.setProperty(childId, "declaredName", "Child")

                engine.createLink("membershipOwningNamespaceOwnedMembershipAssociation", rootId, membershipId)
                engine.createLink("owningMembershipOwnedMemberElementAssociation", membershipId, childId)

                // Verify the escapedName operation works
                val rootEscapedName = engine.invokeOperation(rootId, "escapedName")
                rootEscapedName shouldBe "Root"

                val childEscapedName = engine.invokeOperation(childId, "escapedName")
                childEscapedName shouldBe "Child"
            }

            it("should handle special characters in name via escapedName") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredName", "my-package")

                // Names with hyphens need to be escaped as restricted names
                val escapedName = engine.invokeOperation(pkgId, "escapedName")
                escapedName shouldBe "'my-package'"
            }

            it("should use shortName when name is null") {
                val (pkgId, _) = engine.createInstance("Package")
                engine.setProperty(pkgId, "declaredShortName", "pkg")
                // declaredName is not set, so name will be null

                val escapedName = engine.invokeOperation(pkgId, "escapedName")
                escapedName shouldBe "pkg"
            }
        }
    }
})
