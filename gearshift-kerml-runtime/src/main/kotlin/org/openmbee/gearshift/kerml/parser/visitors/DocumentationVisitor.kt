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

import org.openmbee.gearshift.generated.interfaces.Documentation
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for Documentation elements.
 *
 * Per KerML spec 8.2.3.3.2: Documentation is a Comment owned by its documented element.
 *
 * Grammar:
 * ```
 * documentation
 *     : DOC identification
 *       ( LOCALE locale=STRING_VALUE )?
 *       body=REGULAR_COMMENT
 *     ;
 * ```
 *
 * Documentation extends Comment.
 */
class DocumentationVisitor : BaseTypedVisitor<KerMLParser.DocumentationContext, Documentation>() {

    override fun visit(ctx: KerMLParser.DocumentationContext, parseContext: ParseContext): Documentation {
        val documentation = parseContext.create<Documentation>()

        // Parse identification
        parseIdentification(ctx.identification(), documentation)

        // Parse locale (optional)
        ctx.locale?.let { locale ->
            documentation.locale = parseStringValue(locale.text)
        }

        // Parse documentation body (REGULAR_COMMENT)
        ctx.body?.let { body ->
            documentation.body = parseCommentBody(body.text)
        }

        // Create membership with parent namespace
        createAnnotatingElementMembership(documentation, parseContext)

        return documentation
    }

    /**
     * Parse a string value, removing quotes.
     */
    private fun parseStringValue(text: String): String {
        return text.trim().removeSurrounding("\"")
    }

    /**
     * Parse comment body, stripping the comment delimiters.
     */
    private fun parseCommentBody(text: String): String {
        // REGULAR_COMMENT is /* ... */
        return text.trim()
            .removePrefix("/*")
            .removeSuffix("*/")
            .trim()
    }

    /**
     * Create an OwningMembership linking the annotating element to its parent.
     */
    private fun createAnnotatingElementMembership(
        documentation: Documentation,
        parseContext: ParseContext
    ) {
        parseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = parseContext.create<OwningMembership>()
                membership.memberElement = documentation
                documentation.declaredName?.let { membership.memberName = it }
                documentation.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }
}
