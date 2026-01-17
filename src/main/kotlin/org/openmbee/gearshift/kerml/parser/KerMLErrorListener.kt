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

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

/**
 * Error listener for KerML parsing that collects syntax errors.
 */
class KerMLErrorListener : BaseErrorListener() {
    private val _errors = mutableListOf<KerMLParseError>()

    /**
     * List of all collected parse errors.
     */
    val errors: List<KerMLParseError> get() = _errors

    /**
     * Returns true if any errors were collected.
     */
    val hasErrors: Boolean get() = _errors.isNotEmpty()

    override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String?,
        e: RecognitionException?
    ) {
        _errors.add(
            KerMLParseError(
                line = line,
                column = charPositionInLine,
                message = msg ?: "Unknown syntax error",
                offendingSymbol = offendingSymbol?.toString()
            )
        )
    }

    /**
     * Clear all collected errors.
     */
    fun clear() {
        _errors.clear()
    }
}

/**
 * Represents a parse error in KerML source code.
 */
data class KerMLParseError(
    val line: Int,
    val column: Int,
    val message: String,
    val offendingSymbol: String? = null
) {
    override fun toString(): String {
        return "Line $line:$column - $message${offendingSymbol?.let { " (at '$it')" } ?: ""}"
    }
}
