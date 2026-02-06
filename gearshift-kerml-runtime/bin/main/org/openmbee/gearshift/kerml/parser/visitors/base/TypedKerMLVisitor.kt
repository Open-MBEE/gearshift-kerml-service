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
import org.openmbee.gearshift.kerml.parser.KermlParseContext


/**
 * Base interface for typed KerML visitors.
 *
 * Each visitor handles a specific ANTLR parser context type (Ctx) and produces
 * a typed wrapper result (Result).
 *
 * @param Ctx The ANTLR parser context type this visitor handles
 * @param Result The typed wrapper type this visitor produces
 */
interface TypedKerMLVisitor<Ctx, Result : ModelElement> {

    /**
     * Visit the ANTLR context and produce a typed wrapper.
     *
     * @param ctx The ANTLR parser context
     * @param kermlParseContext The parsing context with engine and parent info
     * @return The created element as a typed wrapper
     */
    fun visit(ctx: Ctx, kermlParseContext: KermlParseContext): Result
}
