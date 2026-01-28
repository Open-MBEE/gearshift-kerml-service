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

/**
 * Represents a semantic binding from a local element to a base library concept.
 *
 * Semantic bindings declare that instances of a metaclass have an implied
 * relationship to a base library type. The actual relationship (Subclassification
 * or Subsetting) is created by the domain-specific handler based on [bindingKind].
 *
 * This approach separates the semantic meaning ("this thing conforms to that concept")
 * from the relationship creation, preventing the common failure mode of over-inference.
 *
 * @property name A unique name for this binding (for documentation/debugging)
 * @property baseConcept The qualified name of the library type to bind to (e.g., "Base::Anything")
 * @property bindingKind The kind of relationship to create (SPECIALIZES or SUBSETS)
 * @property condition When this binding applies (default: always)
 */
data class SemanticBinding(
    val name: String,
    val baseConcept: String,
    val bindingKind: BindingKind,
    val condition: BindingCondition = BindingCondition.Default
)

/**
 * The kind of implied relationship a semantic binding creates.
 */
enum class BindingKind {
    /**
     * Creates an implied Subclassification relationship.
     * Used for Classifiers that implicitly specialize a base library type.
     */
    SPECIALIZES,

    /**
     * Creates an implied Subsetting relationship.
     * Used for Features that implicitly subset a base library feature.
     */
    SUBSETS
}

/**
 * Conditions under which a semantic binding applies.
 *
 * These are evaluated by the domain-specific handler using simple Kotlin logic,
 * avoiding the complexity of OCL expression parsing and derived property chains.
 */
sealed class BindingCondition {
    /**
     * Binding always applies (unconditional).
     */
    data object Default : BindingCondition()

    /**
     * Binding applies when the element is typed by a specific metaclass.
     * Used for Features that have different bindings based on their type.
     *
     * @property metaclass The metaclass name to check (e.g., "DataType", "Class", "Structure")
     */
    data class TypedBy(val metaclass: String) : BindingCondition()

    /**
     * Binding applies when the owning type is a specific metaclass.
     *
     * @property metaclass The metaclass name to check
     */
    data class OwningTypeIs(val metaclass: String) : BindingCondition()

    /**
     * Binding applies when a property has a specific value.
     *
     * @property property The property name to check
     * @property value The expected value
     */
    data class PropertyEquals(val property: String, val value: Any) : BindingCondition()

    /**
     * Binding applies when all sub-conditions are true.
     *
     * @property conditions The conditions that must all be true
     */
    data class And(val conditions: List<BindingCondition>) : BindingCondition()

    /**
     * Binding applies when any sub-condition is true.
     *
     * @property conditions The conditions where at least one must be true
     */
    data class Or(val conditions: List<BindingCondition>) : BindingCondition()

    /**
     * Binding applies when the sub-condition is false.
     *
     * @property condition The condition to negate
     */
    data class Not(val condition: BindingCondition) : BindingCondition()

    /**
     * Binding applies when the element is an end feature (isEnd = true).
     */
    data object IsEnd : BindingCondition()

    /**
     * Binding applies when the element is a portion (isPortion = true).
     */
    data object IsPortion : BindingCondition()

    /**
     * Binding applies when the element is composite (isComposite = true).
     */
    data object IsComposite : BindingCondition()

    /**
     * Binding applies when the owning type is typed by a specific metaclass.
     * This handles the pattern "owningType is a Class or Feature typed by Class".
     *
     * @property metaclass The metaclass name to check
     */
    data class OwningTypeTypedBy(val metaclass: String) : BindingCondition()
}

/**
 * Provenance tracking for relationships.
 * Indicates how a relationship was created.
 */
enum class Origin {
    /**
     * Relationship was explicitly declared by the user in the model.
     */
    DECLARED,

    /**
     * Relationship was inferred from patterns in the user model.
     */
    INFERRED,

    /**
     * Relationship was implied by semantic bindings to the base library.
     */
    BASE_IMPLIED
}
