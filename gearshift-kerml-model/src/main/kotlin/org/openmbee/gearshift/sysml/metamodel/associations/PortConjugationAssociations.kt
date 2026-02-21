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
package org.openmbee.gearshift.sysml.metamodel.associations

import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 17: Port Conjugation
 */
fun createPortConjugationAssociations(): List<MetaAssociation> {

    // ConjugatedPortTyping has portDefinition : PortDefinition [1..1] {derived}
    val conjugatedPortTypingPortDefinitionAssociation = MetaAssociation(
        name = "conjugatedPortTypingPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "conjugatedPortTyping",
            type = "ConjugatedPortTyping",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "portDefinition",
            type = "PortDefinition",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveConjugatedPortTypingPortDefinition"
        )
    )

    // ConjugatedPortTyping has conjugatedPortDefinition : ConjugatedPortDefinition [1..1] {derived, redefines type}
    val typingByConjugatedPortConjugatedPortDefinitionAssociation = MetaAssociation(
        name = "typingByConjugatedPortConjugatedPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typingByConjugatedPort",
            type = "ConjugatedPortTyping",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("typingByType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "conjugatedPortDefinition",
            type = "ConjugatedPortDefinition",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("type"),
            derivationConstraint = "deriveConjugatedPortTypingConjugatedPortDefinition"
        )
    )

    // PortDefinition has conjugatedPortDefinition : ConjugatedPortDefinition [0..1] {derived}
    val originalPortDefinitionConjugatedPortDefinitionAssociation = MetaAssociation(
        name = "originalPortDefinitionConjugatedPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "originalPortDefinition",
            type = "PortDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "conjugatedPortDefinition",
            type = "ConjugatedPortDefinition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "derivePortDefinitionConjugatedPortDefinition"
        )
    )

    // PortConjugation has originalPortDefinition : PortDefinition [1..1] {derived, redefines originalType}
    val portConjugationOriginalPortDefinitionAssociation = MetaAssociation(
        name = "portConjugationOriginalPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "portConjugation",
            type = "PortConjugation",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("conjugation"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "originalPortDefinition",
            type = "PortDefinition",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("originalType"),
            derivationConstraint = "derivePortConjugationOriginalPortDefinition"
        )
    )

    // ConjugatedPortDefinition has ownedPortConjugator : PortConjugation [1..1] {derived, subsets ownedConjugator}
    val ownedPortConjugatorConjugatedPortDefinitionAssociation = MetaAssociation(
        name = "ownedPortConjugatorConjugatedPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningConjugatedPortDefinition",
            type = "ConjugatedPortDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedPortConjugator",
            type = "PortConjugation",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("ownedConjugator"),
            derivationConstraint = "deriveConjugatedPortDefinitionOwnedPortConjugator"
        )
    )

    return listOf(
        conjugatedPortTypingPortDefinitionAssociation,
        originalPortDefinitionConjugatedPortDefinitionAssociation,
        ownedPortConjugatorConjugatedPortDefinitionAssociation,
        portConjugationOriginalPortDefinitionAssociation,
        typingByConjugatedPortConjugatedPortDefinitionAssociation
    )
}
