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
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypedVisitor

/**
 * Visitor for the root namespace (entry point for KerML parsing).
 *
 * The root namespace is an implicit namespace that contains all top-level elements
 * in a KerML file. It doesn't have a declaration or explicit body - just a sequence
 * of namespace body elements.
 *
 * Grammar:
 * ```
 * rootNamespace
 *     : namespaceBodyElement*
 *     ;
 * ```
 */
class RootNamespaceVisitor : BaseTypedVisitor<KerMLParser.RootNamespaceContext, Namespace>() {

    private val namespaceVisitor = NamespaceVisitor()

    override fun visit(ctx: KerMLParser.RootNamespaceContext, kermlParseContext: KermlParseContext): Namespace {
        // Create an implicit root namespace to hold all top-level elements.
        // Per spec: "A root namespace is a namespace that has no owner."
        // Top-level elements ARE owned by the root namespace, but the root namespace has no owner.
        val rootNamespace = kermlParseContext.create<Namespace>()

        // Create child context with root as parent
        val childContext = kermlParseContext.withParent(rootNamespace, "")

        // Process each namespace body element as children of the root namespace
        ctx.namespaceBodyElement()?.forEach { bodyElement ->
            namespaceVisitor.parseNamespaceBodyElement(bodyElement, childContext)
        }

        return rootNamespace
    }
}
