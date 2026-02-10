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

import org.openmbee.gearshift.generated.interfaces.Expose
import org.openmbee.gearshift.generated.interfaces.MembershipExpose
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.NamespaceExpose
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Expose elements.
 *
 * An Expose is an Import owned by a View that determines which elements
 * are visible in the View. Dispatches to MembershipExpose or NamespaceExpose
 * based on the importDeclaration.
 *
 * Grammar:
 * ```
 * expose
 *     : EXPOSE ( isImportAll=ALL )?
 *       importDeclaration relationshipBody
 *     ;
 * ```
 *
 * Expose extends Import, so we reuse the import parsing mechanics.
 */
class ExposeVisitor : BaseRelationshipVisitor<KerMLParser.ExposeContext, Expose>() {

    override fun visit(ctx: KerMLParser.ExposeContext, kermlParseContext: KermlParseContext): Expose {
        val importDecl = ctx.importDeclaration()

        val expose: Expose = if (importDecl?.membershipImport() != null) {
            parseMembershipExpose(importDecl.membershipImport(), kermlParseContext)
        } else if (importDecl?.namespaceImport() != null) {
            parseNamespaceExpose(importDecl.namespaceImport(), kermlParseContext)
        } else {
            kermlParseContext.create<MembershipExpose>()
        }

        // Parse isImportAll
        ctx.isImportAll?.let {
            expose.isImportAll = true
        }

        // Set import owning namespace
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                expose.importOwningNamespace = parent
            }
        }

        // Parse relationship body
        ctx.relationshipBody()?.let { body ->
            val childContext = kermlParseContext.withParent(expose, "")
            parseRelationshipBody(body, expose, childContext)
        }

        return expose
    }

    private fun parseMembershipExpose(
        ctx: KerMLParser.MembershipImportContext,
        kermlParseContext: KermlParseContext
    ): MembershipExpose {
        val expose = kermlParseContext.create<MembershipExpose>()

        ctx.importedMembership?.let { qn ->
            val membershipName = extractQualifiedName(qn)
            kermlParseContext.registerReference(expose, "importedMembership", membershipName)
        }

        ctx.isRecursive?.let {
            expose.isRecursive = true
        }

        return expose
    }

    private fun parseNamespaceExpose(
        ctx: KerMLParser.NamespaceImportContext,
        kermlParseContext: KermlParseContext
    ): NamespaceExpose {
        val expose = kermlParseContext.create<NamespaceExpose>()

        ctx.qualifiedName()?.let { qn ->
            val namespaceName = extractQualifiedName(qn)
            kermlParseContext.registerReference(expose, "importedNamespace", namespaceName)
        }

        ctx.isRecursive?.let {
            expose.isRecursive = true
        }

        return expose
    }
}
