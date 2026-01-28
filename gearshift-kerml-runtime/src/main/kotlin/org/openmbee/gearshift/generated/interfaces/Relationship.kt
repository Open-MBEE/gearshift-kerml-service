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
 * An abstract base class for all relationships between elements
 */
interface Relationship : Element {

    /**
     * Whether this Relationship is implied or explicitly stated
     */
    var isImplied: Boolean

    var ownedRelatedElement: List<Element>

    var owningRelatedElement: Element?

    val relatedElement: List<Element>

    var source: Set<Element>

    var target: Set<Element>

    /**
     * Return whether this Relationship has either an owningRelatedElement or owningRelationship that is a library element.
     */
    override fun libraryNamespace(): Namespace?

    /**
     * If the owningRelationship of the Relationship is null but its owningRelatedElement is non-null, construct the path using the position of the Relationship in the list of ownedRelationships of its owningRelatedElement. Otherwise, return the path of the Relationship as specified for an Element in general.
     */
    override fun path(): String
}

