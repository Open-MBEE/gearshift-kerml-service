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
package org.openmbee.gearshift.kerml.parser.visitors

import org.openmbee.gearshift.generated.interfaces.Import
import org.openmbee.gearshift.generated.interfaces.MembershipImport
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.NamespaceImport
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Import elements.
 *
 * Per KerML spec 8.2.3.5: Imports make members of other namespaces accessible.
 *
 * Grammar:
 * ```
 * import_
 *     : visibilityIndicator
 *       IMPORT ( isImportAll=ALL )?
 *       importDeclaration relationshipBody
 *     ;
 *
 * importDeclaration
 *     : membershipImport
 *     | namespaceImport
 *     ;
 * ```
 *
 * Import is the base for MembershipImport and NamespaceImport.
 */
class ImportVisitor : BaseRelationshipVisitor<KerMLParser.Import_Context, Import>() {

    override fun visit(ctx: KerMLParser.Import_Context, parseContext: ParseContext): Import {
        // Determine which type of import to create based on importDeclaration
        val importDecl = ctx.importDeclaration()

        val import: Import = if (importDecl?.membershipImport() != null) {
            parseMembershipImport(importDecl.membershipImport(), parseContext)
        } else if (importDecl?.namespaceImport() != null) {
            parseNamespaceImport(importDecl.namespaceImport(), parseContext)
        } else {
            // Default to base import if no declaration
            parseContext.create<MembershipImport>()
        }

        // Parse visibility
        ctx.visibilityIndicator()?.let { visibility ->
            import.visibility = visibility.text
        }

        // Parse isImportAll
        ctx.isImportAll?.let {
            import.isImportAll = true
        }

        // Set import owning namespace
        parseContext.parent?.let { parent ->
            if (parent is Namespace) {
                import.importOwningNamespace = parent
            }
        }

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = parseContext.withParent(import, "")
            parseRelationshipBody(body, import, childContext)
        }

        return import
    }

    /**
     * Parse a membership import.
     *
     * Grammar: membershipImport = importedMembership=qualifiedName ( '::' isRecursive='**' )?
     */
    private fun parseMembershipImport(
        ctx: KerMLParser.MembershipImportContext,
        parseContext: ParseContext
    ): MembershipImport {
        val membershipImport = parseContext.create<MembershipImport>()

        // Parse imported membership reference
        ctx.importedMembership?.let { qn ->
            val membershipName = extractQualifiedName(qn)
            parseContext.registerReference(membershipImport, "importedMembership", membershipName)
        }

        // Parse isRecursive
        ctx.isRecursive?.let {
            membershipImport.isRecursive = true
        }

        return membershipImport
    }

    /**
     * Parse a namespace import.
     *
     * Grammar: namespaceImport = qualifiedName '::' '*' ( '::' isRecursive='**' )? | filterPackageImport
     */
    private fun parseNamespaceImport(
        ctx: KerMLParser.NamespaceImportContext,
        parseContext: ParseContext
    ): NamespaceImport {
        val namespaceImport = parseContext.create<NamespaceImport>()

        // Parse imported namespace reference
        ctx.qualifiedName()?.let { qn ->
            val namespaceName = extractQualifiedName(qn)
            parseContext.registerReference(namespaceImport, "importedNamespace", namespaceName)
        }

        // Parse isRecursive
        ctx.isRecursive?.let {
            namespaceImport.isRecursive = true
        }

        // Parse filter package import (if present)
        ctx.filterPackageImport()?.let { _ ->
            // TODO: Parse filter package import
        }

        return namespaceImport
    }
}
