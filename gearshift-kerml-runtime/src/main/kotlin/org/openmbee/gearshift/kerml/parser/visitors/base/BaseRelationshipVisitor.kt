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
package org.openmbee.gearshift.kerml.parser.visitors.base

import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Relationship
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext

/**
 * Abstract base visitor for Relationship elements and their subclasses.
 *
 * Provides shared parsing methods for:
 * - identification (declaredShortName, declaredName)
 * - relationshipBody
 * - ownership membership creation
 *
 * This follows the KerML hierarchy where Relationship extends Element
 * and is the base for Specialization, Conjugation, Disjoining, etc.
 *
 * @param Ctx The ANTLR parser context type
 * @param Result The typed wrapper type (must extend Relationship)
 */
abstract class BaseRelationshipVisitor<Ctx, Result : Relationship> : BaseTypedVisitor<Ctx, Result>() {

    /**
     * Parse relationship body.
     *
     * Grammar: relationshipBody = ';' | '{' relationshipOwnedElement* '}'
     */
    protected fun parseRelationshipBody(
        ctx: KerMLParser.RelationshipBodyContext,
        relationship: Relationship,
        kermlParseContext: KermlParseContext
    ) {
        ctx.relationshipOwnedElement()?.forEach { ownedElement ->
            parseRelationshipOwnedElement(ownedElement, relationship, kermlParseContext)
        }
    }

    /**
     * Parse relationship owned element.
     *
     * Grammar: relationshipOwnedElement = ownedAnnotation | REGULAR_COMMENT
     */
    protected fun parseRelationshipOwnedElement(
        ctx: KerMLParser.RelationshipOwnedElementContext,
        relationship: Relationship,
        kermlParseContext: KermlParseContext
    ) {
        ctx.ownedAnnotation()?.let { _ ->
            // TODO: Delegate to AnnotationVisitor
        }
        // REGULAR_COMMENT - informational only, no model element created
    }

    /**
     * Create an OwningMembership linking the relationship to its parent namespace.
     */
    protected fun createRelationshipMembership(
        relationship: Relationship,
        kermlParseContext: KermlParseContext
    ) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = kermlParseContext.create<OwningMembership>()
                // Use ownedMemberElement (redefines memberElement) for proper ownership link
                membership.ownedMemberElement = relationship
                relationship.declaredName?.let { membership.memberName = it }
                relationship.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }

    /**
     * Parse a qualified name and store it for later resolution.
     * Returns the qualified name string.
     */
    protected fun parseQualifiedNameReference(ctx: KerMLParser.QualifiedNameContext?): String? {
        return ctx?.let { extractQualifiedName(it) }
    }

    /**
     * Parse a feature chain and return a string representation.
     * Feature chains are dot-separated feature references.
     */
    protected fun parseFeatureChain(ctx: KerMLParser.FeatureChainContext?): String? {
        return ctx?.ownedFeatureChaining()?.mapNotNull { chaining ->
            chaining.qualifiedName()?.let { extractQualifiedName(it) }
        }?.joinToString(".")
    }
}
