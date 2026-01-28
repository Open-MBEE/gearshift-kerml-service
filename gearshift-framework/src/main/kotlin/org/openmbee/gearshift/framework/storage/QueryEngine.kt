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
package org.openmbee.gearshift.framework.storage

import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.framework.storage.ModelRepository

/**
 * Query engine for searching and filtering model objects.
 * Provides a fluent API for building queries.
 */
class QueryEngine(private val repository: ModelRepository) {

    /**
     * Start a query for objects of a specific type.
     */
    fun from(className: String): Query {
        return Query(repository, className)
    }

    /**
     * Start a query for all objects.
     */
    fun all(): Query {
        return Query(repository, null)
    }
}

/**
 * Represents a query being built.
 */
class Query(
    private val repository: ModelRepository,
    private val className: String?
) {
    private val filters = mutableListOf<(MDMObject) -> Boolean>()

    /**
     * Filter by a property value.
     */
    fun where(propertyName: String, value: Any): Query {
        filters.add { obj ->
            obj.getProperty(propertyName) == value
        }
        return this
    }

    /**
     * Filter by a custom predicate.
     */
    fun filter(predicate: (MDMObject) -> Boolean): Query {
        filters.add(predicate)
        return this
    }

    /**
     * Execute the query and return results.
     */
    fun execute(): List<MDMObject> {
        val baseResults = if (className != null) {
            repository.getByType(className)
        } else {
            repository.getAll()
        }

        return baseResults.filter { obj ->
            filters.all { filter -> filter(obj) }
        }
    }

    /**
     * Execute the query and return a single result.
     */
    fun single(): MDMObject? = execute().firstOrNull()

    /**
     * Execute the query and count results.
     */
    fun count(): Int = execute().size
}
