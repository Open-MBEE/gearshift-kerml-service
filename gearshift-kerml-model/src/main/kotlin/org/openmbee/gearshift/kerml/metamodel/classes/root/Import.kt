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

import org.openmbee.gearshift.framework.meta.*

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
            name = "isImportAll",
            type = "Boolean",
            description = "Whether all elements are imported"
        ),
        MetaProperty(
            name = "isRecursive",
            type = "Boolean",
            description = "Whether the import is recursive"
        ),
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            description = "The visibility of the imported elements"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveImportImportedElement",
            type = ConstraintType.DERIVATION,
            expression = "null",
            description = "Abstract derivation for importedElement - redefined by MembershipImport and NamespaceImport"
        ),
        MetaConstraint(
            name = "validateImportTopLevelVisibility",
            type = ConstraintType.VERIFICATION,
            expression = "importOwningNamespace.owner = null implies visibility = VisibilityKind::private",
            description = "A top-level Import (owned by a root Namespace) must have visibility of private"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "importedMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(
                    name = "excluded",
                    type = "Namespace",
                    lowerBound = 0,
                    upperBound = -1
                )
            ),
            isAbstract = true,
            isQuery = true,
            description = "Return the Memberships that are imported by this Import, excluding those in the given set"
        )
    ),
    description = "An abstract relationship that imports elements into a namespace"
)
