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
 * Abstract root of the KerML element hierarchy
 */
interface Element : ModelElement {

    /**
     * Alias identifiers for this element
     */
    var aliasIds: List<String>

    /**
     * Declared name
     */
    var declaredName: String?

    /**
     * Declared short name
     */
    var declaredShortName: String?

    /**
     * Unique identifier for this element
     */
    var elementId: String

    /**
     * Whether this element is implied included
     */
    var isImpliedIncluded: Boolean

    /**
     * Whether this is a library element (derived)
     */
    val isLibraryElement: Boolean

    /**
     * Effective name (derived)
     */
    val name: String?

    /**
     * Fully qualified name (derived)
     */
    val qualifiedName: String?

    /**
     * Effective short name (derived)
     */
    val shortName: String?

    var clientDependency: Set<Dependency>

    val documentation: List<Documentation>

    val ownedAnnotation: List<Annotation>

    val ownedElement: List<Element>

    var ownedRelationship: List<Relationship>

    val owner: Element?

    var owningMembership: OwningMembership?

    val owningNamespace: Namespace?

    var owningRelationship: Relationship?

    var supplierDependency: Set<Dependency>

    val textualRepresentation: List<TextualRepresentation>

    /**
     * Return an effective name for this Element. By default this is the same as its declaredName.
     */
    fun effectiveName(): String?

    /**
     * Return an effective short name for this Element. By default this is the same as its declaredShortName.
     */
    fun effectiveShortName(): String?

    /**
     * Return name if not null, otherwise shortName if not null, otherwise null. If non-null, returned as-is if it has the form of a basic name, otherwise as a restricted name.
     */
    fun escapedName(): String?

    /**
     * Return the library Namespace of the owningRelationship of this Element, if it has one.
     */
    fun libraryNamespace(): Namespace?

    /**
     * Return a unique description of the location of this Element in the containment structure rooted in a root Namespace. If the Element has a non-null qualifiedName, return that. Otherwise, if it has an owningRelationship, return the path of the owningRelationship followed by '/' and the 1-based index in ownedRelatedElement. Otherwise, return empty string.
     */
    fun path(): String
}

