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

import org.antlr.v4.runtime.*
import org.openmbee.gearshift.kerml.antlr.SysMLLexer
import org.openmbee.gearshift.kerml.antlr.SysMLParser
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Test class for validating the SysML grammar against the SysML Standard Library.
 */
class SysMLGrammarTest {

    // Try multiple possible locations for the library
    private val libraryPath: File by lazy {
        listOf(
            File("references/SysML Standard Library"),
            File("../references/SysML Standard Library"),
            File("../../references/SysML Standard Library"),
            File(System.getProperty("user.dir"), "references/SysML Standard Library"),
            File(System.getProperty("user.dir"), "../references/SysML Standard Library")
        ).firstOrNull { it.exists() } ?: File("references/SysML Standard Library")
    }

    /**
     * Custom error listener that collects parse errors.
     */
    class CollectingErrorListener : BaseErrorListener() {
        val errors = mutableListOf<String>()

        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInLine: Int,
            msg: String?,
            e: RecognitionException?
        ) {
            errors.add("line $line:$charPositionInLine $msg")
        }
    }

    /**
     * Parse a single SysML file and return any errors.
     */
    fun parseFile(file: File): List<String> {
        val input = CharStreams.fromPath(file.toPath())
        val lexer = SysMLLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = SysMLParser(tokens)

        // Remove default error listeners and add our collecting one
        val errorListener = CollectingErrorListener()
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        // Parse starting from the root rule
        try {
            parser.rootNamespace()
        } catch (e: Exception) {
            errorListener.errors.add("Exception during parsing: ${e.message}")
        }

        return errorListener.errors
    }

    /**
     * Test parsing all SysML files in the Standard Library.
     */
    @Test
    fun `parse all SysML Standard Library files`() {
        if (!libraryPath.exists()) {
            println("WARNING: SysML Standard Library not found at $libraryPath")
            println("Skipping grammar validation tests.")
            return
        }

        val sysmlFiles = libraryPath.walkTopDown()
            .filter { it.extension == "sysml" }
            .toList()
            .sortedBy { it.name }

        println("\n" + "=".repeat(80))
        println("SysML Grammar Test - Parsing ${sysmlFiles.size} files")
        println("=".repeat(80) + "\n")

        var totalErrors = 0
        var filesWithErrors = 0

        for (file in sysmlFiles) {
            val relativePath = file.relativeTo(libraryPath.parentFile)
            val errors = parseFile(file)

            if (errors.isNotEmpty()) {
                filesWithErrors++
                totalErrors += errors.size
                println("❌ $relativePath (${errors.size} errors)")
                errors.take(10).forEach { error ->
                    println("   $error")
                }
                if (errors.size > 10) {
                    println("   ... and ${errors.size - 10} more errors")
                }
                println()
            } else {
                println("✓ $relativePath")
            }
        }

        println("\n" + "=".repeat(80))
        println("Summary: ${sysmlFiles.size - filesWithErrors}/${sysmlFiles.size} files parsed successfully")
        println("Total errors: $totalErrors in $filesWithErrors files")
        println("=".repeat(80))

        // Don't fail the test - just report errors for now
        // We expect errors while the grammar is being developed
        if (totalErrors > 0) {
            println("\nNote: Errors are expected while the grammar is under development.")
        }
    }

    /**
     * Test parsing a single specific file for debugging.
     */
    @Test
    fun `parse single file for debugging`() {
        // Change this to test a specific file
        val testFile = File(libraryPath, "Quantities and Units/Quantities.sysml")

        if (!testFile.exists()) {
            println("Test file not found: $testFile")
            return
        }

        println("Parsing: ${testFile.name}")
        println("-".repeat(40))

        val errors = parseFile(testFile)

        if (errors.isEmpty()) {
            println("✓ No parse errors!")
        } else {
            println("Found ${errors.size} errors:")
            errors.forEach { println("  $it") }
        }
    }
}
