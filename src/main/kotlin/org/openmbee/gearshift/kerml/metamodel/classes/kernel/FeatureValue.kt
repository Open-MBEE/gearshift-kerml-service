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
package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML FeatureValue metaclass.
 * Specializes: OwningMembership
 * A FeatureValue is a Membership that identifies a particular memberExpression that provides the value
 * of the Feature that owns the FeatureValue. The value is specified as either a bound value or an initial
 * value, and as either a concrete or default value. A Feature can have at most one FeatureValue.
 */
fun createFeatureValueMetaClass() = MetaClass(
    name = "FeatureValue",
    isAbstract = false,
    superclasses = listOf("OwningMembership"),
    attributes = listOf(
        MetaProperty(
            name = "isDefault",
            type = "Boolean",
            description = "Whether this FeatureValue is a concrete specification of the bound or initial value of the featureWithValue, or just a default value that may be overridden."
        ),
        MetaProperty(
            name = "isInitial",
            type = "Boolean",
            description = "Whether this FeatureValue specifies a bound value or an initial value for the featureWithValue."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveFeatureValueFeatureWithValue",
            type = ConstraintType.DERIVATION,
            expression = "membershipOwningNamespace.oclAsType(Feature)",
            description = "The featureWithValue of a FeatureValue is its membershipOwningNamespace, which must be a Feature."
        ),
        MetaConstraint(
            name = "deriveFeatureValueValue",
            type = ConstraintType.DERIVATION,
            expression = "ownedMemberElement.oclAsType(Expression)",
            description = "The value of a FeatureValue is its ownedMemberElement, which must be an Expression."
        ),
        MetaConstraint(
            name = "checkFeatureValueBindingConnector",
            type = ConstraintType.IMPLICIT_BINDING_CONNECTOR,
            expression = "not isDefault implies featureWithValue.ownedMember->selectByKind(BindingConnector)->exists(b | b.relatedFeature->includes(featureWithValue) and b.relatedFeature->exists(f | f.chainingFeature = Sequence{value, value.result}) and if not isInitial then b.featuringType = featureWithValue.featuringType else b.featuringType->exists(t | t.oclIsKindOf(Feature) and t.oclAsType(Feature).chainingFeature = Sequence{resolveGlobal('Base::things::that').memberElement, resolveGlobal('Occurrences::Occurrence::startShot').memberElement}) endif)",
            description = "If isDefault = false, then the featureWithValue must have an ownedMember that is a BindingConnector whose relatedElements are the featureWithValue and a feature chain consisting of the valueExpression and its result."
        ),
        MetaConstraint(
            name = "validateFeatureValueIsInitial",
            type = ConstraintType.VERIFICATION,
            expression = "isInitial implies featureWithValue.isVariable",
            description = "If a FeatureValue has isInitial = true, then its featureWithValue must have isVariable = true."
        ),
        MetaConstraint(
            name = "validateFeatureValueOverriding",
            type = ConstraintType.VERIFICATION,
            expression = "featureWithValue.redefinition.redefinedFeature->closure(redefinition.redefinedFeature).valuation->forAll(isDefault)",
            description = "All Features directly or indirectly redefined by the featureWithValue of a FeatureValue must have only default FeatureValues."
        )
    ),
    description = "A FeatureValue is a Membership that identifies a particular memberExpression that provides the value of the Feature that owns the FeatureValue."
)
