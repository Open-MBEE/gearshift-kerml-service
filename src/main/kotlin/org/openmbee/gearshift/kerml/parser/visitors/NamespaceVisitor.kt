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
        // TODO: Parse memberPrefix for visibility

        // Get the memberElement
        ctx.memberElement()?.let { memberElement ->
            // Check for nonFeatureElement
            memberElement.nonFeatureElement()?.let { element ->
                parseNonFeatureElement(element, parseContext)
                return
            }

            // Check for annotatingElement
            memberElement.annotatingElement()?.let { _ ->
                // TODO: Delegate to AnnotatingElementVisitor
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
        // TODO: Parse memberPrefix for visibility

        // Get the featureElement
        ctx.featureElement()?.let { _ ->
            // TODO: Delegate to FeatureVisitor
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
        ctx.libraryPackage()?.let { _ ->
            // TODO: Delegate to LibraryPackageVisitor
            return
        }

        // Namespace (generic)
        ctx.namespace()?.let { ns ->
            NamespaceVisitor().visit(ns, parseContext)
            return
        }

        // Class
        ctx.class_()?.let { cls ->
            ClassVisitor().visit(cls, parseContext)
            return
        }

        // DataType
        ctx.datatype()?.let { _ ->
            // TODO: Delegate to DataTypeVisitor
            return
        }

        // Structure
        ctx.structure()?.let { _ ->
            // TODO: Delegate to StructureVisitor
            return
        }

        // Association
        ctx.association()?.let { _ ->
            // TODO: Delegate to AssociationVisitor
            return
        }

        // AssociationStructure
        ctx.associationStructure()?.let { _ ->
            // TODO: Delegate to AssociationStructureVisitor
            return
        }

        // Behavior
        ctx.behavior()?.let { _ ->
            // TODO: Delegate to BehaviorVisitor
            return
        }

        // Function
        ctx.function()?.let { _ ->
            // TODO: Delegate to FunctionVisitor
            return
        }

        // Predicate
        ctx.predicate()?.let { _ ->
            // TODO: Delegate to PredicateVisitor
            return
        }

        // Interaction
        ctx.interaction()?.let { _ ->
            // TODO: Delegate to InteractionVisitor
            return
        }

        // Metaclass
        ctx.metaclass()?.let { _ ->
            // TODO: Delegate to MetaclassVisitor
            return
        }

        // Dependency
        ctx.dependency()?.let { _ ->
            // TODO: Delegate to DependencyVisitor
            return
        }

        // Multiplicity
        ctx.multiplicity()?.let { _ ->
            // TODO: Delegate to MultiplicityVisitor
            return
        }

        // Relationship elements
        ctx.specialization()?.let { _ ->
            // TODO: Delegate to SpecializationVisitor
            return
        }

        ctx.conjugation()?.let { _ ->
            // TODO: Delegate to ConjugationVisitor
            return
        }

        ctx.subclassification()?.let { _ ->
            // TODO: Delegate to SubclassificationVisitor
            return
        }

        ctx.disjoining()?.let { _ ->
            // TODO: Delegate to DisjoiningVisitor
            return
        }

        ctx.featureInverting()?.let { _ ->
            // TODO: Delegate to FeatureInvertingVisitor
            return
        }

        ctx.featureTyping()?.let { _ ->
            // TODO: Delegate to FeatureTypingVisitor
            return
        }

        ctx.subsetting()?.let { _ ->
            // TODO: Delegate to SubsettingVisitor
            return
        }

        ctx.redefinition()?.let { _ ->
            // TODO: Delegate to RedefinitionVisitor
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
        // TODO: Implement import parsing
        // Creates Import or MembershipImport based on import type
    }
}
