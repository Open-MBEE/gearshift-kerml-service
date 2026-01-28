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
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

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

    override fun visit(ctx: KerMLParser.NamespaceContext, parseContext: ParseContext): Namespace {
        // Create the Namespace instance
        val ns = parseContext.create<Namespace>()

        // Parse identification from namespaceDeclaration
        ctx.namespaceDeclaration()?.identification()?.let { id ->
            parseIdentification(id, ns)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(ns, ns.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(ns, parseContext)

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
        parseContext: ParseContext
    ) {
        parseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = parseContext.create<OwningMembership>()
                membership.memberElement = element

                // Set membership name from element name
                element.declaredName?.let { membership.memberName = it }
                element.declaredShortName?.let { membership.memberShortName = it }

                // Link membership to owning namespace
                membership.membershipOwningNamespace = parent
                membership.visibility = parseContext.memberVisibility
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
        parseContext: ParseContext
    ) {
        // Process each namespace body element
        ctx.namespaceBodyElement()?.forEach { bodyElement ->
            parseNamespaceBodyElement(bodyElement, parseContext)
        }
    }

    /**
     * Parse a namespace body element.
     *
     * Grammar: namespaceBodyElement = namespaceMember | aliasMember | import_ | REGULAR_COMMENT
     */
    fun parseNamespaceBodyElement(
        ctx: KerMLParser.NamespaceBodyElementContext,
        parseContext: ParseContext
    ) {
        // Handle namespaceMember
        ctx.namespaceMember()?.let { member ->
            parseNamespaceMember(member, parseContext)
            return
        }

        // Handle aliasMember
        ctx.aliasMember()?.let { alias ->
            parseAliasMember(alias, parseContext)
            return
        }

        // Handle import
        ctx.import_()?.let { import ->
            parseImport(import, parseContext)
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
        parseContext: ParseContext
    ) {
        // Handle nonFeatureMember
        ctx.nonFeatureMember()?.let { nonFeature ->
            parseNonFeatureMember(nonFeature, parseContext)
            return
        }

        // Handle namespaceFeatureMember
        ctx.namespaceFeatureMember()?.let { featureMember ->
            parseNamespaceFeatureMember(featureMember, parseContext)
        }
    }

    /**
     * Parse a non-feature member.
     *
     * Grammar: nonFeatureMember = memberPrefix memberElement
     */
    fun parseNonFeatureMember(
        ctx: KerMLParser.NonFeatureMemberContext,
        parseContext: ParseContext
    ) {
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        val visCtx = parseContext.copy(memberVisibility = visibility)

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
        parseContext: ParseContext
    ) {
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        val visCtx = parseContext.copy(memberVisibility = visibility)

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
        parseContext: ParseContext
    ) {
        // Feature
        ctx.feature()?.let { feature ->
            FeatureVisitor().visit(feature, parseContext)
            return
        }

        // Step
        ctx.step()?.let { step ->
            StepVisitor().visit(step, parseContext)
            return
        }

        // Expression
        ctx.expression()?.let { expr ->
            ExpressionVisitor().visit(expr, parseContext)
            return
        }

        // BooleanExpression
        ctx.booleanExpression()?.let { boolExpr ->
            BooleanExpressionVisitor().visit(boolExpr, parseContext)
            return
        }

        // Invariant
        ctx.invariant()?.let { inv ->
            InvariantVisitor().visit(inv, parseContext)
            return
        }

        // Connector
        ctx.connector()?.let { conn ->
            ConnectorVisitor().visit(conn, parseContext)
            return
        }

        // BindingConnector
        ctx.bindingConnector()?.let { binding ->
            BindingConnectorVisitor().visit(binding, parseContext)
            return
        }

        // Succession
        ctx.succession()?.let { succ ->
            SuccessionVisitor().visit(succ, parseContext)
            return
        }

        // Flow
        ctx.flow()?.let { flow ->
            FlowVisitor().visit(flow, parseContext)
            return
        }

        // SuccessionFlow
        ctx.successionFlow()?.let { succFlow ->
            SuccessionFlowVisitor().visit(succFlow, parseContext)
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
        parseContext: ParseContext
    ) {
        // Package
        ctx.package_()?.let { pkg ->
            PackageVisitor().visit(pkg, parseContext)
            return
        }

        // LibraryPackage
        ctx.libraryPackage()?.let { libPkg ->
            LibraryPackageVisitor().visit(libPkg, parseContext)
            return
        }

        // Namespace (generic)
        ctx.namespace()?.let { ns ->
            NamespaceVisitor().visit(ns, parseContext)
            return
        }

        // Type (generic)
        ctx.type()?.let { type ->
            TypeVisitor().visit(type, parseContext)
            return
        }

        // Classifier (generic)
        ctx.classifier()?.let { classifier ->
            ClassifierVisitor().visit(classifier, parseContext)
            return
        }

        // Class
        ctx.class_()?.let { cls ->
            ClassVisitor().visit(cls, parseContext)
            return
        }

        // DataType
        ctx.datatype()?.let { dt ->
            DataTypeVisitor().visit(dt, parseContext)
            return
        }

        // Structure
        ctx.structure()?.let { struct ->
            StructureVisitor().visit(struct, parseContext)
            return
        }

        // Association
        ctx.association()?.let { assoc ->
            AssociationVisitor().visit(assoc, parseContext)
            return
        }

        // AssociationStructure
        ctx.associationStructure()?.let { assocStruct ->
            AssociationStructureVisitor().visit(assocStruct, parseContext)
            return
        }

        // Behavior
        ctx.behavior()?.let { beh ->
            BehaviorVisitor().visit(beh, parseContext)
            return
        }

        // Function
        ctx.function()?.let { func ->
            FunctionVisitor().visit(func, parseContext)
            return
        }

        // Predicate
        ctx.predicate()?.let { pred ->
            PredicateVisitor().visit(pred, parseContext)
            return
        }

        // Interaction
        ctx.interaction()?.let { inter ->
            InteractionVisitor().visit(inter, parseContext)
            return
        }

        // Metaclass
        ctx.metaclass()?.let { meta ->
            MetaclassVisitor().visit(meta, parseContext)
            return
        }

        // Dependency
        ctx.dependency()?.let { _ ->
            // TODO: Delegate to DependencyVisitor
            return
        }

        // Multiplicity
        ctx.multiplicity()?.let { mult ->
            MultiplicityVisitor().visit(mult, parseContext)
            return
        }

        // Relationship elements
        ctx.specialization()?.let { spec ->
            SpecializationVisitor().visit(spec, parseContext)
            return
        }

        ctx.conjugation()?.let { conj ->
            ConjugationVisitor().visit(conj, parseContext)
            return
        }

        ctx.subclassification()?.let { subcl ->
            SubclassificationVisitor().visit(subcl, parseContext)
            return
        }

        ctx.disjoining()?.let { disj ->
            DisjoiningVisitor().visit(disj, parseContext)
            return
        }

        ctx.featureInverting()?.let { _ ->
            // TODO: Delegate to FeatureInvertingVisitor
            return
        }

        ctx.featureTyping()?.let { ft ->
            FeatureTypingVisitor().visit(ft, parseContext)
            return
        }

        ctx.subsetting()?.let { sub ->
            SubsettingVisitor().visit(sub, parseContext)
            return
        }

        ctx.redefinition()?.let { redef ->
            RedefinitionVisitor().visit(redef, parseContext)
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
        parseContext: ParseContext
    ) {
        // TODO: Implement alias member parsing
        // Creates an Membership with memberName/memberShortName aliasing another element
    }

    /**
     * Parse an import.
     */
    private fun parseImport(
        ctx: KerMLParser.Import_Context,
        parseContext: ParseContext
    ) {
        ImportVisitor().visit(ctx, parseContext)
    }

    /**
     * Parse an annotating element (dispatches to specific annotation visitor).
     *
     * Grammar: annotatingElement = comment | documentation | textualRepresentation | metadataFeature
     */
    private fun parseAnnotatingElement(
        ctx: KerMLParser.AnnotatingElementContext,
        parseContext: ParseContext
    ) {
        ctx.comment()?.let { comment ->
            CommentVisitor().visit(comment, parseContext)
            return
        }

        ctx.documentation()?.let { doc ->
            DocumentationVisitor().visit(doc, parseContext)
            return
        }

        ctx.textualRepresentation()?.let { textRep ->
            TextualRepresentationVisitor().visit(textRep, parseContext)
            return
        }

        ctx.metadataFeature()?.let { metadata ->
            MetadataFeatureVisitor().visit(metadata, parseContext)
            return
        }
    }
}
