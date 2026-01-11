package org.openmbee.gearshift.query

import org.openmbee.gearshift.engine.MofObject
import org.openmbee.gearshift.repository.ModelRepository

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
    private val filters = mutableListOf<(MofObject) -> Boolean>()

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
    fun filter(predicate: (MofObject) -> Boolean): Query {
        filters.add(predicate)
        return this
    }

    /**
     * Execute the query and return results.
     */
    fun execute(): List<MofObject> {
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
    fun single(): MofObject? = execute().firstOrNull()

    /**
     * Execute the query and count results.
     */
    fun count(): Int = execute().size
}
