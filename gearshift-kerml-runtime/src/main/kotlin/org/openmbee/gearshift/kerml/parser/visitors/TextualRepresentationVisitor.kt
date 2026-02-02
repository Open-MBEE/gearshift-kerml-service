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

import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.TextualRepresentation
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor

/**
 * Visitor for TextualRepresentation elements.
 *
 * Per KerML spec 8.2.3.3.3: TextualRepresentation provides a textual notation for its owner.
 *
 * Grammar:
 * ```
 * textualRepresentation
 *     : ( REP identification )?
 *       LANGUAGE language=STRING_VALUE
 *       body=REGULAR_COMMENT
 *     ;
 * ```
 *
 * TextualRepresentation extends AnnotatingElement.
 */
class TextualRepresentationVisitor :
    BaseTypedVisitor<KerMLParser.TextualRepresentationContext, TextualRepresentation>() {

    override fun visit(
        ctx: KerMLParser.TextualRepresentationContext,
        kermlParseContext: KermlParseContext
    ): TextualRepresentation {
        val textRep = kermlParseContext.create<TextualRepresentation>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), textRep)

        // Parse language
        ctx.language?.let { lang ->
            textRep.language = parseStringValue(lang.text)
        }

        // Parse body (REGULAR_COMMENT)
        ctx.body?.let { body ->
            textRep.body = parseCommentBody(body.text)
        }

        // Create membership with parent namespace
        createAnnotatingElementMembership(textRep, kermlParseContext)

        return textRep
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
        textRep: TextualRepresentation,
        kermlParseContext: KermlParseContext
    ) {
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = kermlParseContext.create<OwningMembership>()
                // Use ownedMemberElement (redefines memberElement) for proper ownership link
                membership.ownedMemberElement = textRep
                textRep.declaredName?.let { membership.memberName = it }
                textRep.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }
}
