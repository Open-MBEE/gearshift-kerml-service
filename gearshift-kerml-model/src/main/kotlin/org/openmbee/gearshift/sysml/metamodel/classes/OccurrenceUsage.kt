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
 * SysML OccurrenceUsage metaclass.
 * Specializes: Usage
 * An OccurrenceUsage is a Usage whose types are all Classes. Nominally, if a type is an
 * OccurrenceDefinition, an OccurrenceUsage is a Usage of that OccurrenceDefinition within a system.
 * However, other types of Kernel Classes are also allowed, to permit use of Classes from the
 * Kernel Model Libraries.
 */
fun createOccurrenceUsageMetaClass() = MetaClass(
    name = "OccurrenceUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = listOf(
        MetaProperty(
            name = "isIndividual",
            type = "Boolean",
            description = "Whether this OccurrenceUsage represents the usage of the specific individual represented by its individualDefinition."
        ),
        MetaProperty(
            name = "portionKind",
            type = "PortionKind",
            lowerBound = 0,
            description = "The kind of temporal portion (time slice or snapshot) represented by this OccurrenceUsage. If portionKind is not null, then the owningType of the OccurrenceUsage must be non-null."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkOccurrenceUsageSnapshotSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                portionKind = PortionKind::snapshot implies
                specializesFromLibrary('Occurrences::Occurrence::snapshots')
            """.trimIndent(),
            description = "If an OccurrenceUsage has portionKind = snapshot, then it must directly or indirectly specialize Occurrences::Occurrence::snapshots."
        ),
        MetaConstraint(
            name = "checkOccurrenceUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Occurrences::occurrences')",
            description = "An OccurrenceUsage must directly or indirectly specialize Occurrences::occurrences from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkOccurrenceUsageSuboccurrenceSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and
                owningType <> null and
                (owningType.oclIsKindOf(Class) or
                owningType.oclIsKindOf(OccurrenceUsage) or
                owningType.oclIsKindOf(Feature) and
                    owningType.oclAsType(Feature).type->
                    exists(oclIsKind(Class))) implies
                specializesFromLibrary('Occurrences::Occurrence::suboccurrences')
            """.trimIndent(),
            description = "A composite OccurrenceUsage, whose owningType is a Class, another OccurrenceUsage, or any kind of Feature typed by a Class, must directly or indirectly specialize Occurrences::Occurrence::suboccurrences."
        ),
        MetaConstraint(
            name = "checkOccurrenceUsageTimeSliceSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                portionKind = PortionKind::timeslice implies
                specializesFromLibrary('Occurrences::Occurrence::timeSlices')
            """.trimIndent(),
            description = "If an OccurrenceUsage has portionKind = timeslice, then it must directly or indirectly specialize Occurrences::Occurrence::timeSlices."
        ),
        MetaConstraint(
            name = "deriveOccurrenceUsageIndividualDefinition",
            type = ConstraintType.DERIVATION,
            expression = """
                let individualDefinitions : OrderedSet(OccurrenceDefinition) =
                    occurrenceDefinition->
                    selectByKind(OccurrenceDefinition)->
                    select(isIndividual) in
                if individualDefinitions->isEmpty() then null
                else individualDefinitions->first() endif
            """.trimIndent(),
            description = "The individualDefinition of an OccurrenceUsage is the occurrenceDefinition that is an OccurrenceDefinition with isIndividual = true, if any."
        ),
        MetaConstraint(
            name = "validateOccurrenceUsageIndividualDefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                occurrenceDefinition->
                selectByKind(OccurrenceDefinition)->
                select(isIndividual).size() <= 1
            """.trimIndent(),
            description = "An OccurrenceUsage must have at most one occurrenceDefinition with isIndividual = true."
        ),
        MetaConstraint(
            name = "validateOccurrenceUsageIndividualUsage",
            type = ConstraintType.VERIFICATION,
            expression = "isIndividual implies individualDefinition <> null",
            description = "If an OccurrenceUsage has isIndividual = true, then it must have an individualDefinition."
        ),
        MetaConstraint(
            name = "validateOccurrenceUsageIsPortion",
            type = ConstraintType.VERIFICATION,
            expression = "portionKind <> null implies isPortion",
            description = "If an OccurrenceUsage has a non-null portionKind, then it must have isPortion = true."
        ),
        MetaConstraint(
            name = "validateOccurrenceUsagePortionKind",
            type = ConstraintType.VERIFICATION,
            expression = """
                portionKind <> null implies
                owningType <> null and
                (owningType.oclIsKindOf(OccurrenceDefinition) or
                owningType.oclIsKindOf(OccurrenceUsage))
            """.trimIndent(),
            description = "If an OccurrenceUsage has a non-null portionKind, then its owningType must be an OccurrenceDefinition or an OccurrenceUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "occurrenceUsageOccurrencesBinding",
            baseConcept = "Occurrences::occurrences",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "occurrenceUsageSuboccurrencesBinding",
            baseConcept = "Occurrences::Occurrence::suboccurrences",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("Class"),
                            BindingCondition.OwningTypeIs("OccurrenceUsage"),
                            BindingCondition.OwningTypeTypedBy("Class")
                        )
                    )
                )
            )
        ),
        SemanticBinding(
            name = "occurrenceUsageSnapshotsBinding",
            baseConcept = "Occurrences::Occurrence::snapshots",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("portionKind", "snapshot")
        ),
        SemanticBinding(
            name = "occurrenceUsageTimeSlicesBinding",
            baseConcept = "Occurrences::Occurrence::timeSlices",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("portionKind", "timeslice")
        )
    ),
    description = "An OccurrenceUsage is a Usage whose types are all Classes."
)
