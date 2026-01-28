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

import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaProperty

/**
 * KerML TextualRepresentation metaclass.
 * Specializes: AnnotatingElement
 * A textual representation of an element in a specific language.
 */
fun createTextualRepresentationMetaClass() = MetaClass(
    name = "TextualRepresentation",
    isAbstract = false,
    superclasses = listOf("AnnotatingElement"),
    attributes = listOf(
        MetaProperty(
            name = "language",
            type = "String",
            description = "The language of the textual representation"
        ),
        MetaProperty(
            name = "body",
            type = "String",
            description = "The text of the representation"
        )
    ),
    description = "A textual representation of an element in a specific language"
)
