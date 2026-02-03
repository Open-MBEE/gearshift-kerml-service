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

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext

/**
 * Abstract base visitor for Feature elements and their subclasses.
 *
 * Extends BaseTypeVisitor with feature-specific parsing:
 * - featurePrefix (basicFeaturePrefix, endFeaturePrefix)
 * - featureDeclaration (featureIdentification, featureSpecializationPart)
 * - typings, subsettings, redefinitions
 * - valuePart
 *
 * This follows the KerML hierarchy where Feature extends Type.
 *
 * @param Ctx The ANTLR parser context type
 * @param Result The typed wrapper type (must extend Feature)
 */
abstract class BaseFeatureVisitor<Ctx, Result : Feature> : BaseTypeVisitor<Ctx, Result>() {

    /**
     * Parse feature prefix from a FeaturePrefixContext.
     */
    protected fun parseFeaturePrefixContext(ctx: KerMLParser.FeaturePrefixContext?, feature: Feature) {
        ctx?.let { prefix ->
            prefix.basicFeaturePrefix()?.let { basicPrefix ->
                parseBasicFeaturePrefix(basicPrefix, feature)
            }
            prefix.endFeaturePrefix()?.let { endPrefix ->
                parseEndFeaturePrefix(endPrefix, feature)
            }
        }
    }

    /**
     * Parse basic feature prefix.
     *
     * Grammar:
     * basicFeaturePrefix
     *     : ( direction = featureDirection )?
     *       ( isDerived = DERIVED )?
     *       ( isAbstract = ABSTRACT )?
     *       ( isComposite = COMPOSITE | isPortion = PORTION )?
     *       ( isVariable = VAR | isConstant = CONST )?
     *     ;
     */
    protected fun parseBasicFeaturePrefix(ctx: KerMLParser.BasicFeaturePrefixContext, feature: Feature) {
        ctx.direction?.let { feature.direction = it.text }
        ctx.isDerived?.let { feature.isDerived = true }
        ctx.isAbstract?.let { feature.isAbstract = true }
        ctx.isComposite?.let { feature.isComposite = true }
        ctx.isPortion?.let { feature.isPortion = true }
        ctx.isVariable?.let { feature.isVariable = true }
        ctx.isConstant?.let {
            feature.isConstant = true
            feature.isVariable = true // const implies variable
        }
    }

    /**
     * Parse end feature prefix.
     *
     * Grammar:
     * endFeaturePrefix
     *     : ( isConstant = CONST )?
     *       isEnd = END
     *     ;
     */
    protected fun parseEndFeaturePrefix(ctx: KerMLParser.EndFeaturePrefixContext, feature: Feature) {
        feature.isEnd = true
        ctx.isConstant?.let {
            feature.isConstant = true
            feature.isVariable = true
        }
    }

    /**
     * Parse feature declaration.
     *
     * Grammar:
     * featureDeclaration
     *     : ( isSufficient = ALL )?
     *       ( featureIdentification ( featureSpecializationPart | conjugationPart )?
     *       | featureSpecializationPart
     *       | conjugationPart
     *       )
     *       featureRelationshipPart*
     *     ;
     */
    protected fun parseFeatureDeclaration(
        ctx: KerMLParser.FeatureDeclarationContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.isSufficient?.let { feature.isSufficient = true }

        ctx.featureIdentification()?.let { id ->
            parseFeatureIdentification(id, feature)
        }

        ctx.featureSpecializationPart()?.let { specPart ->
            parseFeatureSpecializationPart(specPart, feature, kermlParseContext)
        }

        ctx.conjugationPart()?.let { conjPart ->
            val conjugation = kermlParseContext.create<Conjugation>()
            conjugation.conjugatedType = feature
            conjPart.ownedConjugation()?.qualifiedName()?.let { qn ->
                val originalName = extractQualifiedName(qn)
                kermlParseContext.registerReference(conjugation, "originalType", originalName)
            }
        }

        ctx.featureRelationshipPart()?.forEach { relPart ->
            parseFeatureRelationshipPart(relPart, feature, kermlParseContext)
        }
    }

    /**
     * Parse feature identification.
     */
    protected fun parseFeatureIdentification(ctx: KerMLParser.FeatureIdentificationContext, feature: Feature) {
        ctx.declaredShortName?.let { feature.declaredShortName = it.text }
        ctx.declaredName?.let { feature.declaredName = it.text }
    }

    /**
     * Parse feature specialization part.
     */
    protected fun parseFeatureSpecializationPart(
        ctx: KerMLParser.FeatureSpecializationPartContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.multiplicityPart()?.let { multPart ->
            parseMultiplicityPart(multPart, feature, kermlParseContext)
        }

        ctx.featureSpecialization()?.forEach { spec ->
            parseFeatureSpecialization(spec, feature, kermlParseContext)
        }
    }

    /**
     * Parse multiplicity part.
     */
    protected fun parseMultiplicityPart(
        ctx: KerMLParser.MultiplicityPartContext,
        feature: Feature,
        kermlParseContext: KermlParseContext? = null
    ) {
        ctx.isOrdered?.let { feature.isOrdered = true }
        ctx.isNonunique?.let { feature.isNonunique = true }
        kermlParseContext?.let { pc ->
            ctx.ownedMultiplicity()?.let { mult ->
                org.openmbee.gearshift.kerml.parser.visitors.MultiplicityVisitor().parseOwnedMultiplicity(mult, pc)
            }
        }
    }

    /**
     * Parse feature specialization.
     */
    protected fun parseFeatureSpecialization(
        ctx: KerMLParser.FeatureSpecializationContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.typings()?.let { typings ->
            parseTypings(typings, feature, kermlParseContext)
        }

        ctx.subsettings()?.let { subsettings ->
            parseSubsettings(subsettings, feature, kermlParseContext)
        }

        ctx.redefinitions()?.let { redefinitions ->
            parseRedefinitions(redefinitions, feature, kermlParseContext)
        }

        ctx.references()?.let { _ ->
            // TODO: Parse reference subsettings
        }

        ctx.crosses()?.let { _ ->
            // TODO: Parse cross subsettings
        }
    }

    /**
     * Parse typings.
     */
    protected fun parseTypings(
        ctx: KerMLParser.TypingsContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.typedBy()?.ownedFeatureTyping()?.let { typing ->
            parseOwnedFeatureTyping(typing, feature, kermlParseContext)
        }

        ctx.ownedFeatureTyping()?.forEach { typing ->
            parseOwnedFeatureTyping(typing, feature, kermlParseContext)
        }
    }

    /**
     * Parse owned feature typing.
     */
    protected fun parseOwnedFeatureTyping(
        ctx: KerMLParser.OwnedFeatureTypingContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        val featureTyping = kermlParseContext.create<FeatureTyping>()
        featureTyping.typedFeature = feature

        // Establish ownership - link FeatureTyping as owned by the Feature
        // This makes it accessible via feature.ownedTyping (derived from ownedRelationship)
        kermlParseContext.engine.link(
            sourceId = feature.id!!,
            targetId = featureTyping.id!!,
            associationName = "owningRelatedElementOwnedRelationshipAssociation"
        )
        // Also link via base association for Specialization.owningType
        kermlParseContext.engine.link(
            sourceId = feature.id!!,
            targetId = featureTyping.id!!,
            associationName = "owningTypeOwnedSpecializationAssociation"
        )

        // Link FeatureTyping -> Feature via the typing association
        kermlParseContext.engine.link(
            sourceId = featureTyping.id!!,
            targetId = feature.id!!,
            associationName = "typingTypedFeatureAssociation"
        )

        // Also link via base association for Specialization.specific
        kermlParseContext.engine.link(
            sourceId = featureTyping.id!!,
            targetId = feature.id!!,
            associationName = "specializationSpecificAssociation"
        )

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val typeName = extractQualifiedName(qnCtx)
            // Register pending reference for type resolution
            kermlParseContext.registerReference(featureTyping, "type", typeName)
        }
    }

    /**
     * Parse subsettings.
     */
    protected fun parseSubsettings(
        ctx: KerMLParser.SubsettingsContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.subsets()?.ownedSubsetting()?.let { subsetting ->
            parseOwnedSubsetting(subsetting, feature, kermlParseContext)
        }

        ctx.ownedSubsetting()?.forEach { subsetting ->
            parseOwnedSubsetting(subsetting, feature, kermlParseContext)
        }
    }

    /**
     * Parse owned subsetting.
     */
    protected fun parseOwnedSubsetting(
        ctx: KerMLParser.OwnedSubsettingContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        val subsetting = kermlParseContext.create<Subsetting>()
        subsetting.subsettingFeature = feature

        // Establish ownership - link Subsetting as owned by the Feature
        // This makes it accessible via feature.ownedSubsetting
        kermlParseContext.engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningFeatureOwnedSubsettingAssociation"
        )
        // Also link via base associations for Type and Element ownership
        kermlParseContext.engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningTypeOwnedSpecializationAssociation"
        )
        kermlParseContext.engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningRelatedElementOwnedRelationshipAssociation"
        )

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val featureName = extractQualifiedName(qnCtx)
            // Register pending reference for subsetted feature
            kermlParseContext.registerReference(subsetting, "subsettedFeature", featureName)
        }
    }

    /**
     * Parse redefinitions.
     */
    protected fun parseRedefinitions(
        ctx: KerMLParser.RedefinitionsContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.redefines()?.ownedRedefinition()?.let { redef ->
            parseOwnedRedefinition(redef, feature, kermlParseContext)
        }

        ctx.ownedRedefinition()?.forEach { redef ->
            parseOwnedRedefinition(redef, feature, kermlParseContext)
        }
    }

    /**
     * Parse owned redefinition.
     */
    protected fun parseOwnedRedefinition(
        ctx: KerMLParser.OwnedRedefinitionContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        val redefinition = kermlParseContext.create<Redefinition>()
        redefinition.redefiningFeature = feature

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val featureName = extractQualifiedName(qnCtx)
            // Register pending reference for redefined feature (uses redefinition context)
            kermlParseContext.registerReference(
                redefinition,
                "redefinedFeature",
                featureName,
                isRedefinitionContext = true
            )
        }
    }

    /**
     * Parse feature relationship part.
     */
    protected fun parseFeatureRelationshipPart(
        ctx: KerMLParser.FeatureRelationshipPartContext,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx.typeRelationshipPart()?.let { relPart ->
            parseTypeRelationshipPart(relPart, feature, kermlParseContext)
        }

        ctx.chainingPart()?.let { chainingPart ->
            chainingPart.ownedFeatureChaining()?.let { ownedChaining ->
                val chaining = kermlParseContext.create<FeatureChaining>()
                ownedChaining.chainingFeature?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(chaining, "chainingFeature", name)
                }
            }
            chainingPart.featureChain()?.ownedFeatureChaining()?.forEach { ownedChaining ->
                val chaining = kermlParseContext.create<FeatureChaining>()
                ownedChaining.chainingFeature?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(chaining, "chainingFeature", name)
                }
            }
        }
    }

    /**
     * Parse value part.
     */
    protected fun parseValuePart(
        ctx: KerMLParser.ValuePartContext?,
        feature: Feature,
        kermlParseContext: KermlParseContext
    ) {
        ctx?.featureValue()?.let { fvCtx ->
            val featureValue = kermlParseContext.create<FeatureValue>()
            fvCtx.isDefault?.let { featureValue.isDefault = true }
            fvCtx.isInitial?.let { featureValue.isInitial = true }
            // ownedExpression parsing requires full expression visitor infrastructure
        }
    }
}
