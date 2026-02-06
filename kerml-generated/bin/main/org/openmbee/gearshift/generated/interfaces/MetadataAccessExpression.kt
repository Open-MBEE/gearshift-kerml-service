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
 * A MetadataAccessExpression is an Expression whose result is a sequence of instances of Metaclasses representing the metadataFeatures of the referencedElement.
 */
interface MetadataAccessExpression : Expression {

    val referencedElement: Element

    /**
     * The model-level evaluation of a MetadataAccessExpression results in instances of Metaclasses representing metadata.
     */
    override fun evaluate(target: Element): List<Element>

    /**
     * Returns the Feature of the given Metaclass with the same name as the referencedElement.
     */
    fun metaclassFeature(metaclass: Metaclass): Feature?

    /**
     * A MetadataAccessExpression is always model-level evaluable.
     */
    override fun modelLevelEvaluable(visited: List<Feature>): Boolean?
}

