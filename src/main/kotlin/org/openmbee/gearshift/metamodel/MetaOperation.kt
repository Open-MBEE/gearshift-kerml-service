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
import org.intellij.lang.annotations.Language

/**
 * Language used to express operation bodies and constraint expressions.
 */
enum class BodyLanguage {
    /** Simple property reference (e.g., "declaredName") */
    PROPERTY_REF,

    /** Object Constraint Language - standard for UML/MOF */
    OCL,

    /** Graph Query Language - for graph-based queries */
    GQL,

    /** Kotlin DSL - type-safe Kotlin expressions (future) */
    KOTLIN_DSL
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

    @JsonProperty
    val body: String? = null,

    @JsonProperty
    val bodyLanguage: BodyLanguage = BodyLanguage.OCL,

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
        /** Helper for Kotlin DSL bodies with IDE language support */
        fun kotlinBody(@Language("kotlin") code: String): String = code

        /** Helper for OCL bodies (for consistency) */
        fun oclBody(code: String): String = code
    }
}
