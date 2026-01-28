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

import org.openmbee.gearshift.generated.interfaces.Annotation
import org.openmbee.gearshift.generated.interfaces.Comment
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Comment elements.
 *
 * Per KerML spec 8.2.3.3.2: Comments are annotating elements containing text.
 *
 * Grammar:
 * ```
 * comment
 *     : ( COMMENT identification
 *         ( ABOUT ownedRelationship+=annotation
 *           ( COMMA ownedRelationship+=annotation )*
 *         )?
 *       )?
 *       ( LOCALE locale=STRING_VALUE )?
 *       body=REGULAR_COMMENT
 *     ;
 * ```
 *
 * Comment extends AnnotatingElement.
 */
class CommentVisitor : BaseTypedVisitor<KerMLParser.CommentContext, Comment>() {

    override fun visit(ctx: KerMLParser.CommentContext, parseContext: ParseContext): Comment {
        val comment = parseContext.create<Comment>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), comment)

        // Parse locale (optional)
        ctx.locale?.let { locale ->
            comment.locale = parseStringValue(locale.text)
        }

        // Parse comment body (REGULAR_COMMENT)
        ctx.body?.let { body ->
            comment.body = parseCommentBody(body.text)
        }

        // Parse annotations (about clause)
        ctx.annotation()?.forEach { annotationCtx ->
            parseAnnotationAbout(annotationCtx, comment, parseContext)
        }

        // Create membership with parent namespace
        createAnnotatingElementMembership(comment, parseContext)

        return comment
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
     * Parse an annotation reference in the 'about' clause.
     */
    private fun parseAnnotationAbout(
        ctx: KerMLParser.AnnotationContext,
        comment: Comment,
        parseContext: ParseContext
    ) {
        val annotation = parseContext.create<Annotation>()
        annotation.ownedAnnotatingElement = comment

        ctx.qualifiedName()?.let { qn ->
            val targetName = extractQualifiedName(qn)
            parseContext.registerReference(annotation, "annotatedElement", targetName)
        }
    }

    /**
     * Create an OwningMembership linking the annotating element to its parent.
     */
    private fun createAnnotatingElementMembership(
        comment: Comment,
        parseContext: ParseContext
    ) {
        parseContext.parent?.let { parent ->
            if (parent is Namespace) {
                val membership = parseContext.create<OwningMembership>()
                membership.memberElement = comment
                comment.declaredName?.let { membership.memberName = it }
                comment.declaredShortName?.let { membership.memberShortName = it }
                membership.membershipOwningNamespace = parent
            }
        }
    }
}
