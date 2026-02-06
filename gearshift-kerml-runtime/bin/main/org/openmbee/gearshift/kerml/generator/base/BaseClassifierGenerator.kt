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

import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.kerml.generator.GenerationContext

/**
 * Abstract base generator for Classifier elements and their subclasses.
 *
 * Provides shared generation methods for:
 * - classifierDeclaration (identification + specialization)
 * - subclassifications (:> SuperClass1, SuperClass2)
 *
 * Mirrors BaseClassifierVisitor from the parser.
 */
abstract class BaseClassifierGenerator<T : Classifier> : BaseTypeGenerator<T>() {

    /**
     * Generate the classifier declaration part.
     *
     * Grammar: `classifierDeclaration = ( isSufficient = 'all' )? Identification
     *           ( ownedRelationship += OwnedSubclassification
     *             ( ',' ownedRelationship += OwnedSubclassification )* )?`
     *
     * @param classifier The classifier to generate declaration for
     * @param context The generation context
     * @return The declaration string
     */
    protected open fun generateClassifierDeclaration(
        classifier: Classifier,
        context: GenerationContext
    ): String = buildString {
        // isSufficient prefix
        if (classifier.isSufficient) {
            append("all ")
        }

        // Identification
        append(generateIdentification(classifier))

        // Subclassifications
        val subclassifications = generateSubclassifications(classifier, context)
        if (subclassifications.isNotEmpty()) {
            append(" ")
            append(subclassifications)
        }

        // Type relationship part (disjoint, unions, etc.)
        append(generateTypeRelationshipPart(classifier, context))
    }

    /**
     * Generate the subclassification relationships.
     *
     * Grammar: `:>` SuperClass1 `,` SuperClass2
     *
     * @param classifier The classifier to generate subclassifications for
     * @param context The generation context
     * @return The subclassification string (without leading space)
     */
    protected open fun generateSubclassifications(
        classifier: Classifier,
        context: GenerationContext
    ): String {
        val explicitSubclassifications = classifier.ownedSubclassification
            .filter { shouldEmitRelationship(it, context) }

        if (explicitSubclassifications.isEmpty()) {
            return ""
        }

        val symbol = context.specializesSymbol()
        val superclassNames = explicitSubclassifications
            .mapNotNull { subclassification ->
                subclassification.superclassifier.let { superclass ->
                    context.resolveDisplayName(superclass)
                }
            }
            .filter { it.isNotEmpty() }

        if (superclassNames.isEmpty()) {
            return ""
        }

        return "$symbol ${superclassNames.joinToString(", ")}"
    }
}
