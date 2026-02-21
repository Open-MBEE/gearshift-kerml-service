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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML EventOccurrenceUsage metaclass.
 * Specializes: OccurrenceUsage
 * An EventOccurrenceUsage is an OccurrenceUsage that represents another OccurrenceUsage occurring
 * as a suboccurrence of the containing occurrence of the EventOccurrenceUsage. Unless it is the
 * EventOccurrenceUsage itself, the referenced OccurrenceUsage is related to the
 * EventOccurrenceUsage by a ReferenceSubsetting Relationship.
 */
fun createEventOccurrenceUsageMetaClass() = MetaClass(
    name = "EventOccurrenceUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage"),
    attributes = listOf(
        MetaProperty(
            name = "isReference",
            type = "Boolean",
            isDerived = true,
            redefines = listOf("isReference"),
            derivationConstraint = "deriveEventOccurrenceUsageIsReference",
            description = "Always true for an EventOccurrenceUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkEventOccurrenceUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(OccurrenceDefinition) or
                owningType.oclIsKindOf(OccurrenceUsage)) implies
                specializesFromLibrary('Occurrences::Occurrence::timeEnclosedOccurrences')
            """.trimIndent(),
            description = "If an EventOccurrenceUsage has an owningType that is an OccurrenceDefinition or OccurrenceUsage, then it must directly or indirectly specialize Occurrences::Occurrence::timeEnclosedOccurrences."
        ),
        MetaConstraint(
            name = "deriveEventOccurrenceUsageEventOccurrence",
            type = ConstraintType.DERIVATION,
            expression = """
                if referencedFeatureTarget() = null then self
                else if referencedFeatureTarget().oclIsKindOf(OccurrenceUsage) then
                    referencedFeatureTarget().oclAsType(OccurrenceUsage)
                else null
                endif endif
            """.trimIndent(),
            description = "If an EventOccurrenceUsage has no ownedReferenceSubsetting, then its eventOccurrence is the EventOccurrenceUsage itself. Otherwise, the eventOccurrence is the featureTarget of the referencedFeature of the ownedReferenceSubsetting."
        ),
        MetaConstraint(
            name = "deriveEventOccurrenceUsageIsReference",
            type = ConstraintType.DERIVATION,
            expression = "true",
            description = "Always true for an EventOccurrenceUsage."
        ),
        MetaConstraint(
            name = "validateEventOccurrenceUsageIsReference",
            type = ConstraintType.VERIFICATION,
            expression = "isReference",
            description = "An EventOccurrenceUsage must be referential."
        ),
        MetaConstraint(
            name = "validateEventOccurrenceUsageReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                referencedFeatureTarget() <> null implies
                referencedFeatureTarget().oclIsKindOf(OccurrenceUsage)
            """.trimIndent(),
            description = "If an EventOccurrenceUsage has an ownedReferenceSubsetting, then the featureTarget of the referencedFeature must be an OccurrenceUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "eventOccurrenceUsageTimeEnclosedOccurrencesBinding",
            baseConcept = "Occurrences::Occurrence::timeEnclosedOccurrences",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("OccurrenceDefinition"),
                    BindingCondition.OwningTypeIs("OccurrenceUsage")
                )
            )
        )
    ),
    description = "An EventOccurrenceUsage is an OccurrenceUsage that represents another OccurrenceUsage occurring as a suboccurrence of the containing occurrence."
)
