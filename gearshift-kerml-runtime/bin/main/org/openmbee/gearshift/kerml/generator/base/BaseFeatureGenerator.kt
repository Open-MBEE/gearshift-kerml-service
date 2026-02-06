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
package org.openmbee.gearshift.kerml.generator.base

import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Redefinition
import org.openmbee.gearshift.kerml.generator.GenerationContext

/**
 * Abstract base generator for Feature elements and their subclasses.
 *
 * Provides shared generation methods for:
 * - featurePrefix (direction, derived, abstract, composite, portion, var, const, end)
 * - featureDeclaration (identification, multiplicity, typings, subsettings, redefinitions)
 * - multiplicity ([n..m] ordered nonunique)
 * - typings (: Type1, Type2)
 * - subsettings (:> feature1, feature2)
 * - redefinitions (:>> feature1, feature2)
 * - valuePart (= expr, := expr, default expr)
 *
 * Mirrors BaseFeatureVisitor from the parser.
 */
abstract class BaseFeatureGenerator<T : Feature> : BaseTypeGenerator<T>() {

    /**
     * Generate the feature prefix (direction and modifiers).
     *
     * Grammar:
     * ```
     * basicFeaturePrefix
     *     : ( direction = featureDirection )?
     *       ( isDerived = DERIVED )?
     *       ( isAbstract = ABSTRACT )?
     *       ( isComposite = COMPOSITE | isPortion = PORTION )?
     *       ( isVariable = VAR | isConstant = CONST )?
     *     ;
     * endFeaturePrefix
     *     : ( isConstant = CONST )?
     *       isEnd = END
     *     ;
     * ```
     */
    protected open fun generateFeaturePrefix(feature: Feature): String = buildString {
        // Direction: in, out, inout
        feature.direction?.let { dir ->
            if (dir.isNotEmpty()) {
                append(dir)
                append(" ")
            }
        }

        // Modifiers in spec order
        if (feature.isDerived) append("derived ")
        if (feature.isAbstract) append("abstract ")

        // Composite/Portion are mutually exclusive
        when {
            feature.isComposite -> append("composite ")
            feature.isPortion -> append("portion ")
        }

        // Var/Const - readonly is the default (neither var nor const)
        when {
            feature.isConstant -> append("const ")
            feature.isVariable && !feature.isConstant -> append("var ")
        }

        // End feature
        if (feature.isEnd) append("end ")
    }

    /**
     * Generate the feature declaration part.
     *
     * Grammar:
     * ```
     * featureDeclaration
     *     : ( isSufficient = ALL )?
     *       ( featureIdentification ( featureSpecializationPart | conjugationPart )?
     *       | featureSpecializationPart
     *       | conjugationPart
     *       )
     *       featureRelationshipPart*
     *     ;
     * ```
     */
    protected open fun generateFeatureDeclaration(
        feature: Feature,
        context: GenerationContext
    ): String = buildString {
        // isSufficient prefix
        if (feature.isSufficient) {
            append("all ")
        }

        // Identification
        append(generateIdentification(feature))

        // Multiplicity: [n..m] ordered nonunique
        append(generateMultiplicity(feature))

        // Typings: : Type1, Type2
        val typings = generateTypings(feature, context)
        if (typings.isNotEmpty()) {
            append(typings)
        }

        // Subsettings: :> feature1, feature2 (excluding redefinitions)
        val subsettings = generateSubsettings(feature, context)
        if (subsettings.isNotEmpty()) {
            append(" ")
            append(subsettings)
        }

        // Redefinitions: :>> feature1, feature2
        val redefinitions = generateRedefinitions(feature, context)
        if (redefinitions.isNotEmpty()) {
            append(" ")
            append(redefinitions)
        }

        // Type relationship part (disjoint, unions, etc.)
        append(generateTypeRelationshipPart(feature, context))
    }

    /**
     * Generate the multiplicity part.
     *
     * Grammar: `'[' lowerBound '..' upperBound ']' ( 'ordered' )? ( 'nonunique' )?`
     *
     * @param feature The feature to generate multiplicity for
     * @return The multiplicity string (may be empty if no explicit multiplicity)
     */
    protected open fun generateMultiplicity(feature: Feature): String {
        val multiplicity = feature.multiplicity ?: return ""

        return buildString {
            // Get bounds from the multiplicity
            // TODO: Access actual bound values when available
            // For now, we'll leave this as a placeholder that can be enhanced

            // Ordered/Nonunique flags
            if (feature.isOrdered) append(" ordered")
            if (feature.isNonunique) append(" nonunique")
        }
    }

    /**
     * Generate the typings (feature typing relationships).
     *
     * Grammar: `:` Type1 `,` Type2
     */
    protected open fun generateTypings(feature: Feature, context: GenerationContext): String {
        val explicitTypings = feature.ownedTyping.filter { shouldEmitRelationship(it, context) }

        if (explicitTypings.isEmpty()) {
            return ""
        }

        val symbol = context.typedBySymbol()
        val typeNames = explicitTypings
            .mapNotNull { typing ->
                typing.type?.let { type ->
                    context.resolveDisplayName(type)
                }
            }
            .filter { it.isNotEmpty() }

        if (typeNames.isEmpty()) {
            return ""
        }

        return " $symbol ${typeNames.joinToString(", ")}"
    }

    /**
     * Generate the subsettings (excluding redefinitions).
     *
     * Grammar: `:>` feature1 `,` feature2
     */
    protected open fun generateSubsettings(feature: Feature, context: GenerationContext): String {
        // Filter out redefinitions (they are a subtype of Subsetting)
        val explicitSubsettings = feature.ownedSubsetting
            .filter { it !is Redefinition }
            .filter { shouldEmitRelationship(it, context) }

        if (explicitSubsettings.isEmpty()) {
            return ""
        }

        val symbol = context.subsetsSymbol()
        val featureNames = explicitSubsettings
            .mapNotNull { subsetting ->
                subsetting.subsettedFeature?.let { f ->
                    context.resolveDisplayName(f)
                }
            }
            .filter { it.isNotEmpty() }

        if (featureNames.isEmpty()) {
            return ""
        }

        return "$symbol ${featureNames.joinToString(", ")}"
    }

    /**
     * Generate the redefinitions.
     *
     * Grammar: `:>>` feature1 `,` feature2
     */
    protected open fun generateRedefinitions(feature: Feature, context: GenerationContext): String {
        val explicitRedefinitions = feature.ownedRedefinition
            .filter { shouldEmitRelationship(it, context) }

        if (explicitRedefinitions.isEmpty()) {
            return ""
        }

        val symbol = context.redefinesSymbol()
        val featureNames = explicitRedefinitions
            .mapNotNull { redefinition ->
                redefinition.redefinedFeature?.let { f ->
                    context.resolveDisplayName(f)
                }
            }
            .filter { it.isNotEmpty() }

        if (featureNames.isEmpty()) {
            return ""
        }

        return "$symbol ${featureNames.joinToString(", ")}"
    }

    /**
     * Generate the value part (default value or binding).
     *
     * Grammar:
     * ```
     * valuePart : featureValue ;
     * featureValue
     *     : ( '=' | isDefault = DEFAULT ( '=' | isInitial = ':=' )? ) ownedRelationship += FeatureValueExpression
     *     | isInitial = ':=' ownedRelationship += FeatureValueExpression
     *     ;
     * ```
     */
    protected open fun generateValuePart(feature: Feature, context: GenerationContext): String {
        // TODO: Implement when FeatureValue is accessible
        // This requires accessing the feature's value expression and generating it
        return ""
    }
}
