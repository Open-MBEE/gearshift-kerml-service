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
            name = "isLibraryElement",
            type = "Boolean",
            isDerived = true,
            derivationConstraint = "deriveElementIsLibraryElement",
            description = "Whether this is a library element (derived)"
        ),
        MetaProperty(
            name = "name",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveElementName",
            description = "Effective name (derived)"
        ),
        MetaProperty(
            name = "qualifiedName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveElementQualifiedName",
            description = "Fully qualified name (derived)"
        ),
        MetaProperty(
            name = "shortName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveElementShortName",
            description = "Effective short name (derived)"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveElementDocumentation",
            type = ConstraintType.DERIVATION,
            expression = "ownedElement->selectByKind(Documentation)",
            description = "Derivation for Element::documentation"
        ),
        MetaConstraint(
            name = "deriveElementIsLibraryElement",
            type = ConstraintType.DERIVATION,
            expression = "self.libraryNamespace() <> null",
            description = "Derivation for Element::isLibraryElement"
        ),
        MetaConstraint(
            name = "deriveElementName",
            type = ConstraintType.DERIVATION,
            expression = "self.effectiveName()",
            description = "Derivation for Element::name"
        ),
        MetaConstraint(
            name = "deriveElementOwnedAnnotation",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Annotation)->select(a | a.annotatedElement = self)",
            description = "Derivation for Element::ownedAnnotation association end"
        ),
        MetaConstraint(
            name = "deriveElementOwnedElement",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship.ownedRelatedElement",
            description = "Derivation for Element::ownedElement association end"
        ),
        MetaConstraint(
            name = "deriveElementOwner",
            type = ConstraintType.DERIVATION,
            expression = "if owningRelationship = null then null else owningRelationship.owningRelatedElement endif",
            description = "Derivation for Element::owner association end"
        ),
        MetaConstraint(
            name = "deriveElementOwningNamespace",
            type = ConstraintType.DERIVATION,
            expression = "if owningMembership = null then null else owningMembership.membershipOwningNamespace endif",
            description = "Derivation for Element::owningNamespace association end"
        ),
        MetaConstraint(
            name = "deriveElementQualifiedName",
            type = ConstraintType.DERIVATION,
            expression = """
                if owningNamespace = null then null
                else if name <> null and
                    owningNamespace.ownedMember->select(m | m.name = name)->indexOf(self) <> 1 then null
                else if owningNamespace.owner = null then self.escapedName()
                else if owningNamespace.qualifiedName = null or self.escapedName() = null then null
                else owningNamespace.qualifiedName + '::' + self.escapedName()
                endif endif endif endif
            """.trimIndent(),
            description = "Derivation for Element::qualifiedName"
        ),
        MetaConstraint(
            name = "deriveElementShortName",
            type = ConstraintType.DERIVATION,
            expression = "self.effectiveShortName()",
            description = "Derivation for Element::shortName"
        ),
        MetaConstraint(
            name = "deriveElementTextualRepresentation",
            type = ConstraintType.DERIVATION,
            expression = "ownedElement->selectByKind(TextualRepresentation)",
            description = "Derivation for Element::textualRepresentation association end"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            description = "Return an effective name for this Element. By default this is the same as its declaredName.",
            body = "declaredName",
            isQuery = true
        ),
        MetaOperation(
            name = "effectiveShortName",
            returnType = "String",
            description = "Return an effective short name for this Element. By default this is the same as its declaredShortName.",
            body = "declaredShortName",
            isQuery = true
        ),
        MetaOperation(
            name = "escapedName",
            returnType = "String",
            description = "Return name if not null, otherwise shortName if not null, otherwise null. " +
                    "If non-null, returned as-is if it has the form of a basic name, otherwise as a restricted name.",
            body = """
                let n : String = if name <> null then name else shortName endif in
                if n = null then null
                else if n.isBasicName() then n
                else n.asRestrictedName()
                endif endif
            """.trimIndent(),
            isQuery = true
        ),
        MetaOperation(
            name = "libraryNamespace",
            returnType = "Namespace",
            description = "Return the library Namespace of the owningRelationship of this Element, if it has one.",
            body = "if owningRelationship <> null then owningRelationship.libraryNamespace() else null endif",
            isQuery = true
        ),
        MetaOperation(
            name = "path",
            returnType = "String",
            returnLowerBound = 1,
            returnUpperBound = 1,
            description = "Return a unique description of the location of this Element in the containment structure " +
                    "rooted in a root Namespace. If the Element has a non-null qualifiedName, return that. " +
                    "Otherwise, if it has an owningRelationship, return the path of the owningRelationship " +
                    "followed by '/' and the 1-based index in ownedRelatedElement. Otherwise, return empty string.",
            body = """
                if qualifiedName <> null then qualifiedName
                else if owningRelationship <> null then
                    owningRelationship.path() + '/' + owningRelationship.ownedRelatedElement->indexOf(self).toString()
                else ''
                endif endif
            """.trimIndent(),
            isQuery = true
        )
    )
)
