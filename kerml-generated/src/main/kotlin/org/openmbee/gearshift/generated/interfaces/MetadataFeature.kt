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

package org.openmbee.gearshift.generated.interfaces

/**
 * A MetadataFeature is a Feature that is an AnnotatingElement used to annotate another Element with metadata. It is typed by a Metaclass.
 */
interface MetadataFeature : AnnotatingElement, Feature {

    val metaclass: Metaclass?

    /**
     * If the given baseFeature is a feature of this MetadataFeature, or is directly or indirectly redefined by a feature, then return the result of evaluating the appropriate (model-level evaluable) valueExpression for it (if any), with the MetadataFeature as the target.
     */
    fun evaluateFeature(baseFeature: Feature): List<Element>

    /**
     * Check if this MetadataFeature has a metaclass which is a kind of SemanticMetadata.
     */
    fun isSemantic(): Boolean?

    /**
     * Check if this MetadataFeature has a metaclass that is a kind of KerML::Element (that is, it is from the reflective abstract syntax model).
     */
    fun isSyntactic(): Boolean?

    /**
     * If this MetadataFeature reflectively represents a model element, then return the corresponding Element instance from the MOF abstract syntax representation of the model.
     */
    fun syntaxElement(): Element?
}

