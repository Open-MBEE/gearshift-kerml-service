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

package org.openmbee.gearshift.kerml.parser.visitors.base

import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.antlr.KerMLParser


/**
 * Abstract base class providing common functionality for typed visitors.
 *
 * @param Ctx The ANTLR parser context type this visitor handles
 * @param Result The typed wrapper type this visitor produces
 */
abstract class BaseTypedVisitor<Ctx, Result : ModelElement> : TypedKerMLVisitor<Ctx, Result> {

    /**
     * Parse the standard identification pattern (declaredShortName, declaredName).
     * Many KerML elements share this pattern.
     */
    protected fun parseIdentification(
        identification: KerMLParser.IdentificationContext?,
        element: org.openmbee.gearshift.generated.interfaces.Element
    ) {
        identification?.let { id ->
            id.declaredShortName?.let { shortName ->
                element.declaredShortName = shortName.text
            }
            id.declaredName?.let { name ->
                element.declaredName = name.text
            }
        }
    }

    /**
     * Parse a memberPrefix to extract visibility.
     * Returns "public", "private", or "protected". Defaults to "public" if not specified.
     */
    protected fun parseMemberPrefix(ctx: KerMLParser.MemberPrefixContext?): String {
        ctx?.visibilityIndicator()?.let { vis ->
            vis.PUBLIC()?.let { return "public" }
            vis.PRIVATE()?.let { return "private" }
            vis.PROTECTED()?.let { return "protected" }
        }
        return "public"
    }

    /**
     * Extract a qualified name from a QualifiedNameContext.
     */
    protected fun extractQualifiedName(ctx: KerMLParser.QualifiedNameContext): String {
        val names = ctx.NAME().map { it.text }
        return if (ctx.DOLLAR() != null) {
            "\$::" + names.joinToString("::")
        } else {
            names.joinToString("::")
        }
    }

    /**
     * Compute the qualified name for an element given its parent qualified name.
     */
    protected fun computeQualifiedName(parentQualifiedName: String, declaredName: String?): String {
        return when {
            declaredName.isNullOrEmpty() -> parentQualifiedName
            parentQualifiedName.isEmpty() -> declaredName
            else -> "$parentQualifiedName::$declaredName"
        }
    }
}
