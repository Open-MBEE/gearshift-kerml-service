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

import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Visitor for Package elements.
 *
 * Per KerML spec 8.2.9.2: Packages are namespaces that organize model elements.
 *
 * Grammar:
 * ```
 * package
 *     : prefixMetadataMember*
 *       packageDeclaration packageBody
 *     ;
 *
 * packageDeclaration
 *     : PACKAGE identification?
 *     ;
 *
 * packageBody
 *     : ';'
 *     | '{' namespaceBodyElement* '}'
 *     ;
 * ```
 *
 * Package extends Namespace, so we delegate namespace body parsing to NamespaceVisitor.
 */
class PackageVisitor : BaseTypedVisitor<KerMLParser.PackageContext, KerMLPackage>() {

    private val namespaceVisitor = NamespaceVisitor()

    override fun visit(ctx: KerMLParser.PackageContext, kermlParseContext: KermlParseContext): KerMLPackage {
        // Extract names before creating element (needed for deterministic library IDs)
        val identification = ctx.packageDeclaration()?.identification()
        val (shortName, name) = extractIdentificationNames(identification)

        // Create Package with names for deterministic ID generation
        val pkg = kermlParseContext.create<KerMLPackage>(
            declaredName = name,
            declaredShortName = shortName
        )

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(pkg, pkg.declaredName ?: "")

        // Parse package body (reuses namespace body parsing from NamespaceVisitor)
        ctx.packageBody()?.let { body ->
            parsePackageBody(body, childContext)
        }

        // Parse prefix metadata members
        ctx.prefixMetadataMember()?.forEach { _ ->
            // TODO: Delegate to PrefixMetadataMemberVisitor
        }

        return pkg
    }

    /**
     * Parse the package body.
     *
     * PackageBody uses the same namespaceBodyElement structure as NamespaceBody.
     */
    private fun parsePackageBody(
        ctx: KerMLParser.PackageBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        // Process each namespace body element using NamespaceVisitor
        ctx.namespaceBodyElement()?.forEach { bodyElement ->
            namespaceVisitor.parseNamespaceBodyElement(bodyElement, kermlParseContext)
        }
    }
}
