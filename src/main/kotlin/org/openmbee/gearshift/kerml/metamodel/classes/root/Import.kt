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
import MetaProperty

/**
 * KerML Import metaclass.
 * Specializes: Relationship
 * An abstract relationship that imports elements into a namespace.
 */
fun createImportMetaClass() = MetaClass(
    name = "Import",
    isAbstract = true,
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            multiplicity = "1",
            description = "The visibility of the imported elements"
        ),
        MetaProperty(
            name = "isRecursive",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether the import is recursive"
        ),
        MetaProperty(
            name = "isImportAll",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether all elements are imported"
        )
    ),
    description = "An abstract relationship that imports elements into a namespace"
)
