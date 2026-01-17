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
package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The type of constraint.
 */
enum class ConstraintType {
    /**
     * Conditional implicit specialization - evaluated reactively.
     * If expression evaluates to true, creates specialization to libraryTypeName.
     */
    CONDITIONAL_IMPLICIT_SPECIALIZATION,

    /** Derivation constraint for computing derived property values */
    DERIVATION,

    /**
     * Implicit specialization constraint - creates specialization to libraryTypeName.
     * Expression is used for verification that the specialization exists.
     */
    IMPLICIT_SPECIALIZATION,

    /** Constraint for calculating non-navigable association ends */
    NON_NAVIGABLE_END,

    /** Verification constraint for validating invariants */
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
     * For CONDITIONAL_IMPLICIT_SPECIALIZATION: the qualified library type name to specialize.
     * For IMPLICIT_SPECIALIZATION: not used (expression contains the library type).
     */
    @JsonProperty
    val libraryTypeName: String? = null,

    @JsonProperty
    val redefines: String? = null,

    @JsonProperty
    val description: String? = null
)
