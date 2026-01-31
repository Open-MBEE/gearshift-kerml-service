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

import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.generated.interfaces.Conjugation
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.kerml.antlr.KerMLParser

/**
 * Abstract base visitor for Classifier elements and their subclasses.
 *
 * Extends BaseTypeVisitor with classifier-specific parsing:
 * - classifierDeclaration (with superclassingPart)
 * - Subclassification relationships
 *
 * This follows the KerML hierarchy where Classifier extends Type.
 *
 * @param Ctx The ANTLR parser context type
 * @param Result The typed wrapper type (must extend Classifier)
 */
abstract class BaseClassifierVisitor<Ctx, Result : Classifier> : BaseTypeVisitor<Ctx, Result>() {

    /**
     * Parse a classifier declaration.
     *
     * Grammar:
     * classifierDeclaration
     *     : ( isSufficient=ALL )? identification
     *       ownedMultiplicity?
     *       ( superclassingPart | conjugationPart )?
     *       typeRelationshipPart*
     *     ;
     */
    protected fun parseClassifierDeclaration(
        decl: KerMLParser.ClassifierDeclarationContext,
        classifier: Classifier,
        parseContext: ParseContext
    ) {
        // Parse identification
        decl.identification()?.let { id ->
            parseIdentification(id, classifier)
        }

        // Parse isSufficient
        decl.isSufficient?.let {
            classifier.isSufficient = true
        }

        // Parse multiplicity
        decl.ownedMultiplicity()?.let { mult ->
            org.openmbee.gearshift.kerml.parser.visitors.MultiplicityVisitor()
                .parseOwnedMultiplicity(mult, parseContext)
        }

        // Parse superclassing (subclassification relationships)
        decl.superclassingPart()?.let { superclassing ->
            parseSuperclassingPart(superclassing, classifier, parseContext)
        }

        // Parse conjugation if present (alternative to superclassing)
        decl.conjugationPart()?.let { conjPart ->
            val conjugation = parseContext.create<Conjugation>()
            conjugation.conjugatedType = classifier
            conjPart.ownedConjugation()?.qualifiedName()?.let { qn ->
                val originalName = extractQualifiedName(qn)
                parseContext.registerReference(conjugation, "originalType", originalName)
            }
        }

        // Parse type relationship parts
        decl.typeRelationshipPart()?.forEach { relPart ->
            parseTypeRelationshipPart(relPart, classifier, parseContext)
        }
    }

    /**
     * Parse superclassing part (subclassification relationships).
     *
     * Grammar:
     * superclassingPart
     *     : specializesToken ownedSubclassification
     *       ( COMMA ownedSubclassification )*
     *     ;
     */
    protected fun parseSuperclassingPart(
        ctx: KerMLParser.SuperclassingPartContext,
        classifier: Classifier,
        parseContext: ParseContext
    ) {
        ctx.ownedSubclassification()?.forEach { subclassCtx ->
            parseOwnedSubclassification(subclassCtx, classifier, parseContext)
        }
    }

    /**
     * Parse an owned subclassification (creates Subclassification relationship).
     *
     * Grammar:
     * ownedSubclassification
     *     : superclassifier=qualifiedName
     *     ;
     */
    protected fun parseOwnedSubclassification(
        ctx: KerMLParser.OwnedSubclassificationContext,
        classifier: Classifier,
        parseContext: ParseContext
    ) {
        val subclassification = parseContext.create<Subclassification>()
        subclassification.subclassifier = classifier

        // Establish ownership - link Subclassification as owned by the Classifier
        // This makes it accessible via classifier.ownedSpecialization
        parseContext.engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningClassifierOwnedSubclassificationAssociation"
        )
        // Also link via base associations for Type and Element ownership
        parseContext.engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningTypeOwnedSpecializationAssociation"
        )
        parseContext.engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningRelatedElementOwnedRelationshipAssociation"
        )

        ctx.qualifiedName()?.let { qnCtx ->
            val superclassName = extractQualifiedName(qnCtx)
            // Register pending reference for superclassifier
            parseContext.registerReference(subclassification, "superclassifier", superclassName)
        }
    }
}
