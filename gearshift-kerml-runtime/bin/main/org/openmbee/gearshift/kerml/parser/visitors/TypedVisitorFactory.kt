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
package org.openmbee.gearshift.kerml.parser.visitors

/**
 * Factory providing access to all typed KerML visitors.
 *
 * Visitors are organized by category following the KerML element hierarchy:
 * - Core: Root namespace, namespace, package
 * - Classifiers: Class, DataType, Structure, Association
 * - Features: Feature, Connector, Step, Expression
 * - Relationships: Specialization, Subclassification, etc.
 *
 * Each visitor is lazily instantiated and cached for reuse.
 */
object TypedVisitorFactory {

    /**
     * Core namespace visitors.
     */
    object Core {
        val rootNamespace by lazy { RootNamespaceVisitor() }
        val namespace by lazy { NamespaceVisitor() }
        val pkg by lazy { PackageVisitor() }
        val libraryPackage by lazy { LibraryPackageVisitor() }
    }

    /**
     * Type and Classifier visitors (non-feature types).
     */
    object Classifiers {
        val type by lazy { TypeVisitor() }
        val classifier by lazy { ClassifierVisitor() }
        val cls by lazy { ClassVisitor() }
        val dataType by lazy { DataTypeVisitor() }
        val structure by lazy { StructureVisitor() }
        val association by lazy { AssociationVisitor() }
        val associationStructure by lazy { AssociationStructureVisitor() }
        val behavior by lazy { BehaviorVisitor() }
        val function by lazy { FunctionVisitor() }
        val predicate by lazy { PredicateVisitor() }
        val interaction by lazy { InteractionVisitor() }
        val metaclass by lazy { MetaclassVisitor() }
    }

    /**
     * Feature visitors.
     */
    object Features {
        val feature by lazy { FeatureVisitor() }
        val step by lazy { StepVisitor() }
        val expression by lazy { ExpressionVisitor() }
        val booleanExpression by lazy { BooleanExpressionVisitor() }
        val invariant by lazy { InvariantVisitor() }
        val connector by lazy { ConnectorVisitor() }
        val bindingConnector by lazy { BindingConnectorVisitor() }
        val succession by lazy { SuccessionVisitor() }
        val flow by lazy { FlowVisitor() }
        val successionFlow by lazy { SuccessionFlowVisitor() }
    }

    /**
     * Relationship visitors.
     */
    object Relationships {
        val specialization by lazy { SpecializationVisitor() }
        val subclassification by lazy { SubclassificationVisitor() }
        val conjugation by lazy { ConjugationVisitor() }
        val disjoining by lazy { DisjoiningVisitor() }
        val featureTyping by lazy { FeatureTypingVisitor() }
        val subsetting by lazy { SubsettingVisitor() }
        val redefinition by lazy { RedefinitionVisitor() }
        // TODO: val featureInverting by lazy { FeatureInvertingVisitor() }
        // TODO: val typeFeaturing by lazy { TypeFeaturingVisitor() }
    }

    /**
     * Annotating element visitors.
     */
    object Annotations {
        val comment by lazy { CommentVisitor() }
        val documentation by lazy { DocumentationVisitor() }
        val textualRepresentation by lazy { TextualRepresentationVisitor() }
        val metadataFeature by lazy { MetadataFeatureVisitor() }
    }

    /**
     * Membership visitors.
     */
    object Memberships {
        val import by lazy { ImportVisitor() }
        // TODO: val owningMembership by lazy { OwningMembershipVisitor() }
        // TODO: val featureMembership by lazy { FeatureMembershipVisitor() }
        // TODO: val membershipImport by lazy { MembershipImportVisitor() }
        // TODO: val namespaceImport by lazy { NamespaceImportVisitor() }
    }
}
