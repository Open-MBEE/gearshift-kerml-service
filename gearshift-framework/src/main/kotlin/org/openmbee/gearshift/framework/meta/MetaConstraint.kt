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
package org.openmbee.gearshift.framework.meta

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The type of constraint.
 *
 * Per KerML 8.4.2, there are categories of semantic constraints:
 * - Implied relationships (Redefinition, TypeFeaturing, BindingConnector) are created by constraints
 * - Implied specializations (Subclassification, Subsetting) are now handled via SemanticBinding
 * - Derivation constraints compute values
 * - Verification constraints validate invariants
 */
enum class ConstraintType {
    /**
     * @deprecated Use SemanticBinding instead. Will be removed in future version.
     * Conditional implicit specialization - now handled by SemanticBinding with condition.
     */
    @Deprecated("Use SemanticBinding instead")
    CONDITIONAL_IMPLICIT_SPECIALIZATION,

    /** Derivation constraint for computing derived property values (spec-defined) */
    DERIVATION,

    /**
     * @deprecated Use SemanticBinding instead. Will be removed in future version.
     * Implicit specialization - now handled by SemanticBinding.
     */
    @Deprecated("Use SemanticBinding instead")
    IMPLICIT_SPECIALIZATION,

    /**
     * Implicit binding connector constraint - creates BindingConnector between features.
     * Expression defines the features to connect.
     * Example: checkFeatureValueBindingConnector
     */
    IMPLICIT_BINDING_CONNECTOR,

    /**
     * Implicit redefinition constraint - creates Redefinition relationships.
     * Expression defines what feature(s) should be redefined.
     * Example: checkConnectorEndRedefinition
     */
    IMPLICIT_REDEFINITION,

    /**
     * Implicit type featuring constraint - creates TypeFeaturing relationships.
     * Expression defines the featuring type(s).
     * Example: checkExpressionTypeFeaturing
     */
    IMPLICIT_TYPE_FEATURING,

    /** Constraint for calculating non-navigable association ends */
    NON_NAVIGABLE_END,

    /**
     * Inferred derivation for properties that redefine another property.
     * Value equals the redefined property. Not explicitly in spec but derived from metamodel structure.
     */
    REDEFINES_DERIVATION,

    /**
     * Inferred derivation for properties that subset another property.
     * Value is filtered from the subsetted property. Not explicitly in spec but derived from metamodel structure.
     */
    SUBSETS_DERIVATION,

    /** Verification constraint for validating invariants (no implied relationships) */
    VERIFICATION
}

/**
 * Represents a constraint in the metamodel.
 * Can be expressed using OCL or other constraint languages.
 */
data class MetaConstraint(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty
    val type: ConstraintType = ConstraintType.DERIVATION,

    @JsonProperty
    val language: String = "OCL",

    @JsonProperty(required = true)
    val expression: String,

    /**
     * @deprecated Use SemanticBinding.baseConcept instead.
     * For legacy IMPLICIT_SPECIALIZATION and CONDITIONAL_IMPLICIT_SPECIALIZATION:
     * The qualified library type name to specialize.
     */
    @Deprecated("Use SemanticBinding.baseConcept instead")
    @JsonProperty
    val libraryTypeName: String? = null,

    /**
     * For IMPLICIT_REDEFINITION:
     * The qualified path to the feature that should be redefined.
     * If null, the expression computes the redefined feature(s).
     */
    @JsonProperty
    val redefinedFeaturePath: String? = null,

    /**
     * For IMPLICIT_TYPE_FEATURING:
     * The qualified path to the type that should feature this element.
     * If null, the expression computes the featuring type(s).
     */
    @JsonProperty
    val featuringTypePath: String? = null,

    /**
     * For DERIVATION constraints that redefine a parent constraint.
     * The name of the constraint being redefined.
     */
    @JsonProperty
    val redefines: String? = null,

    @JsonProperty
    val description: String? = null
)
