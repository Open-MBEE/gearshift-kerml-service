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
package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaOperation
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Element (Root.Element)
 * Abstract root of the KerML element hierarchy.
 */
fun createElementMetaClass() = MetaClass(
    name = "Element",
    isAbstract = true,
    description = "Abstract root of the KerML element hierarchy",
    attributes = listOf(
        MetaProperty(
            name = "elementId",
            type = "String",
            description = "Unique identifier for this element"
        ),
        MetaProperty(
            name = "aliasIds",
            type = "String",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            description = "Alias identifiers for this element"
        ),
        MetaProperty(
            name = "declaredShortName",
            type = "String",
            lowerBound = 0,
            description = "Declared short name"
        ),
        MetaProperty(
            name = "declaredName",
            type = "String",
            lowerBound = 0,
            description = "Declared name"
        ),
        MetaProperty(
            name = "isImpliedIncluded",
            type = "Boolean",
            description = "Whether this element is implied included"
        ),
        MetaProperty(
            name = "shortName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            description = "Effective short name (derived)"
        ),
        MetaProperty(
            name = "name",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            description = "Effective name (derived)"
        ),
        MetaProperty(
            name = "qualifiedName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            description = "Fully qualified name (derived)"
        ),
        MetaProperty(
            name = "isLibraryElement",
            type = "Boolean",
            isDerived = true,
            description = "Whether this is a library element (derived)"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            description = "Return an effective name for this Element. By default this is the same as its declaredName.",
            body = "declaredName",
            isQuery = true
        )
    )
)
