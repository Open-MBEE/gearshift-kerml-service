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
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML AssignmentActionUsage metaclass.
 * Specializes: ActionUsage
 * An AssignmentActionUsage is an ActionUsage that is defined, directly or indirectly, by the
 * ActionDefinition AssignmentAction from the Systems Model Library. It specifies that the value of the
 * referent Feature, relative to the target given by the result of the targetArgument Expression, should be set
 * to the result of the valueExpression.
 */
fun createAssignmentActionUsageMetaClass() = MetaClass(
    name = "AssignmentActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAssignmentActionUsageAccessedFeatureRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let targetParameter : Feature = inputParameter(1) in
                targetParameter <> null and
                targetParameter.ownedFeature->notEmpty() and
                targetParameter->first().ownedFeature->notEmpty() and
                targetParameter->first().ownedFeature->first().
                    redefines('AssignmentAction::target::startingAt::accessedFeature')
            """.trimIndent(),
            description = "The first ownedFeature of the first ownedFeature of the first parameter of an AssignmentActionUsage must redefine AssignmentAction::target::startingAt::accessedFeature."
        ),
        MetaConstraint(
            name = "checkAssignmentActionUsageReferentRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let targetParameter : Feature = inputParameter(1) in
                targetParameter <> null and
                targetParameter.ownedFeature->notEmpty() and
                targetParameter->first().ownedFeature->notEmpty() and
                targetParameter->first().ownedFeature->first().redefines(referent)
            """.trimIndent(),
            description = "The first ownedFeature of the first ownedFeature of the first parameter of an AssignmentActionUsage must redefine the referent of the AssignmentActionUsage."
        ),
        MetaConstraint(
            name = "checkAssignmentActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::assignmentActions')",
            description = "An AssignmentActionUsage must directly or indirectly specialize the ActionUsage Actions::assignmentActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkAssignmentActionUsageStartingAtRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let targetParameter : Feature = inputParameter(1) in
                targetParameter <> null and
                targetParameter.ownedFeature->notEmpty() and
                targetParameter.ownedFeature->first().
                    redefines('AssignmentAction::target::startingAt')
            """.trimIndent(),
            description = "The first ownedFeature of the first parameter of an AssignmentActionUsage must redefine AssignmentAction::target::startingAt."
        ),
        MetaConstraint(
            name = "checkAssignmentActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::assignments')
            """.trimIndent(),
            description = "A composite AssignmentActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage Actions::Action::assignments from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveAssignmentActionUsageReferent",
            type = ConstraintType.DERIVATION,
            expression = """
                let unownedFeatures : Sequence(Feature) = ownedMembership->
                    reject(oclIsKindOf(FeatureMembership)).memberElement->
                    selectByKind(Feature) in
                if unownedFeatures->isEmpty() then null
                else unownedFeatures->first().oclAsType(Feature)
                endif
            """.trimIndent(),
            description = "The referent of an AssignmentActionUsage is the first Feature that is the memberElement of an ownedMembership that is not a FeatureMembership."
        ),
        MetaConstraint(
            name = "deriveAssignmentActionUsageValueExpression",
            type = ConstraintType.DERIVATION,
            expression = "argument(2)",
            description = "The valueExpression of an AssignmentActionUsage is its second argument Expression."
        ),
        MetaConstraint(
            name = "deriveAssignmentUsageTargetArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(1)",
            description = "The targetArgument of an AssignmentActionUsage is its first argument Expression."
        ),
        MetaConstraint(
            name = "validateAssignmentActionUsage",
            type = ConstraintType.VERIFICATION,
            expression = "referent <> null implies referent.featureTarget.mayTimeVary",
            description = "The featureTarget of the referent of an AssignmentActionUsage must be able to have time-varying values."
        ),
        MetaConstraint(
            name = "validateAssignmentActionUsageReferent",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMembership->exists(
                    not oclIsKindOf(OwningMembership) and
                    memberElement.oclIsKindOf(Feature))
            """.trimIndent(),
            description = "An AssignmentActionUsage must have an ownedMembership that is not an OwningMembership and whose memberElement is a Feature."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "assignmentActionUsageAssignmentActionsBinding",
            baseConcept = "Actions::assignmentActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "assignmentActionUsageAssignmentsBinding",
            baseConcept = "Actions::Action::assignments",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "An AssignmentActionUsage is an ActionUsage that specifies that the value of the referent Feature should be set to the result of the valueExpression."
)
