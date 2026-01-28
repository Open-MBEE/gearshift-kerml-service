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
package org.openmbee.gearshift.kerml.parser

import org.openmbee.gearshift.GearshiftEngine

/**
 * Visitor for Comment elements.
 * Per KerML spec 8.2.3.3.2: Comments provide textual annotations about elements.
 */
class CommentVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement comment parsing
        // Extract: identification, about annotations, locale, body (REGULAR_COMMENT)
        val (instanceId, instance) = createInstance(engine, "Comment")

        // Parse optional identification
        // Parse 'about' annotations (comma-separated qualified names)
        // Parse optional locale (STRING_VALUE)
        // Parse body (REGULAR_COMMENT token)

        return instanceId
    }
}

/**
 * Visitor for Documentation elements.
 * Per KerML spec 8.2.3.3.2: Documentation provides descriptive comments.
 */
class DocumentationVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement documentation parsing
        // Extract: identification, locale, body
        val (instanceId, instance) = createInstance(engine, "Documentation")

        // Parse identification
        // Parse optional locale (STRING_VALUE)
        // Parse body (REGULAR_COMMENT token)

        return instanceId
    }
}

/**
 * Visitor for TextualRepresentation elements.
 * Per KerML spec 8.2.3.3.3: Textual representations define alternative syntax.
 */
class TextualRepresentationVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement textual representation parsing
        // Extract: identification, language, body
        val (instanceId, instance) = createInstance(engine, "TextualRepresentation")

        // Parse optional identification ('rep' Identification)
        // Parse language (STRING_VALUE)
        // Parse body (REGULAR_COMMENT token)

        return instanceId
    }
}

/**
 * Visitor for MetadataFeature elements.
 * Per KerML spec 8.2.5.12: Metadata features provide annotations with structured metadata.
 */
class MetadataFeatureVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement metadata feature parsing
        val (instanceId, instance) = createInstance(engine, "MetadataFeature")

        // Parse metadata typing
        // Parse metadata values
        // Parse about relationship (annotated elements)

        return instanceId
    }
}

/**
 * Visitor for Annotation elements.
 * Per KerML spec 8.2.3.3.1: Annotations reference annotated elements.
 */
class AnnotationVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement annotation parsing
        // Extract: annotatedElement (QualifiedName)
        val (instanceId, instance) = createInstance(engine, "Annotation")

        // Parse annotated element reference

        return instanceId
    }
}

/**
 * Visitor for OwnedAnnotation elements.
 * Per KerML spec: Owned annotations contain annotating elements.
 */
class OwnedAnnotationVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement owned annotation parsing
        // Extract: annotatingElement
        val (instanceId, instance) = createInstance(engine, "OwnedAnnotation")

        // Parse and delegate to appropriate annotating element visitor

        return instanceId
    }
}

/**
 * Visitor for PrefixMetadataAnnotation elements.
 * Per KerML spec: Prefix metadata annotations appear before element declarations.
 */
class PrefixMetadataAnnotationVisitor : BaseKerMLVisitor<Any>() {
    override fun visit(ctx: Any, engine: GearshiftEngine): Any? {
        // TODO: Implement prefix metadata annotation parsing
        val (instanceId, instance) = createInstance(engine, "PrefixMetadataAnnotation")

        // Parse metadata reference or feature
        // Parse '#' prefix syntax

        return instanceId
    }
}
