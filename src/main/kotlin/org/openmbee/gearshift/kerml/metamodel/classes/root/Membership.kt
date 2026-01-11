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
 * KerML Membership metaclass.
 * Specializes: Relationship
 * A relationship that makes an element a member of a namespace.
 */
fun createMembershipMetaClass() = MetaClass(
    name = "Membership",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(
            name = "memberName",
            type = "String",
            multiplicity = "0..1",
            description = "The name of the member element"
        ),
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            multiplicity = "1",
            description = "The visibility of the member"
        )
    ),
    description = "A relationship that makes an element a member of a namespace"
)
