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

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML Comment metaclass.
 * Specializes: AnnotatingElement
 * A textual comment that annotates elements.
 */
fun createCommentMetaClass() = MetaClass(
    name = "Comment",
    isAbstract = false,
    superclasses = listOf("AnnotatingElement"),
    attributes = listOf(
        MetaProperty(
            name = "body",
            type = "String",
            description = "The text of the comment"
        ),
        MetaProperty(
            name = "locale",
            type = "String",
            lowerBound = 0,
            description = "The locale of the comment text"
        )
    ),
    description = "A textual comment that annotates elements"
)
