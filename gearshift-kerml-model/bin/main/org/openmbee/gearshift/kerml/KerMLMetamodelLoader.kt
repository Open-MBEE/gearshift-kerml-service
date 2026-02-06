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
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
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
     * Initialize the complete KerML metamodel in a metamodel registry.
     *
     * Classes are registered in dependency order to ensure superclasses
     * are available before subclasses.
     */
    fun initialize(registry: MetamodelRegistry) {
        logger.info { "Initializing KerML metamodel..." }

        // Register all metaclasses first
        registerRootPackage(registry)
        registerCorePackage(registry)
        registerKernelPackage(registry)

        // Register associations after classes are in place
        registerAssociations(registry)

        // Note: Ownership semantics are now declared on individual MetaClass definitions
        // via ownershipBinding (e.g., on OwningMembership, FeatureMembership, etc.)
        // and resolved dynamically by OwnershipResolver.

        val classCount = registry.getAllClasses().size
        val assocCount = registry.getAllAssociations().size
        logger.info { "KerML metamodel initialized: $classCount classes, $assocCount associations" }
    }

    /**
     * Register Root package classes.
     * Contains the fundamental element hierarchy and relationships.
     */
    private fun registerRootPackage(registry: MetamodelRegistry) {
        logger.debug { "Registering Root package..." }

        // Foundation
        registry.registerClass(createElementMetaClass())
        registry.registerClass(createRelationshipMetaClass())

        // Annotations
        registry.registerClass(createAnnotatingElementMetaClass())
        registry.registerClass(createAnnotationMetaClass())
        registry.registerClass(createCommentMetaClass())
        registry.registerClass(createDocumentationMetaClass())
        registry.registerClass(createTextualRepresentationMetaClass())

        // Namespaces and membership
        registry.registerClass(createNamespaceMetaClass())
        registry.registerClass(createMembershipMetaClass())
        registry.registerClass(createOwningMembershipMetaClass())

        // Imports
        registry.registerClass(createImportMetaClass())
        registry.registerClass(createMembershipImportMetaClass())
        registry.registerClass(createNamespaceImportMetaClass())

        // Dependencies
        registry.registerClass(createDependencyMetaClass())

        logger.debug { "Root package registered" }
    }

    /**
     * Register Core package classes.
     * Contains types, features, and core relationships.
     */
    private fun registerCorePackage(registry: MetamodelRegistry) {
        logger.debug { "Registering Core package..." }

        // Specialization relationships
        registry.registerClass(createSpecializationMetaClass())
        registry.registerClass(createSubclassificationMetaClass())
        registry.registerClass(createConjugationMetaClass())
        registry.registerClass(createDisjoiningMetaClass())
        registry.registerClass(createDifferencingMetaClass())
        registry.registerClass(createIntersectingMetaClass())
        registry.registerClass(createUnioningMetaClass())

        // Features
        registry.registerClass(createCrossSubsettingMetaClass())
        registry.registerClass(createEndFeatureMembershipMetaClass())
        registry.registerClass(createFeatureMetaClass())
        registry.registerClass(createFeatureChainingMetaClass())
        registry.registerClass(createFeatureMembershipMetaClass())
        registry.registerClass(createFeatureTypingMetaClass())
        registry.registerClass(createFeatureInvertingMetaClass())
        registry.registerClass(createRedefinitionMetaClass())
        registry.registerClass(createReferenceSubsettingMetaClass())
        registry.registerClass(createSubsettingMetaClass())

        // Types
        registry.registerClass(createTypeMetaClass())
        registry.registerClass(createClassifierMetaClass())
        registry.registerClass(createFeaturingMetaClass())
        registry.registerClass(createTypeFeaturingMetaClass())

        // Multiplicity
        registry.registerClass(createMultiplicityMetaClass())
        registry.registerClass(createMultiplicityRangeMetaClass())

        logger.debug { "Core package registered" }
    }

    /**
     * Register Kernel package classes.
     * Contains definitions, usages, expressions, and high-level constructs.
     */
    private fun registerKernelPackage(registry: MetamodelRegistry) {
        logger.debug { "Registering Kernel package..." }

        // Structured types
        registry.registerClass(createDataTypeMetaClass())
        registry.registerClass(createClassMetaClass())
        registry.registerClass(createStructureMetaClass())

        // Associations
        registry.registerClass(createAssociationMetaClass())
        registry.registerClass(createAssociationStructureMetaClass())

        // Connectors
        registry.registerClass(createConnectorMetaClass())
        registry.registerClass(createBindingConnectorMetaClass())
        registry.registerClass(createSuccessionMetaClass())

        // Behaviors
        registry.registerClass(createBehaviorMetaClass())
        registry.registerClass(createStepMetaClass())
        registry.registerClass(createParameterMembershipMetaClass())

        // Functions and Calculations
        registry.registerClass(createFunctionMetaClass())
        registry.registerClass(createPredicateMetaClass())
        registry.registerClass(createResultExpressionMembershipMetaClass())
        registry.registerClass(createReturnParameterMembershipMetaClass())

        // Expressions
        registry.registerClass(createExpressionMetaClass())
        registry.registerClass(createBooleanExpressionMetaClass())
        registry.registerClass(createInvariantMetaClass())

        // Literal Expressions
        registry.registerClass(createLiteralExpressionMetaClass())
        registry.registerClass(createLiteralBooleanMetaClass())
        registry.registerClass(createLiteralIntegerMetaClass())
        registry.registerClass(createLiteralRationalMetaClass())
        registry.registerClass(createLiteralStringMetaClass())
        registry.registerClass(createLiteralInfinityMetaClass())
        registry.registerClass(createNullExpressionMetaClass())

        // Complex Expressions
        registry.registerClass(createInstantiationExpressionMetaClass())
        registry.registerClass(createOperatorExpressionMetaClass())
        registry.registerClass(createInvocationExpressionMetaClass())
        registry.registerClass(createFeatureChainExpressionMetaClass())
        registry.registerClass(createFeatureReferenceExpressionMetaClass())
        registry.registerClass(createIndexExpressionMetaClass())
        registry.registerClass(createCollectExpressionMetaClass())
        registry.registerClass(createConstructorExpressionMetaClass())
        registry.registerClass(createSelectExpressionMetaClass())
        registry.registerClass(createMetadataAccessExpressionMetaClass())

        // Interactions
        registry.registerClass(createInteractionMetaClass())
        registry.registerClass(createFlowMetaClass())
        registry.registerClass(createFlowEndMetaClass())
        registry.registerClass(createPayloadFeatureMetaClass())
        registry.registerClass(createSuccessionFlowMetaClass())

        // Feature Values
        registry.registerClass(createFeatureValueMetaClass())

        // Metadata
        registry.registerClass(createMetaclassMetaClass())
        registry.registerClass(createMetadataFeatureMetaClass())

        // Packages
        registry.registerClass(createPackageMetaClass())
        registry.registerClass(createLibraryPackageMetaClass())
        registry.registerClass(createElementFilterMembershipMetaClass())

        logger.debug { "Kernel package registered" }
    }

    /**
     * Register all metamodel associations.
     * Associations define relationships between metaclasses.
     */
    private fun registerAssociations(registry: MetamodelRegistry) {
        logger.debug { "Registering associations..." }

        // Root associations (Figures 4-8)
        createElementAssociations().forEach { registry.registerAssociation(it) }
        createDependencyAssociations().forEach { registry.registerAssociation(it) }
        createAnnotationAssociations().forEach { registry.registerAssociation(it) }
        createNamespaceAssociations().forEach { registry.registerAssociation(it) }
        createImportAssociations().forEach { registry.registerAssociation(it) }
        // Core Associations (Figures 9-22)
        createTypeAssociations().forEach { registry.registerAssociation(it) }
        createSpecializationAssociations().forEach { registry.registerAssociation(it) }
        createConjugationAssociations().forEach { registry.registerAssociation(it) }
        createDisjoiningAssociations().forEach { registry.registerAssociation(it) }
        createUnioningAssociations().forEach { registry.registerAssociation(it) }
        createIntersectingAssociations().forEach { registry.registerAssociation(it) }
        createDifferencingAssociations().forEach { registry.registerAssociation(it) }
        createClassifierAssociations().forEach { registry.registerAssociation(it) }
        createFeaturesAssociations().forEach { registry.registerAssociation(it) }
        createSubsettingAssociations().forEach { registry.registerAssociation(it) }
        createFeatureChainingAssociations().forEach { registry.registerAssociation(it) }
        createFeatureInvertingAssociations().forEach { registry.registerAssociation(it) }
        createEndFeatureMembershipAssociations().forEach { registry.registerAssociation(it) }
        createCrossSubsettingAssociations().forEach { registry.registerAssociation(it) }
        createMultiplicityAssociations().forEach { registry.registerAssociation(it) }
        // Kernel Associations (Figures 23-
        createAssociationAssociations().forEach { registry.registerAssociation(it) }
        createConnectorAssociations().forEach { registry.registerAssociation(it) }
        // Behavior
        createBehaviorAssociations().forEach { registry.registerAssociation(it) }
        createParameterMembershipAssociations().forEach { registry.registerAssociation(it) }
        // Functions
        createFunctionAssociations().forEach { registry.registerAssociation(it) }
        createPredicateAssociations().forEach { registry.registerAssociation(it) }
        createFunctionAssociations().forEach { registry.registerAssociation(it) }
        // Expressions
        createExpressionAssociations().forEach { registry.registerAssociation(it) }
        createLiteralExpressionAssociations().forEach { registry.registerAssociation(it) }
        // Flow
        createFlowAssociations().forEach { registry.registerAssociation(it) }
        // Feature Values
        createFeatureValueAssociations().forEach { registry.registerAssociation(it) }
        // Metadata
        createMetadataAnnotationAssociations().forEach { registry.registerAssociation(it) }
        //...
        createPackageAssociations().forEach { registry.registerAssociation(it) }

        logger.debug { "Associations registered" }
    }

    /**
     * Get statistics about the loaded metamodel.
     */
    fun getStatistics(registry: MetamodelRegistry): Map<String, Int> {
        val classes = registry.getAllClasses()
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
        // Structured types
        "DataType", "Class", "Structure",
        // Associations
        "Association", "AssociationStructure",
        // Connectors
        "Connector", "BindingConnector", "Succession",
        // Behaviors
        "Behavior", "Step", "ParameterMembership",
        // Functions
        "Function", "Predicate", "ResultExpressionMembership", "ReturnParameterMembership",
        // Expressions
        "Expression", "BooleanExpression", "Invariant",
        // Literal Expressions
        "LiteralExpression", "LiteralBoolean", "LiteralInteger", "LiteralRational",
        "LiteralString", "LiteralInfinity", "NullExpression",
        // Complex Expressions
        "InstantiationExpression", "OperatorExpression", "InvocationExpression",
        "FeatureChainExpression", "FeatureReferenceExpression", "IndexExpression",
        "CollectExpression", "ConstructorExpression", "SelectExpression", "MetadataAccessExpression",
        // Interactions
        "Interaction", "Flow", "FlowEnd", "PayloadFeature", "SuccessionFlow",
        // Feature Values
        "FeatureValue",
        // Metadata
        "Metaclass", "MetadataFeature",
        // Packages
        "Package", "LibraryPackage", "ElementFilterMembership",
        // Multiplicity
        "MultiplicityRange"
    )
}
