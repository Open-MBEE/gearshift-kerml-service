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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML Relationship metaclass.
 * Specializes: Element
 * An abstract base class for all relationships between elements.
 */
fun createRelationshipMetaClass() = MetaClass(
    name = "Relationship",
    isAbstract = true,
    superclasses = listOf("Element"),
    attributes = listOf(
        MetaProperty(
            name = "isImplied",
            type = "Boolean",
            description = "Whether this Relationship is implied or explicitly stated"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveRelationshipRelatedElement",
            type = ConstraintType.DERIVATION,
            expression = "source->union(target)",
            description = "The relatedElements of a Relationship consist of all of its sourceElements followed by all of its targetElements."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "libraryNamespace",
            returnType = "Namespace",
            redefines = "Element::libraryNamespace",
            description = "Return whether this Relationship has either an owningRelatedElement or owningRelationship that is a library element.",
            body = """
                if owningRelatedElement <> null then owningRelatedElement.libraryNamespace()
                else if owningRelationship <> null then owningRelationship.libraryNamespace()
                else null endif endif
            """.trimIndent(),
            isQuery = true
        ),
        MetaOperation(
            name = "path",
            returnType = "String",
            returnLowerBound = 1,
            returnUpperBound = 1,
            redefines = "Element::path",
            description = "If the owningRelationship of the Relationship is null but its owningRelatedElement is non-null, " +
                    "construct the path using the position of the Relationship in the list of ownedRelationships of its " +
                    "owningRelatedElement. Otherwise, return the path of the Relationship as specified for an Element in general.",
            body = """
                if owningRelationship = null and owningRelatedElement <> null then
                    owningRelatedElement.path() + '/' + owningRelatedElement.ownedRelationship->indexOf(self).toString()
                else self.oclAsType(Element).path()
                endif
            """.trimIndent(),
            isQuery = true
        )
    ),
    description = "An abstract base class for all relationships between elements"
)
