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
package org.openmbee.mdm.framework.query.gql

import org.openmbee.mdm.framework.query.gql.ast.GqlQuery
import org.openmbee.mdm.framework.query.gql.executor.BindingTable
import org.openmbee.mdm.framework.query.gql.executor.GqlExecutor
import org.openmbee.mdm.framework.query.gql.parser.GqlParser
import org.openmbee.mdm.framework.runtime.MDMEngine

/**
 * Public API for executing GQL queries against MDM models.
 *
 * Usage:
 * ```kotlin
 * val engine: MDMEngine = ...
 * val results = GqlQueryExecutor.execute("MATCH (n:Classifier) RETURN n.name", engine)
 * ```
 *
 * Or using the extension function:
 * ```kotlin
 * val results = engine.query("MATCH (n:Classifier) RETURN n.name")
 * ```
 */
object GqlQueryExecutor {

    /**
     * Parse a GQL query string into an AST.
     */
    fun parse(query: String): GqlQuery {
        return GqlParser.parse(query)
    }

    /**
     * Execute a GQL query string against an MDMEngine.
     *
     * @param query The GQL query string
     * @param engine The MDMEngine to query against
     * @return A BindingTable with the query results
     */
    fun execute(query: String, engine: MDMEngine): BindingTable {
        val ast = GqlParser.parse(query)
        val executor = GqlExecutor(engine)
        return executor.execute(ast)
    }

    /**
     * Execute a pre-parsed GQL query against an MDMEngine.
     *
     * @param query The parsed GQL query AST
     * @param engine The MDMEngine to query against
     * @return A BindingTable with the query results
     */
    fun execute(query: GqlQuery, engine: MDMEngine): BindingTable {
        val executor = GqlExecutor(engine)
        return executor.execute(query)
    }
}

/**
 * Extension function to execute GQL queries on an MDMEngine.
 *
 * Usage:
 * ```kotlin
 * val engine: MDMEngine = ...
 * val results = engine.query("MATCH (n:Classifier) RETURN n.name")
 * for (row in results) {
 *     println(row["n.name"])
 * }
 * ```
 */
fun MDMEngine.query(gql: String): BindingTable {
    return GqlQueryExecutor.execute(gql, this)
}

/**
 * Extension function to execute a pre-parsed GQL query on an MDMEngine.
 */
fun MDMEngine.query(gql: GqlQuery): BindingTable {
    return GqlQueryExecutor.execute(gql, this)
}
