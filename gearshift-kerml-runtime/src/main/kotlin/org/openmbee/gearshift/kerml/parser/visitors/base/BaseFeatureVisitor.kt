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

import org.openmbee.gearshift.generated.interfaces.Conjugation
import org.openmbee.gearshift.generated.interfaces.EndFeatureMembership
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.FeatureChaining
import org.openmbee.gearshift.generated.interfaces.FeatureTyping
import org.openmbee.gearshift.generated.interfaces.FeatureValue
import org.openmbee.gearshift.generated.interfaces.MultiplicityRange
import org.openmbee.gearshift.generated.interfaces.Redefinition
import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.kerml.antlr.KerMLParser

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
        parseContext: ParseContext
    ) {
        ctx.isSufficient?.let { feature.isSufficient = true }

        ctx.featureIdentification()?.let { id ->
            parseFeatureIdentification(id, feature)
        }

        ctx.featureSpecializationPart()?.let { specPart ->
            parseFeatureSpecializationPart(specPart, feature, parseContext)
        }

        ctx.conjugationPart()?.let { conjPart ->
            val conjugation = parseContext.create<Conjugation>()
            conjugation.conjugatedType = feature
            conjPart.ownedConjugation()?.qualifiedName()?.let { qn ->
                val originalName = extractQualifiedName(qn)
                parseContext.registerReference(conjugation, "originalType", originalName)
            }
        }

        ctx.featureRelationshipPart()?.forEach { relPart ->
            parseFeatureRelationshipPart(relPart, feature, parseContext)
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
        parseContext: ParseContext
    ) {
        ctx.multiplicityPart()?.let { multPart ->
            parseMultiplicityPart(multPart, feature, parseContext)
        }

        ctx.featureSpecialization()?.forEach { spec ->
            parseFeatureSpecialization(spec, feature, parseContext)
        }
    }

    /**
     * Parse multiplicity part.
     */
    protected fun parseMultiplicityPart(ctx: KerMLParser.MultiplicityPartContext, feature: Feature, parseContext: ParseContext? = null) {
        ctx.isOrdered?.let { feature.isOrdered = true }
        ctx.isNonunique?.let { feature.isNonunique = true }
        parseContext?.let { pc ->
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
        parseContext: ParseContext
    ) {
        ctx.typings()?.let { typings ->
            parseTypings(typings, feature, parseContext)
        }

        ctx.subsettings()?.let { subsettings ->
            parseSubsettings(subsettings, feature, parseContext)
        }

        ctx.redefinitions()?.let { redefinitions ->
            parseRedefinitions(redefinitions, feature, parseContext)
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
        parseContext: ParseContext
    ) {
        ctx.typedBy()?.ownedFeatureTyping()?.let { typing ->
            parseOwnedFeatureTyping(typing, feature, parseContext)
        }

        ctx.ownedFeatureTyping()?.forEach { typing ->
            parseOwnedFeatureTyping(typing, feature, parseContext)
        }
    }

    /**
     * Parse owned feature typing.
     */
    protected fun parseOwnedFeatureTyping(
        ctx: KerMLParser.OwnedFeatureTypingContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        val featureTyping = parseContext.create<FeatureTyping>()
        featureTyping.typedFeature = feature

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val typeName = extractQualifiedName(qnCtx)
            // Register pending reference for type resolution
            parseContext.registerReference(featureTyping, "type", typeName)
        }
    }

    /**
     * Parse subsettings.
     */
    protected fun parseSubsettings(
        ctx: KerMLParser.SubsettingsContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        ctx.subsets()?.ownedSubsetting()?.let { subsetting ->
            parseOwnedSubsetting(subsetting, feature, parseContext)
        }

        ctx.ownedSubsetting()?.forEach { subsetting ->
            parseOwnedSubsetting(subsetting, feature, parseContext)
        }
    }

    /**
     * Parse owned subsetting.
     */
    protected fun parseOwnedSubsetting(
        ctx: KerMLParser.OwnedSubsettingContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        val subsetting = parseContext.create<Subsetting>()
        subsetting.subsettingFeature = feature

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val featureName = extractQualifiedName(qnCtx)
            // Register pending reference for subsetted feature
            parseContext.registerReference(subsetting, "subsettedFeature", featureName)
        }
    }

    /**
     * Parse redefinitions.
     */
    protected fun parseRedefinitions(
        ctx: KerMLParser.RedefinitionsContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        ctx.redefines()?.ownedRedefinition()?.let { redef ->
            parseOwnedRedefinition(redef, feature, parseContext)
        }

        ctx.ownedRedefinition()?.forEach { redef ->
            parseOwnedRedefinition(redef, feature, parseContext)
        }
    }

    /**
     * Parse owned redefinition.
     */
    protected fun parseOwnedRedefinition(
        ctx: KerMLParser.OwnedRedefinitionContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        val redefinition = parseContext.create<Redefinition>()
        redefinition.redefiningFeature = feature

        ctx.generalType()?.qualifiedName()?.let { qnCtx ->
            val featureName = extractQualifiedName(qnCtx)
            // Register pending reference for redefined feature (uses redefinition context)
            parseContext.registerReference(redefinition, "redefinedFeature", featureName, isRedefinitionContext = true)
        }
    }

    /**
     * Parse feature relationship part.
     */
    protected fun parseFeatureRelationshipPart(
        ctx: KerMLParser.FeatureRelationshipPartContext,
        feature: Feature,
        parseContext: ParseContext
    ) {
        ctx.typeRelationshipPart()?.let { relPart ->
            parseTypeRelationshipPart(relPart, feature, parseContext)
        }

        ctx.chainingPart()?.let { chainingPart ->
            chainingPart.ownedFeatureChaining()?.let { ownedChaining ->
                val chaining = parseContext.create<FeatureChaining>()
                ownedChaining.chainingFeature?.let { qn ->
                    val name = extractQualifiedName(qn)
                    parseContext.registerReference(chaining, "chainingFeature", name)
                }
            }
            chainingPart.featureChain()?.ownedFeatureChaining()?.forEach { ownedChaining ->
                val chaining = parseContext.create<FeatureChaining>()
                ownedChaining.chainingFeature?.let { qn ->
                    val name = extractQualifiedName(qn)
                    parseContext.registerReference(chaining, "chainingFeature", name)
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
        parseContext: ParseContext
    ) {
        ctx?.featureValue()?.let { fvCtx ->
            val featureValue = parseContext.create<FeatureValue>()
            fvCtx.isDefault?.let { featureValue.isDefault = true }
            fvCtx.isInitial?.let { featureValue.isInitial = true }
            // ownedExpression parsing requires full expression visitor infrastructure
        }
    }
}
