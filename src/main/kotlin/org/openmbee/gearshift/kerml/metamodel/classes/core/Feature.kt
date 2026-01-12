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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Feature metaclass.
 * Specializes: Type
 * A type that is also a feature.
 */
fun createFeatureMetaClass() = MetaClass(
    name = "Feature",
    isAbstract = false,
    superclasses = listOf("Type"),
    attributes = listOf(
        MetaProperty(
            name = "direction",
            type = "FeatureDirectionKind",
            lowerBound = 0,
            description = "The direction of the feature"
        ),
        MetaProperty(
            name = "isComposite",
            type = "Boolean",
            description = "Whether this feature is composite"
        ),
        MetaProperty(
            name = "isEnd",
            type = "Boolean",
            description = "Whether this feature is an end feature"
        ),
        MetaProperty(
            name = "isOrdered",
            type = "Boolean",
            description = "Whether values of this feature are ordered"
        ),
        MetaProperty(
            name = "isPortion",
            type = "Boolean",
            description = "Whether this feature is a portion"
        ),
        MetaProperty(
            name = "isReadOnly",
            type = "Boolean",
            description = "Whether this feature is read-only"
        ),
        MetaProperty(
            name = "isUnique",
            type = "Boolean",
            description = "Whether values of this feature are unique"
        )
    ),
    description = "A type that is also a feature"
)
