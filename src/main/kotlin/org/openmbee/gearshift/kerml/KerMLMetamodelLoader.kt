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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.kerml.metamodel.associations.*
import org.openmbee.gearshift.kerml.metamodel.classes.core.*
import org.openmbee.gearshift.kerml.metamodel.classes.kernel.*
import org.openmbee.gearshift.kerml.metamodel.classes.root.*

private val logger = KotlinLogging.logger {}

/**
 * Loads the KerML metamodel using the modular class definitions.
 *
 * This loader uses individual metaclass definition functions from the
 * `org.openmbee.gearshift.kerml.metamodel.classes` package hierarchy:
 * - `root.*` - Root package classes (Element, Relationship, Namespace, etc.)
 * - `core.*` - Core package classes (Feature, Type, Classifier, etc.)
 * - `kernel.*` - Kernel package classes (Definition, Usage, Expression, etc.)
 *
 * Each class is defined in its own file with a `createXxxMetaClass()` function.
 */
object KerMLMetamodelLoader {

    /**
     * Initialize the complete KerML metamodel in a Gearshift engine.
     *
     * Classes are registered in dependency order to ensure superclasses
     * are available before subclasses.
     */
    fun initialize(engine: GearshiftEngine) {
        logger.info { "Initializing KerML metamodel..." }

        // Register all metaclasses first
        registerRootPackage(engine)
        registerCorePackage(engine)
        registerKernelPackage(engine)

        // Register associations after classes are in place
        registerAssociations(engine)

        val classCount = engine.metamodelRegistry.getAllClasses().size
        val assocCount = engine.metamodelRegistry.getAllAssociations().size
        logger.info { "KerML metamodel initialized: $classCount classes, $assocCount associations" }
    }

    /**
     * Register Root package classes.
     * Contains the fundamental element hierarchy and relationships.
     */
    private fun registerRootPackage(engine: GearshiftEngine) {
        logger.debug { "Registering Root package..." }

        // Foundation
        engine.registerMetaClass(createElementMetaClass())
        engine.registerMetaClass(createRelationshipMetaClass())

        // Annotations
        engine.registerMetaClass(createAnnotatingElementMetaClass())
        engine.registerMetaClass(createAnnotationMetaClass())
        engine.registerMetaClass(createCommentMetaClass())
        engine.registerMetaClass(createDocumentationMetaClass())
        engine.registerMetaClass(createTextualRepresentationMetaClass())

        // Namespaces and membership
        engine.registerMetaClass(createNamespaceMetaClass())
        engine.registerMetaClass(createMembershipMetaClass())
        engine.registerMetaClass(createOwningMembershipMetaClass())

        // Imports
        engine.registerMetaClass(createImportMetaClass())
        engine.registerMetaClass(createMembershipImportMetaClass())
        engine.registerMetaClass(createNamespaceImportMetaClass())

        // Dependencies
        engine.registerMetaClass(createDependencyMetaClass())

        logger.debug { "Root package registered" }
    }

    /**
     * Register Core package classes.
     * Contains types, features, and core relationships.
     */
    private fun registerCorePackage(engine: GearshiftEngine) {
        logger.debug { "Registering Core package..." }

        // Specialization relationships
        engine.registerMetaClass(createSpecializationMetaClass())
        engine.registerMetaClass(createSubclassificationMetaClass())
        engine.registerMetaClass(createConjugationMetaClass())
        engine.registerMetaClass(createDisjoiningMetaClass())
        engine.registerMetaClass(createDifferencingMetaClass())
        engine.registerMetaClass(createIntersectingMetaClass())
        engine.registerMetaClass(createUnioningMetaClass())

        // Features
        engine.registerMetaClass(createCrossSubsettingMetaClass())
        engine.registerMetaClass(createEndFeatureMembershipMetaClass())
        engine.registerMetaClass(createFeatureMetaClass())
        engine.registerMetaClass(createFeatureChainingMetaClass())
        engine.registerMetaClass(createFeatureMembershipMetaClass())
        engine.registerMetaClass(createFeatureTypingMetaClass())
        engine.registerMetaClass(createFeatureInvertingMetaClass())
        engine.registerMetaClass(createRedefinitionMetaClass())
        engine.registerMetaClass(createReferenceSubsettingMetaClass())
        engine.registerMetaClass(createSubsettingMetaClass())

        // Types
        engine.registerMetaClass(createTypeMetaClass())
        engine.registerMetaClass(createClassifierMetaClass())
        engine.registerMetaClass(createFeaturingMetaClass())
        engine.registerMetaClass(createTypeFeaturingMetaClass())

        // Multiplicity
        engine.registerMetaClass(createMultiplicityMetaClass())
        engine.registerMetaClass(createMultiplicityRangeMetaClass())

        logger.debug { "Core package registered" }
    }

    /**
     * Register Kernel package classes.
     * Contains definitions, usages, expressions, and high-level constructs.
     */
    private fun registerKernelPackage(engine: GearshiftEngine) {
        logger.debug { "Registering Kernel package..." }

        // Structured types
        engine.registerMetaClass(createDataTypeMetaClass())
        engine.registerMetaClass(createClassMetaClass())
        engine.registerMetaClass(createStructureMetaClass())

        // Associations
        engine.registerMetaClass(createAssociationMetaClass())
        engine.registerMetaClass(createAssociationStructureMetaClass())

        // Connectors
        engine.registerMetaClass(createConnectorMetaClass())
        engine.registerMetaClass(createBindingConnectorMetaClass())
        engine.registerMetaClass(createSuccessionMetaClass())

        // Behaviors
        engine.registerMetaClass(createBehaviorMetaClass())
        engine.registerMetaClass(createStepMetaClass())
        engine.registerMetaClass(createParameterMembershipMetaClass())

        // Actions


        // Interactions
        engine.registerMetaClass(createInteractionMetaClass())
        engine.registerMetaClass(createItemFlowMetaClass())
        engine.registerMetaClass(createSuccessionItemFlowMetaClass())

        // Functions and Calculations
        engine.registerMetaClass(createFunctionMetaClass())
        engine.registerMetaClass(createPredicateMetaClass())

        // Expressions
        engine.registerMetaClass(createExpressionMetaClass())
        engine.registerMetaClass(createBooleanExpressionMetaClass())
        engine.registerMetaClass(createInvariantMetaClass())

        // Literal Expressions
        engine.registerMetaClass(createLiteralExpressionMetaClass())
        engine.registerMetaClass(createLiteralBooleanMetaClass())
        engine.registerMetaClass(createLiteralIntegerMetaClass())
        engine.registerMetaClass(createLiteralRationalMetaClass())
        engine.registerMetaClass(createLiteralStringMetaClass())
        engine.registerMetaClass(createLiteralInfinityMetaClass())
        engine.registerMetaClass(createNullExpressionMetaClass())

        // Complex Expressions
        engine.registerMetaClass(createOperatorExpressionMetaClass())
        engine.registerMetaClass(createInvocationExpressionMetaClass())
        engine.registerMetaClass(createFeatureChainExpressionMetaClass())
        engine.registerMetaClass(createFeatureReferenceExpressionMetaClass())
        engine.registerMetaClass(createCollectExpressionMetaClass())
        engine.registerMetaClass(createSelectExpressionMetaClass())
        engine.registerMetaClass(createMetadataAccessExpressionMetaClass())

        // Metadata
        engine.registerMetaClass(createMetaclassMetaClass())
        engine.registerMetaClass(createMetadataFeatureMetaClass())

        // Packages
        engine.registerMetaClass(createPackageMetaClass())
        engine.registerMetaClass(createLibraryPackageMetaClass())
        engine.registerMetaClass(createElementFilterMembershipMetaClass())

        logger.debug { "Kernel package registered" }
    }

    /**
     * Register all metamodel associations.
     * Associations define relationships between metaclasses.
     */
    private fun registerAssociations(engine: GearshiftEngine) {
        logger.debug { "Registering associations..." }

        // Root associations (Figures 4-8)
        createElementAssociations().forEach { engine.registerMetaAssociation(it) }
        createDependencyAssociations().forEach { engine.registerMetaAssociation(it) }
        createAnnotationAssociations().forEach { engine.registerMetaAssociation(it) }
        createNamespaceAssociations().forEach { engine.registerMetaAssociation(it) }
        createImportAssociations().forEach { engine.registerMetaAssociation(it) }
        // Core Associations (Figures 9-22)
        createTypeAssociations().forEach { engine.registerMetaAssociation(it) }
        createSpecializationAssociations().forEach { engine.registerMetaAssociation(it) }
        createConjugationAssociations().forEach { engine.registerMetaAssociation(it) }
        createDisjoiningAssociations().forEach { engine.registerMetaAssociation(it) }
        createUnioningAssociations().forEach { engine.registerMetaAssociation(it) }
        createIntersectingAssociations().forEach { engine.registerMetaAssociation(it) }
        createDifferencingAssociations().forEach { engine.registerMetaAssociation(it) }
        createClassifierAssociations().forEach { engine.registerMetaAssociation(it) }
        createFeaturesAssociations().forEach { engine.registerMetaAssociation(it) }
        createSubsettingAssociations().forEach { engine.registerMetaAssociation(it) }
        createFeatureChainingAssociations().forEach { engine.registerMetaAssociation(it) }
        createFeatureInvertingAssociations().forEach { engine.registerMetaAssociation(it) }
        createEndFeatureMembershipAssociations().forEach { engine.registerMetaAssociation(it) }
        createCrossSubsettingAssociations().forEach { engine.registerMetaAssociation(it) }
        // Kernel Associations (Figures 23-
        createAssociationAssociations().forEach { engine.registerMetaAssociation(it) }
        createConnectorAssociations().forEach { engine.registerMetaAssociation(it) }
        createBehaviorAssociations().forEach { engine.registerMetaAssociation(it) }
        createParameterMembershipAssociations().forEach { engine.registerMetaAssociation(it) }
        //...
        createPackageAssociations().forEach { engine.registerMetaAssociation(it) }

        logger.debug { "Associations registered" }
    }

    /**
     * Get statistics about the loaded metamodel.
     */
    fun getStatistics(engine: GearshiftEngine): Map<String, Int> {
        val classes = engine.metamodelRegistry.getAllClasses()
        val root = classes.count { it.name in getRootPackageClasses() }
        val core = classes.count { it.name in getCorePackageClasses() }
        val kernel = classes.count { it.name in getKernelPackageClasses() }

        return mapOf(
            "total" to classes.size,
            "root" to root,
            "core" to core,
            "kernel" to kernel
        )
    }

    private fun getRootPackageClasses() = setOf(
        "Element", "Relationship", "AnnotatingElement", "Annotation",
        "Comment", "Documentation", "TextualRepresentation",
        "Namespace", "Membership", "OwningMembership",
        "Import", "MembershipImport", "NamespaceImport", "Dependency"
    )

    private fun getCorePackageClasses() = setOf(
        // Type Metaclasses
        "Conjugation",
        "Differencing",
        "Disjoining",
        "FeatureMembership",
        "Intersecting",
        "Specialization",
        "Multiplicity",
        "Type",
        "Unioning",
        // Classifier
        "Classifier",
        "Subclassification",
        // Feature Metaclasses
        "CrossSubsetting",
        "EndFeatureMembership",
        "Feature",
        "FeatureChaining",
        "FeatureInverting",
        "FeatureTyping",
        "Redefinition",
        "ReferenceSubsetting",
        "Subsetting",
        "Featuring",
        "TypeFeaturing",
    )

    private fun getKernelPackageClasses() = setOf(
        "DataType",
        "Class",
        "Structure",
        "Association",
        "AssociationStructure",
        "Connector",
        "BindingConnector",
        "Package", "LibraryPackage",
        "Definition", "Usage",
        "OccurrenceDefinition", "OccurrenceUsage",
        "ItemDefinition", "ItemUsage",
        "PartDefinition", "PartUsage",
        "PortDefinition", "PortUsage", "ConjugatedPortDefinition",
        "ConnectionDefinition", "ConnectionUsage", "ConnectorAsUsage", "FlowConnectionUsage",
        "InterfaceDefinition", "InterfaceUsage",
        "AllocationDefinition", "AllocationUsage",
        "AttributeDefinition", "AttributeUsage",
        "ReferenceUsage",
        "EnumerationDefinition", "EnumerationUsage",
        "Behavior", "Step",
        "ActionDefinition", "ActionUsage", "Succession",
        "Interaction", "ItemFlow", "SuccessionItemFlow",
        "Function", "CalculationDefinition", "CalculationUsage", "Predicate",
        "Expression", "BooleanExpression", "Invariant",
        "LiteralExpression", "LiteralBoolean", "LiteralInteger", "LiteralRational",
        "LiteralString", "LiteralInfinity", "NullExpression",
        "OperatorExpression", "InvocationExpression", "FeatureChainExpression",
        "FeatureReferenceExpression", "CollectExpression", "SelectExpression",
        "MetadataAccessExpression",
        "CaseDefinition", "CaseUsage",
        "AnalysisCaseDefinition", "AnalysisCaseUsage",
        "VerificationCaseDefinition", "VerificationCaseUsage",
        "UseCaseDefinition", "UseCaseUsage",
        "MultiplicityRange",
        "Metaclass", "MetadataFeature"
    )
}
