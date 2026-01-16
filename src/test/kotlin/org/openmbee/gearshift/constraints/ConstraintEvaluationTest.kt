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
package org.openmbee.gearshift.constraints

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.engine.MDMEngine
import org.openmbee.gearshift.engine.MetamodelRegistry
import org.openmbee.gearshift.metamodel.AggregationKind
import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.metamodel.MetaAssociationEnd
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty
import org.openmbee.gearshift.repository.LinkRepository
import org.openmbee.gearshift.repository.ModelRepository

/**
 * Tests for constraint evaluation system.
 * Validates:
 * - Derived property evaluation
 * - Validation constraint evaluation
 * - Association end derivation
 */
class ConstraintEvaluationTest : DescribeSpec({

    fun createTestEngine(): MDMEngine {
        val registry = MetamodelRegistry()
        val objectRepo = ModelRepository()
        val linkRepo = LinkRepository()

        // Register test classes
        registry.registerClass(MetaClass(
            name = "Element",
            attributes = listOf(
                MetaProperty(name = "name", type = "String", lowerBound = 0),
                MetaProperty(name = "documentation", type = "Documentation", lowerBound = 0, upperBound = -1, isDerived = true)
            )
        ))
        registry.registerClass(MetaClass(name = "Documentation", superclasses = listOf("Element")))
        registry.registerClass(MetaClass(name = "Comment", superclasses = listOf("Element")))
        registry.registerClass(MetaClass(
            name = "Annotation",
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(name = "ownedAnnotatingElement", type = "AnnotatingElement", lowerBound = 0, upperBound = 1),
                MetaProperty(name = "owningAnnotatingElement", type = "AnnotatingElement", lowerBound = 0, upperBound = 1)
            )
        ))
        registry.registerClass(MetaClass(name = "AnnotatingElement", superclasses = listOf("Element")))

        // Register ownedElement association
        registry.registerAssociation(
            MetaAssociation(
                name = "ownedElementOwnerAssociation",
                sourceEnd = MetaAssociationEnd(
                    name = "owner",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = 1
                ),
                targetEnd = MetaAssociationEnd(
                    name = "ownedElement",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1,
                    aggregation = AggregationKind.COMPOSITE
                )
            )
        )

        return MDMEngine(registry, objectRepo, linkRepo)
    }

    describe("Derived Property Evaluation") {

        context("registered evaluators") {

            it("should evaluate derived property using registered evaluator") {
                val engine = createTestEngine()

                // Register derived property evaluator for Element.documentation
                // documentation = ownedElement->selectByKind(Documentation)
                engine.constraintRegistry.register {
                    derivedProperty("Element", "documentation") { ctx ->
                        ctx.getLinkedTargets("ownedElementOwnerAssociation")
                            .filter { ctx.isKindOf(it, "Documentation") }
                    }
                }

                // Create element with owned documentation
                val owner = engine.createInstance("Element")
                engine.objectRepository.store("owner", owner)

                val doc1 = engine.createInstance("Documentation")
                engine.objectRepository.store("doc1", doc1)
                engine.setProperty(doc1, "name", "Doc 1")

                val doc2 = engine.createInstance("Documentation")
                engine.objectRepository.store("doc2", doc2)
                engine.setProperty(doc2, "name", "Doc 2")

                val comment = engine.createInstance("Comment")
                engine.objectRepository.store("comment", comment)

                // Link owner to children
                engine.createLink("ownedElementOwnerAssociation", "owner", "doc1")
                engine.createLink("ownedElementOwnerAssociation", "owner", "doc2")
                engine.createLink("ownedElementOwnerAssociation", "owner", "comment")

                // Evaluate derived property
                val documentation = engine.getProperty(owner, "documentation")

                documentation shouldNotBe null
                (documentation as List<*>) shouldHaveSize 2
            }

            it("should inherit evaluators from superclass") {
                val engine = createTestEngine()

                // Register evaluator on Element
                engine.constraintRegistry.register {
                    derivedProperty("Element", "documentation") { ctx ->
                        ctx.getLinkedTargets("ownedElementOwnerAssociation")
                            .filter { ctx.isKindOf(it, "Documentation") }
                    }
                }

                // Create Documentation (subclass of Element)
                val doc = engine.createInstance("Documentation")
                engine.objectRepository.store("doc", doc)

                // Should find evaluator via inheritance
                val hasEvaluator = engine.constraintEngine.hasDerivedPropertyEvaluator("Documentation", "documentation")
                hasEvaluator shouldBe true
            }
        }
    }

    describe("Validation Constraint Evaluation") {

        context("class constraints") {

            it("should validate XOR constraint") {
                val engine = createTestEngine()

                // Register validation: ownedAnnotatingElement XOR owningAnnotatingElement
                engine.constraintRegistry.register {
                    validation("Annotation", "validateAnnotationAnnotatingElement") { ctx ->
                        val owned = ctx.getProperty("ownedAnnotatingElement") != null
                        val owning = ctx.getProperty("owningAnnotatingElement") != null
                        if (owned xor owning) {
                            ValidationResult.valid()
                        } else {
                            ValidationResult.invalid(
                                "Exactly one of ownedAnnotatingElement or owningAnnotatingElement must be set"
                            )
                        }
                    }
                }

                // Create annotation with ownedAnnotatingElement set
                val validAnnotation = engine.createInstance("Annotation")
                engine.objectRepository.store("valid", validAnnotation)

                val annotatingElem = engine.createInstance("AnnotatingElement")
                engine.objectRepository.store("annotatingElem", annotatingElem)
                validAnnotation.setProperty("ownedAnnotatingElement", annotatingElem)

                // Should pass validation
                val validResult = engine.constraintEngine.validateInstance(validAnnotation, "valid")
                validResult.isValid shouldBe true

                // Create annotation with both set (invalid)
                val invalidAnnotation = engine.createInstance("Annotation")
                engine.objectRepository.store("invalid", invalidAnnotation)
                invalidAnnotation.setProperty("ownedAnnotatingElement", annotatingElem)
                invalidAnnotation.setProperty("owningAnnotatingElement", annotatingElem)

                val invalidResult = engine.constraintEngine.validateInstance(invalidAnnotation, "invalid")
                invalidResult.isValid shouldBe false
                invalidResult.errors shouldHaveSize 1
            }

            it("should validate constraint on instance without both properties set") {
                val engine = createTestEngine()

                engine.constraintRegistry.register {
                    validation("Annotation", "validateAnnotationAnnotatingElement") { ctx ->
                        val owned = ctx.getProperty("ownedAnnotatingElement") != null
                        val owning = ctx.getProperty("owningAnnotatingElement") != null
                        if (owned xor owning) {
                            ValidationResult.valid()
                        } else {
                            ValidationResult.invalid(
                                "Exactly one of ownedAnnotatingElement or owningAnnotatingElement must be set"
                            )
                        }
                    }
                }

                // Create annotation with neither set (also invalid per XOR)
                val neitherAnnotation = engine.createInstance("Annotation")
                engine.objectRepository.store("neither", neitherAnnotation)

                val result = engine.constraintEngine.validateInstance(neitherAnnotation, "neither")
                result.isValid shouldBe false
            }
        }

        context("inherited constraints") {

            it("should apply superclass constraints to subclasses") {
                val engine = createTestEngine()

                // Register constraint on Element
                engine.constraintRegistry.register {
                    validation("Element", "checkNameNotEmpty") { ctx ->
                        val name = ctx.getProperty("name") as? String
                        if (name.isNullOrBlank()) {
                            ValidationResult.invalid("Name must not be empty")
                        } else {
                            ValidationResult.valid()
                        }
                    }
                }

                // Create Documentation (subclass) without name
                val doc = engine.createInstance("Documentation")
                engine.objectRepository.store("doc", doc)

                // Should fail constraint inherited from Element
                val result = engine.constraintEngine.validateInstance(doc, "doc")
                result.isValid shouldBe false
            }
        }
    }

    describe("Association End Derivation") {

        it("should evaluate derived association end") {
            val engine = createTestEngine()

            // Register derived association end
            engine.constraintRegistry.register {
                associationEnd("ownedElementOwnerAssociation", "ownedDocumentation") { ctx ->
                    ctx.getLinkedTargets("ownedElementOwnerAssociation")
                        .filter { ctx.isKindOf(it, "Documentation") }
                }
            }

            // Create element with mixed children
            val owner = engine.createInstance("Element")
            engine.objectRepository.store("owner", owner)

            val doc = engine.createInstance("Documentation")
            engine.objectRepository.store("doc", doc)

            val comment = engine.createInstance("Comment")
            engine.objectRepository.store("comment", comment)

            engine.createLink("ownedElementOwnerAssociation", "owner", "doc")
            engine.createLink("ownedElementOwnerAssociation", "owner", "comment")

            // Evaluate derived end
            val docs = engine.constraintEngine.evaluateAssociationEnd(
                owner, "owner", "ownedElementOwnerAssociation", "ownedDocumentation"
            )

            docs shouldHaveSize 1
            docs.first().className shouldBe "Documentation"
        }
    }

    describe("ConstraintRegistry") {

        it("should provide statistics") {
            val registry = ConstraintRegistry()

            registry.registerDerivedProperty("Element", "prop1") { null }
            registry.registerDerivedProperty("Element", "prop2") { null }
            registry.registerValidationConstraint("Element", "check1") { ValidationResult.valid() }
            registry.registerAssociationEnd("Assoc", "end1") { emptyList() }

            val stats = registry.getStatistics()

            stats.derivedPropertyCount shouldBe 2
            stats.validationConstraintCount shouldBe 1
            stats.associationEndCount shouldBe 1
        }

        it("should clear all registrations") {
            val registry = ConstraintRegistry()

            registry.registerDerivedProperty("Element", "prop1") { null }
            registry.registerValidationConstraint("Element", "check1") { ValidationResult.valid() }

            registry.clear()

            val stats = registry.getStatistics()
            stats.derivedPropertyCount shouldBe 0
            stats.validationConstraintCount shouldBe 0
        }
    }

    describe("EvaluationContext helpers") {

        it("should support selectByKind operation") {
            val engine = createTestEngine()

            val elem = engine.createInstance("Element")
            engine.objectRepository.store("elem", elem)

            val doc = engine.createInstance("Documentation")
            engine.objectRepository.store("doc", doc)

            val comment = engine.createInstance("Comment")
            engine.objectRepository.store("comment", comment)

            engine.createLink("ownedElementOwnerAssociation", "elem", "doc")
            engine.createLink("ownedElementOwnerAssociation", "elem", "comment")

            // Use selectByKind in an evaluator
            engine.constraintRegistry.register {
                derivedProperty("Element", "documentation") { ctx ->
                    val ownedElements = ctx.getLinkedTargets("ownedElementOwnerAssociation")
                    ctx.selectByKind(ownedElements, "Documentation")
                }
            }

            val result = engine.getProperty(elem, "documentation")
            (result as List<*>) shouldHaveSize 1
        }
    }

    describe("OCL enum literal evaluation") {

        it("should resolve enum literal to lowercase value") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(
                name = "Import",
                attributes = listOf(
                    MetaProperty(name = "visibility", type = "VisibilityKind", lowerBound = 0)
                ),
                constraints = listOf(
                    org.openmbee.gearshift.metamodel.MetaConstraint(
                        name = "checkVisibilityIsPrivate",
                        type = org.openmbee.gearshift.metamodel.ConstraintType.VERIFICATION,
                        expression = "visibility = VisibilityKind::private"
                    )
                )
            ))

            val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
            val importInstance = engine.createInstance("Import")
            engine.setProperty(importInstance, "visibility", "private")

            val errors = engine.validate(importInstance)
            errors.shouldBeEmpty()
        }

        it("should evaluate implies with enum comparison") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(
                name = "TestElement",
                attributes = listOf(
                    MetaProperty(name = "owner", type = "Element", lowerBound = 0),
                    MetaProperty(name = "visibility", type = "VisibilityKind", lowerBound = 0)
                ),
                constraints = listOf(
                    org.openmbee.gearshift.metamodel.MetaConstraint(
                        name = "checkTopLevelVisibility",
                        type = org.openmbee.gearshift.metamodel.ConstraintType.VERIFICATION,
                        expression = "owner = null implies visibility = VisibilityKind::private"
                    )
                )
            ))

            val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
            val elem = engine.createInstance("TestElement")
            // owner is null, visibility is private - constraint should pass
            engine.setProperty(elem, "visibility", "private")

            val errors = engine.validate(elem)
            errors.shouldBeEmpty()
        }

        it("should fail when enum comparison does not match") {
            val registry = MetamodelRegistry()
            registry.registerClass(MetaClass(
                name = "TestElement",
                attributes = listOf(
                    MetaProperty(name = "owner", type = "Element", lowerBound = 0),
                    MetaProperty(name = "visibility", type = "VisibilityKind", lowerBound = 0)
                ),
                constraints = listOf(
                    org.openmbee.gearshift.metamodel.MetaConstraint(
                        name = "checkTopLevelVisibility",
                        type = org.openmbee.gearshift.metamodel.ConstraintType.VERIFICATION,
                        expression = "owner = null implies visibility = VisibilityKind::private"
                    )
                )
            ))

            val engine = MDMEngine(registry, ModelRepository(), LinkRepository())
            val elem = engine.createInstance("TestElement")
            // owner is null, but visibility is public - constraint should fail
            engine.setProperty(elem, "visibility", "public")

            val errors = engine.validate(elem)
            errors shouldHaveSize 1
        }
    }
})
