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
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Visitor for Class elements.
 *
 * Per KerML spec 8.2.5.2: Classes are classifiers that represent object types with identity.
 *
 * Grammar:
 * ```
 * class
 *     : typePrefix CLASS
 *       classifierDeclaration typeBody
 *     ;
 *
 * typePrefix
 *     : ( ownedRelationship += PrefixMetadataMember )*
 *       ( isAbstract ?= ABSTRACT )?
 *     ;
 *
 * classifierDeclaration
 *     : ( isSufficient=ALL )? identification
 *       ownedMultiplicity?
 *       ( superclassingPart | conjugationPart )?
 *       typeRelationshipPart*
 *     ;
 * ```
 */
class ClassVisitor : BaseTypedVisitor<KerMLParser.ClassContext, KerMLClass>() {

    override fun visit(ctx: KerMLParser.ClassContext, parseContext: ParseContext): KerMLClass {
        // Create the Class instance using typed wrapper
        val cls = parseContext.create<KerMLClass>()

        // Parse typePrefix for isAbstract
        ctx.typePrefix()?.isAbstract?.let {
            cls.isAbstract = true
        }

        // Parse classifierDeclaration
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, cls, parseContext)
        }

        // Compute qualified name for this class
        val qualifiedName = computeQualifiedName(
            parseContext.parentQualifiedName,
            cls.declaredName
        )

        // Create child context for nested elements
        val childContext = parseContext.withParent(cls, cls.declaredName ?: "")

        // Create ownership relationship with parent namespace
        parseContext.parent?.let { parent ->
            val membership = parseContext.create<OwningMembership>()
            membership.memberElement = cls

            // Set membership name from class name
            cls.declaredName?.let { membership.memberName = it }
            cls.declaredShortName?.let { membership.memberShortName = it }

            // Link membership to owning namespace
            if (parent is Namespace) {
                membership.membershipOwningNamespace = parent
            }
        }

        // Parse type body (features, nested types, etc.)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        // TODO: Parse prefixMetadataMember annotations from typePrefix
        ctx.typePrefix()?.prefixMetadataMember()?.forEach { _ ->
            // Will delegate to PrefixMetadataMemberVisitor
        }

        return cls
    }

    /**
     * Parse a classifier declaration.
     */
    private fun parseClassifierDeclaration(
        decl: KerMLParser.ClassifierDeclarationContext,
        cls: KerMLClass,
        parseContext: ParseContext
    ) {
        // Parse identification
        decl.identification()?.let { id ->
            parseIdentification(id, cls)
        }

        // Parse isSufficient
        decl.isSufficient?.let {
            cls.isSufficient = true
        }

        // Parse multiplicity
        decl.ownedMultiplicity()?.let { mult ->
            // TODO: Parse multiplicity and set on class
        }

        // Parse superclassing (subclassification relationships)
        decl.superclassingPart()?.let { superclassing ->
            parseSuperclassingPart(superclassing, cls, parseContext)
        }

        // Parse conjugation if present (alternative to superclassing)
        decl.conjugationPart()?.let { _ ->
            // TODO: Delegate to ConjugationVisitor
        }

        // Parse type relationship parts (additional relationships)
        decl.typeRelationshipPart()?.forEach { relPart ->
            parseTypeRelationshipPart(relPart, cls, parseContext)
        }
    }

    /**
     * Parse superclassing part (subclassification relationships).
     *
     * Creates Subclassification relationships for each superclass reference.
     */
    private fun parseSuperclassingPart(
        ctx: KerMLParser.SuperclassingPartContext,
        cls: KerMLClass,
        parseContext: ParseContext
    ) {
        // Process each ownedSubclassification
        ctx.ownedSubclassification()?.forEach { subclassCtx ->
            parseOwnedSubclassification(subclassCtx, cls, parseContext)
        }
    }

    /**
     * Parse an owned subclassification (creates Subclassification relationship).
     */
    private fun parseOwnedSubclassification(
        ctx: KerMLParser.OwnedSubclassificationContext,
        cls: KerMLClass,
        parseContext: ParseContext
    ) {
        // Create Subclassification relationship
        val subclassification = parseContext.create<Subclassification>()

        // Set the subclassifier (the class being defined)
        subclassification.subclassifier = cls

        // Get the superclassifier reference (qualified name)
        ctx.qualifiedName()?.let { qnCtx ->
            val superclassName = extractQualifiedName(qnCtx)

            // Record unresolved reference - will be resolved after parsing
            // TODO: Add to parse context's unresolved references
            // For now, we'll need a mechanism to resolve this later
        }

        // Link the subclassification to the class's ownedRelationship
        // TODO: Set up the proper ownership link
    }

    /**
     * Parse a type relationship part (disjoining, unioning, intersecting, differencing).
     */
    private fun parseTypeRelationshipPart(
        ctx: KerMLParser.TypeRelationshipPartContext,
        cls: KerMLClass,
        parseContext: ParseContext
    ) {
        // Handle disjoiningPart
        ctx.disjoiningPart()?.let { _ ->
            // TODO: Parse disjoining relationships
        }

        // Handle unioningPart
        ctx.unioningPart()?.let { _ ->
            // TODO: Parse unioning relationships
        }

        // Handle intersectingPart
        ctx.intersectingPart()?.let { _ ->
            // TODO: Parse intersecting relationships
        }

        // Handle differencingPart
        ctx.differencingPart()?.let { _ ->
            // TODO: Parse differencing relationships
        }
    }

    /**
     * Parse a type body (contains features, nested types, etc.).
     */
    private fun parseTypeBody(
        ctx: KerMLParser.TypeBodyContext,
        parseContext: ParseContext
    ) {
        // Process each type body element
        ctx.typeBodyElement()?.forEach { bodyElement ->
            parseTypeBodyElement(bodyElement, parseContext)
        }
    }

    /**
     * Parse a type body element.
     *
     * Grammar: typeBodyElement = nonFeatureMember | featureMember | aliasMember | import_ | REGULAR_COMMENT
     */
    private fun parseTypeBodyElement(
        ctx: KerMLParser.TypeBodyElementContext,
        parseContext: ParseContext
    ) {
        // Handle nonFeatureMember
        ctx.nonFeatureMember()?.let { _ ->
            // TODO: Dispatch to appropriate non-feature visitor
            return
        }

        // Handle featureMember
        ctx.featureMember()?.let { featureMember ->
            parseFeatureMember(featureMember, parseContext)
            return
        }

        // Handle aliasMember
        ctx.aliasMember()?.let { _ ->
            // TODO: Delegate to AliasMemberVisitor
            return
        }

        // Handle import
        ctx.import_()?.let { _ ->
            // TODO: Delegate to ImportVisitor
            return
        }

        // Handle REGULAR_COMMENT (standalone comments in type body)
        // These are informational and don't create model elements
    }

    /**
     * Parse a feature member.
     */
    private fun parseFeatureMember(
        ctx: KerMLParser.FeatureMemberContext,
        parseContext: ParseContext
    ) {
        // featureMember = typeFeatureMember | ownedFeatureMember
        ctx.typeFeatureMember()?.let { typeFeat ->
            // typeFeatureMember has memberPrefix, MEMBER, featureElement
            typeFeat.featureElement()?.let { _ ->
                // TODO: Delegate to FeatureVisitor
            }
            return
        }

        ctx.ownedFeatureMember()?.let { ownedFeat ->
            // ownedFeatureMember has memberPrefix, featureElement
            ownedFeat.featureElement()?.let { _ ->
                // TODO: Delegate to FeatureVisitor
            }
        }
    }
}
