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
import org.openmbee.gearshift.generated.interfaces.MetadataFeature
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for MetadataFeature elements.
 *
 * Per KerML spec 8.2.3.3.4: MetadataFeature is both an AnnotatingElement and a Feature.
 *
 * Grammar:
 * ```
 * metadataFeature
 *     : prefixMetadataMember*
 *       ( AT | METADATA )
 *       metadataFeatureDeclaration
 *       ( ABOUT annotation
 *         ( COMMA annotation )*
 *       )?
 *       metadataBody
 *     ;
 * ```
 *
 * MetadataFeature extends Feature and AnnotatingElement.
 */
class MetadataFeatureVisitor : BaseFeatureVisitor<KerMLParser.MetadataFeatureContext, MetadataFeature>() {

    override fun visit(ctx: KerMLParser.MetadataFeatureContext, parseContext: ParseContext): MetadataFeature {
        val metadata = parseContext.create<MetadataFeature>()

        // Parse metadata feature declaration
        ctx.metadataFeatureDeclaration()?.let { decl ->
            parseMetadataFeatureDeclaration(decl, metadata, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(metadata, metadata.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(metadata, parseContext)

        // Parse annotations (about clause)
        ctx.annotation()?.forEach { annotationCtx ->
            parseAnnotationAbout(annotationCtx, metadata, parseContext)
        }

        // Parse metadata body
        ctx.metadataBody()?.let { body ->
            parseMetadataBody(body, childContext)
        }

        return metadata
    }

    /**
     * Parse metadata feature declaration.
     */
    private fun parseMetadataFeatureDeclaration(
        ctx: KerMLParser.MetadataFeatureDeclarationContext,
        metadata: MetadataFeature,
        parseContext: ParseContext
    ) {
        // Parse identification (optional)
        ctx.identification()?.let { id ->
            parseIdentification(id, metadata)
        }

        // Parse owned feature typing
        ctx.ownedFeatureTyping()?.let { typing ->
            typing.generalType()?.qualifiedName()?.let { qn ->
                val typeName = extractQualifiedName(qn)
                // TODO: Resolve and set typing relationship
            }
        }
    }

    /**
     * Parse an annotation reference in the 'about' clause.
     */
    private fun parseAnnotationAbout(
        ctx: KerMLParser.AnnotationContext,
        metadata: MetadataFeature,
        parseContext: ParseContext
    ) {
        val annotation = parseContext.create<Annotation>()
        annotation.ownedAnnotatingElement = metadata

        ctx.qualifiedName()?.let { qn ->
            val targetName = extractQualifiedName(qn)
            // TODO: Resolve and set annotation.annotatedElement
        }
    }

    /**
     * Parse metadata body.
     */
    private fun parseMetadataBody(
        ctx: KerMLParser.MetadataBodyContext,
        parseContext: ParseContext
    ) {
        ctx.metadataBodyElement()?.forEach { bodyElement ->
            // Non-feature member
            bodyElement.nonFeatureMember()?.let { nonFeature ->
                NamespaceVisitor().parseNonFeatureMember(nonFeature, parseContext)
            }

            // Metadata body feature member
            bodyElement.metadataBodyFeatureMember()?.metadataBodyFeature()?.let { bodyFeature ->
                parseMetadataBodyFeature(bodyFeature, parseContext)
            }
        }
    }

    /**
     * Parse metadata body feature.
     */
    private fun parseMetadataBodyFeature(
        ctx: KerMLParser.MetadataBodyFeatureContext,
        parseContext: ParseContext
    ) {
        // Creates a feature with redefinition and optional value
        val feature = parseContext.create<org.openmbee.gearshift.generated.interfaces.Feature>()

        // Parse redefinition
        ctx.ownedRedefinition()?.let { redef ->
            redef.generalType()?.qualifiedName()?.let { qn ->
                val redefinedName = extractQualifiedName(qn)
                // TODO: Resolve and create redefinition relationship
            }
        }

        // Parse feature specialization part (inherited)
        ctx.featureSpecializationPart()?.let { specPart ->
            parseFeatureSpecializationPart(specPart, feature, parseContext)
        }

        // Parse value part (inherited)
        parseValuePart(ctx.valuePart(), feature, parseContext)

        // Create feature membership
        createFeatureMembership(feature, parseContext)

        // Recursively parse nested metadata body
        val childContext = parseContext.withParent(feature, feature.declaredName ?: "")
        ctx.metadataBody()?.let { body ->
            parseMetadataBody(body, childContext)
        }
    }
}
