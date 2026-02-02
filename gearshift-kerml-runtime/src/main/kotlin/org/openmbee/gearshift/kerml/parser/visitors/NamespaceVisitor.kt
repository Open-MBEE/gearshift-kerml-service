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

import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor

/**
 * Base visitor for Namespace elements and namespace body parsing.
 *
 * This visitor provides common functionality for parsing namespace bodies,
 * which is shared by Package, LibraryPackage, and other namespace-like elements.
 *
 * Per KerML spec 8.2.3.4: Namespaces organize elements with qualified names.
 *
 * Grammar:
 * ```
 * namespace
 *     : prefixMetadataMember*
 *       namespaceDeclaration namespaceBody
 *     ;
 *
 * namespaceDeclaration
 *     : NAMESPACE identification
 *     ;
 *
 * namespaceBody
 *     : ';' | '{' namespaceBodyElement* '}'
 *     ;
 *
 * namespaceBodyElement
 *     : namespaceMember
 *     | aliasMember
 *     | import_
 *     | REGULAR_COMMENT
 *     ;
 * ```
 */
class NamespaceVisitor : BaseTypedVisitor<KerMLParser.NamespaceContext, Namespace>() {

    override fun visit(ctx: KerMLParser.NamespaceContext, kermlParseContext: KermlParseContext): Namespace {
        // Create the Namespace instance
        val ns = kermlParseContext.create<Namespace>()

        // Parse identification from namespaceDeclaration
        ctx.namespaceDeclaration()?.identification()?.let { id ->
            parseIdentification(id, ns)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(ns, ns.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(ns, kermlParseContext)

        // Parse namespace body
        ctx.namespaceBody()?.let { body ->
            parseNamespaceBody(body, childContext)
        }

        // Parse prefix metadata members
        ctx.prefixMetadataMember()?.forEach { _ ->
            // TODO: Delegate to PrefixMetadataMemberVisitor
        }

        return ns
    }

    /**
     * Create an OwningMembership linking the element to its parent namespace.
     */
    fun createOwnershipMembership(
        element: org.openmbee.gearshift.generated.interfaces.Element,
        kermlParseContext: KermlParseContext
    ) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = kermlParseContext.create<OwningMembership>()
                // Use ownedMemberElement (redefines memberElement) for proper ownership link
                membership.ownedMemberElement = element

                // Set membership name from element name
                element.declaredName?.let { membership.memberName = it }
                element.declaredShortName?.let { membership.memberShortName = it }

                // Link membership to owning namespace
                membership.membershipOwningNamespace = parent
                membership.visibility = kermlParseContext.memberVisibility
            }
        }
    }

    /**
     * Parse a namespace body.
     *
     * This method can be called by subclass visitors (Package, etc.) to parse
     * the body elements of any namespace-like element.
     */
    fun parseNamespaceBody(
        ctx: KerMLParser.NamespaceBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        // Process each namespace body element
        ctx.namespaceBodyElement()?.forEach { bodyElement ->
            parseNamespaceBodyElement(bodyElement, kermlParseContext)
        }
    }

    /**
     * Parse a namespace body element.
     *
     * Grammar: namespaceBodyElement = namespaceMember | aliasMember | import_ | REGULAR_COMMENT
     */
    fun parseNamespaceBodyElement(
        ctx: KerMLParser.NamespaceBodyElementContext,
        kermlParseContext: KermlParseContext
    ) {
        // Handle namespaceMember
        ctx.namespaceMember()?.let { member ->
            parseNamespaceMember(member, kermlParseContext)
            return
        }

        // Handle aliasMember
        ctx.aliasMember()?.let { alias ->
            parseAliasMember(alias, kermlParseContext)
            return
        }

        // Handle import
        ctx.import_()?.let { import ->
            parseImport(import, kermlParseContext)
            return
        }

        // Handle REGULAR_COMMENT (standalone comments - informational only)
    }

    /**
     * Parse a namespace member.
     *
     * Grammar: namespaceMember = nonFeatureMember | namespaceFeatureMember
     */
    fun parseNamespaceMember(
        ctx: KerMLParser.NamespaceMemberContext,
        kermlParseContext: KermlParseContext
    ) {
        // Handle nonFeatureMember
        ctx.nonFeatureMember()?.let { nonFeature ->
            parseNonFeatureMember(nonFeature, kermlParseContext)
            return
        }

        // Handle namespaceFeatureMember
        ctx.namespaceFeatureMember()?.let { featureMember ->
            parseNamespaceFeatureMember(featureMember, kermlParseContext)
        }
    }

    /**
     * Parse a non-feature member.
     *
     * Grammar: nonFeatureMember = memberPrefix memberElement
     */
    fun parseNonFeatureMember(
        ctx: KerMLParser.NonFeatureMemberContext,
        kermlParseContext: KermlParseContext
    ) {
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        val visCtx = kermlParseContext.copy(memberVisibility = visibility)

        // Get the memberElement
        ctx.memberElement()?.let { memberElement ->
            // Check for nonFeatureElement
            memberElement.nonFeatureElement()?.let { element ->
                parseNonFeatureElement(element, visCtx)
                return
            }

            // Check for annotatingElement
            memberElement.annotatingElement()?.let { annotating ->
                parseAnnotatingElement(annotating, visCtx)
            }
        }
    }

    /**
     * Parse a namespace feature member.
     *
     * Grammar: namespaceFeatureMember = memberPrefix featureElement
     */
    fun parseNamespaceFeatureMember(
        ctx: KerMLParser.NamespaceFeatureMemberContext,
        kermlParseContext: KermlParseContext
    ) {
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        val visCtx = kermlParseContext.copy(memberVisibility = visibility)

        // Get the featureElement
        ctx.featureElement()?.let { featureElement ->
            parseFeatureElement(featureElement, visCtx)
        }
    }

    /**
     * Parse a feature element (dispatches to specific feature visitor).
     *
     * Grammar: featureElement = feature | step | expression | booleanExpression | invariant
     *                          | connector | bindingConnector | succession | flow | successionFlow
     */
    fun parseFeatureElement(
        ctx: KerMLParser.FeatureElementContext,
        kermlParseContext: KermlParseContext
    ) {
        // Feature
        ctx.feature()?.let { feature ->
            FeatureVisitor().visit(feature, kermlParseContext)
            return
        }

        // Step
        ctx.step()?.let { step ->
            StepVisitor().visit(step, kermlParseContext)
            return
        }

        // Expression
        ctx.expression()?.let { expr ->
            ExpressionVisitor().visit(expr, kermlParseContext)
            return
        }

        // BooleanExpression
        ctx.booleanExpression()?.let { boolExpr ->
            BooleanExpressionVisitor().visit(boolExpr, kermlParseContext)
            return
        }

        // Invariant
        ctx.invariant()?.let { inv ->
            InvariantVisitor().visit(inv, kermlParseContext)
            return
        }

        // Connector
        ctx.connector()?.let { conn ->
            ConnectorVisitor().visit(conn, kermlParseContext)
            return
        }

        // BindingConnector
        ctx.bindingConnector()?.let { binding ->
            BindingConnectorVisitor().visit(binding, kermlParseContext)
            return
        }

        // Succession
        ctx.succession()?.let { succ ->
            SuccessionVisitor().visit(succ, kermlParseContext)
            return
        }

        // Flow
        ctx.flow()?.let { flow ->
            FlowVisitor().visit(flow, kermlParseContext)
            return
        }

        // SuccessionFlow
        ctx.successionFlow()?.let { succFlow ->
            SuccessionFlowVisitor().visit(succFlow, kermlParseContext)
            return
        }
    }

    /**
     * Parse a non-feature element (dispatches to specific element visitor).
     *
     * Grammar: nonFeatureElement = Dependency | Namespace | Type | Classifier | ...
     */
    fun parseNonFeatureElement(
        ctx: KerMLParser.NonFeatureElementContext,
        kermlParseContext: KermlParseContext
    ) {
        // Package
        ctx.package_()?.let { pkg ->
            PackageVisitor().visit(pkg, kermlParseContext)
            return
        }

        // LibraryPackage
        ctx.libraryPackage()?.let { libPkg ->
            LibraryPackageVisitor().visit(libPkg, kermlParseContext)
            return
        }

        // Namespace (generic)
        ctx.namespace()?.let { ns ->
            NamespaceVisitor().visit(ns, kermlParseContext)
            return
        }

        // Type (generic)
        ctx.type()?.let { type ->
            TypeVisitor().visit(type, kermlParseContext)
            return
        }

        // Classifier (generic)
        ctx.classifier()?.let { classifier ->
            ClassifierVisitor().visit(classifier, kermlParseContext)
            return
        }

        // Class
        ctx.class_()?.let { cls ->
            ClassVisitor().visit(cls, kermlParseContext)
            return
        }

        // DataType
        ctx.datatype()?.let { dt ->
            DataTypeVisitor().visit(dt, kermlParseContext)
            return
        }

        // Structure
        ctx.structure()?.let { struct ->
            StructureVisitor().visit(struct, kermlParseContext)
            return
        }

        // Association
        ctx.association()?.let { assoc ->
            AssociationVisitor().visit(assoc, kermlParseContext)
            return
        }

        // AssociationStructure
        ctx.associationStructure()?.let { assocStruct ->
            AssociationStructureVisitor().visit(assocStruct, kermlParseContext)
            return
        }

        // Behavior
        ctx.behavior()?.let { beh ->
            BehaviorVisitor().visit(beh, kermlParseContext)
            return
        }

        // Function
        ctx.function()?.let { func ->
            FunctionVisitor().visit(func, kermlParseContext)
            return
        }

        // Predicate
        ctx.predicate()?.let { pred ->
            PredicateVisitor().visit(pred, kermlParseContext)
            return
        }

        // Interaction
        ctx.interaction()?.let { inter ->
            InteractionVisitor().visit(inter, kermlParseContext)
            return
        }

        // Metaclass
        ctx.metaclass()?.let { meta ->
            MetaclassVisitor().visit(meta, kermlParseContext)
            return
        }

        // Dependency
        ctx.dependency()?.let { _ ->
            // TODO: Delegate to DependencyVisitor
            return
        }

        // Multiplicity
        ctx.multiplicity()?.let { mult ->
            MultiplicityVisitor().visit(mult, kermlParseContext)
            return
        }

        // Relationship elements
        ctx.specialization()?.let { spec ->
            SpecializationVisitor().visit(spec, kermlParseContext)
            return
        }

        ctx.conjugation()?.let { conj ->
            ConjugationVisitor().visit(conj, kermlParseContext)
            return
        }

        ctx.subclassification()?.let { subcl ->
            SubclassificationVisitor().visit(subcl, kermlParseContext)
            return
        }

        ctx.disjoining()?.let { disj ->
            DisjoiningVisitor().visit(disj, kermlParseContext)
            return
        }

        ctx.featureInverting()?.let { _ ->
            // TODO: Delegate to FeatureInvertingVisitor
            return
        }

        ctx.featureTyping()?.let { ft ->
            FeatureTypingVisitor().visit(ft, kermlParseContext)
            return
        }

        ctx.subsetting()?.let { sub ->
            SubsettingVisitor().visit(sub, kermlParseContext)
            return
        }

        ctx.redefinition()?.let { redef ->
            RedefinitionVisitor().visit(redef, kermlParseContext)
            return
        }

        ctx.typeFeaturing()?.let { _ ->
            // TODO: Delegate to TypeFeaturingVisitor
            return
        }
    }

    /**
     * Parse an alias member.
     */
    private fun parseAliasMember(
        ctx: KerMLParser.AliasMemberContext,
        kermlParseContext: KermlParseContext
    ) {
        // TODO: Implement alias member parsing
        // Creates an Membership with memberName/memberShortName aliasing another element
    }

    /**
     * Parse an import.
     */
    private fun parseImport(
        ctx: KerMLParser.Import_Context,
        kermlParseContext: KermlParseContext
    ) {
        ImportVisitor().visit(ctx, kermlParseContext)
    }

    /**
     * Parse an annotating element (dispatches to specific annotation visitor).
     *
     * Grammar: annotatingElement = comment | documentation | textualRepresentation | metadataFeature
     */
    private fun parseAnnotatingElement(
        ctx: KerMLParser.AnnotatingElementContext,
        kermlParseContext: KermlParseContext
    ) {
        ctx.comment()?.let { comment ->
            CommentVisitor().visit(comment, kermlParseContext)
            return
        }

        ctx.documentation()?.let { doc ->
            DocumentationVisitor().visit(doc, kermlParseContext)
            return
        }

        ctx.textualRepresentation()?.let { textRep ->
            TextualRepresentationVisitor().visit(textRep, kermlParseContext)
            return
        }

        ctx.metadataFeature()?.let { metadata ->
            MetadataFeatureVisitor().visit(metadata, kermlParseContext)
            return
        }
    }
}
