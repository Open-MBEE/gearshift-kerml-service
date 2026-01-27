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

import org.openmbee.gearshift.generated.interfaces.Specialization
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypeVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for generic Type elements.
 *
 * Per KerML spec 8.2.3.5: Types are generalizations of what things are or can be.
 *
 * Grammar:
 * ```
 * type
 *     : typePrefix TYPE
 *       typeDeclaration typeBody
 *     ;
 * ```
 *
 * Type is the base for Classifier, Feature, and other typed elements.
 */
class TypeVisitor : BaseTypeVisitor<KerMLParser.TypeContext, Type>() {

    override fun visit(ctx: KerMLParser.TypeContext, parseContext: ParseContext): Type {
        val type = parseContext.create<Type>()

        // Parse typePrefix
        parseTypePrefix(ctx.typePrefix(), type)

        // Parse typeDeclaration
        ctx.typeDeclaration()?.let { decl ->
            parseTypeDeclaration(decl, type, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(type, type.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(type, parseContext)

        // Parse type body
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return type
    }

    /**
     * Parse a type declaration.
     *
     * Grammar:
     * typeDeclaration
     *     : ( isSufficient=ALL )? identification
     *       ownedMultiplicity?
     *       ( specializationPart | conjugationPart )+
     *       typeRelationshipPart*
     *     ;
     */
    protected fun parseTypeDeclaration(
        decl: KerMLParser.TypeDeclarationContext,
        type: Type,
        parseContext: ParseContext
    ) {
        // Parse identification
        decl.identification()?.let { id ->
            parseIdentification(id, type)
        }

        // Parse isSufficient
        decl.isSufficient?.let {
            type.isSufficient = true
        }

        // Parse multiplicity
        decl.ownedMultiplicity()?.let { _ ->
            // TODO: Parse multiplicity
        }

        // Parse specialization parts
        decl.specializationPart()?.forEach { specPart ->
            parseSpecializationPart(specPart, type, parseContext)
        }

        // Parse conjugation if present
        decl.conjugationPart()?.forEach { _ ->
            // TODO: Delegate to ConjugationVisitor
        }

        // Parse type relationship parts
        decl.typeRelationshipPart()?.forEach { relPart ->
            parseTypeRelationshipPart(relPart, type, parseContext)
        }
    }

    /**
     * Parse specialization part.
     */
    private fun parseSpecializationPart(
        ctx: KerMLParser.SpecializationPartContext,
        type: Type,
        parseContext: ParseContext
    ) {
        ctx.ownedSpecialization()?.forEach { specCtx ->
            parseOwnedSpecialization(specCtx, type, parseContext)
        }
    }

    /**
     * Parse an owned specialization.
     */
    private fun parseOwnedSpecialization(
        ctx: KerMLParser.OwnedSpecializationContext,
        type: Type,
        parseContext: ParseContext
    ) {
        val specialization = parseContext.create<Specialization>()
        specialization.specific = type

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val generalName = extractQualifiedName(qnCtx)
            // Register pending reference for general type
            parseContext.registerReference(specialization, "general", generalName)
        }
    }
}
