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
package org.openmbee.mdm.framework.meta

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Type alias for native operation implementation lambdas.
 * Parameters:
 * - element: The ModelElement on which the operation is invoked
 * - args: Map of operation arguments
 * - engine: The ModelEngine for model access
 */
typealias OperationImplementation = (element: ModelElement, args: Map<String, Any?>, engine: ModelEngine) -> Any?

/**
 * Language used to express operation bodies and constraint expressions.
 */
enum class BodyLanguage {
    /** Simple property reference (e.g., "declaredName") */
    PROPERTY_REF,

    /** Object Constraint Language - standard for UML/MOF */
    OCL,

    /** Graph Query Language - for graph-based queries */
    GQL
}

/**
 * Represents an operation body - either a string expression or native Kotlin implementation.
 */
sealed class OperationBody {
    /**
     * String-based expression (OCL, GQL, property ref, etc.) - serializable.
     */
    data class Expression(
        val code: String,
        val language: BodyLanguage = BodyLanguage.OCL
    ) : OperationBody()

    /**
     * Native Kotlin implementation - not serializable, but type-safe and fast.
     */
    data class Native(
        val impl: OperationImplementation
    ) : OperationBody()
}

/**
 * Represents an operation (method) in the metamodel.
 */
data class MetaOperation(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty
    val returnType: String? = null,

    @JsonProperty
    val returnLowerBound: Int = 0,

    @JsonProperty
    val returnUpperBound: Int = 1,

    @JsonProperty
    val parameters: List<MetaParameter> = emptyList(),

    /**
     * The operation body - either an expression string or native implementation.
     * Use companion helpers: ocl(), native(), propertyRef()
     */
    @JsonIgnore
    val body: OperationBody? = null,

    @JsonProperty
    val description: String? = null,

    @JsonProperty
    val isAbstract: Boolean = false,

    @JsonProperty
    val preconditions: List<String> = emptyList(),

    @JsonProperty
    val isQuery: Boolean = false,

    @JsonProperty
    val redefines: String? = null
) {
    companion object {
        /** Create an OCL expression body */
        fun ocl(code: String) = OperationBody.Expression(code, BodyLanguage.OCL)

        /** Create a GQL expression body */
        fun gql(code: String) = OperationBody.Expression(code, BodyLanguage.GQL)

        /** Create a simple property reference body */
        fun propertyRef(propertyName: String) = OperationBody.Expression(propertyName, BodyLanguage.PROPERTY_REF)

        /** Create a native Kotlin implementation body */
        fun native(impl: OperationImplementation) = OperationBody.Native(impl)
    }
}
