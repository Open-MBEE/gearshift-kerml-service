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

import org.openmbee.gearshift.generated.interfaces.LibraryPackage
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor

/**
 * Visitor for LibraryPackage elements.
 *
 * Per KerML spec: LibraryPackages are packages that contain model libraries.
 * Standard libraries use the 'standard library' prefix.
 *
 * Grammar:
 * ```
 * libraryPackage
 *     : isStandard=STANDARD LIBRARY
 *       prefixMetadataMember*
 *       packageDeclaration packageBody
 *     ;
 * ```
 *
 * LibraryPackage extends Package.
 */
class LibraryPackageVisitor : BaseTypedVisitor<KerMLParser.LibraryPackageContext, LibraryPackage>() {

    private val namespaceVisitor = NamespaceVisitor()

    override fun visit(ctx: KerMLParser.LibraryPackageContext, kermlParseContext: KermlParseContext): LibraryPackage {
        // Extract names before creating element (needed for deterministic library IDs)
        val identification = ctx.packageDeclaration()?.identification()
        val (shortName, name) = extractIdentificationNames(identification)

        // Create the LibraryPackage instance with the name for deterministic ID generation
        val libraryPkg = kermlParseContext.create<LibraryPackage>(
            declaredName = name,
            declaredShortName = shortName
        )

        // Set isStandard flag (the grammar requires 'standard library' prefix)
        ctx.isStandard?.let {
            libraryPkg.isStandard = true
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(libraryPkg, libraryPkg.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(libraryPkg, kermlParseContext)

        // Parse package body
        ctx.packageBody()?.let { body ->
            parsePackageBody(body, childContext)
        }

        // Parse prefix metadata members
        ctx.prefixMetadataMember()?.forEach { _ ->
            // TODO: Delegate to PrefixMetadataMemberVisitor
        }

        return libraryPkg
    }

    /**
     * Parse the package body.
     *
     * PackageBody uses the same namespaceBodyElement structure as NamespaceBody,
     * plus optional ElementFilterMember elements.
     */
    private fun parsePackageBody(
        ctx: KerMLParser.PackageBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        // Process each namespace body element using NamespaceVisitor
        ctx.namespaceBodyElement()?.forEach { bodyElement ->
            namespaceVisitor.parseNamespaceBodyElement(bodyElement, kermlParseContext)
        }

        // Process element filter members (specific to library packages)
        ctx.elementFilterMember()?.forEach { _ ->
            // TODO: Parse element filter members
        }
    }

    /**
     * Create ownership membership.
     */
    private fun createOwnershipMembership(libraryPkg: LibraryPackage, kermlParseContext: KermlParseContext) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = kermlParseContext.create<OwningMembership>()
                // Use ownedMemberElement (redefines memberElement) for proper ownership link
                membership.ownedMemberElement = libraryPkg
                libraryPkg.declaredName?.let { membership.memberName = it }
                libraryPkg.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }
}
