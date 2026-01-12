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
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader

/**
 * Example demonstrating how to use the KerML parser visitors.
 * This is a template that will be completed once ANTLR generates the parser.
 */
object KerMLParserExample {

    /**
     * Parse a KerML file (pseudocode - requires ANTLR parser generation).
     */
    fun parseKerMLFile(filePath: String): KerMLParseCoordinator {
        // Initialize engine and metamodel
        val engine = GearshiftEngine()
        KerMLMetamodelLoader.initialize(engine)

        // Create coordinator
        val coordinator = KerMLParseCoordinator(engine)

        // TODO: Once ANTLR parser is generated, uncomment and use:
        /*
        // Create ANTLR parser
        val input = CharStreams.fromPath(Paths.get(filePath))
        val lexer = KerMLLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)

        // Parse the root namespace
        val rootContext = parser.rootNamespace()
        coordinator.parseRootNamespace(rootContext)

        // Resolve references
        val unresolvedRefs = coordinator.resolveReferences()
        if (unresolvedRefs.isNotEmpty()) {
            println("Warning: ${unresolvedRefs.size} unresolved references:")
            unresolvedRefs.forEach { ref ->
                println("  - ${ref.sourceInstanceId}.${ref.propertyName} -> ${ref.targetQualifiedName}")
            }
        }

        // Print statistics
        val stats = coordinator.getParseStatistics()
        println("Parse complete:")
        println("  Total elements: ${stats.totalElements}")
        println("  By type:")
        stats.elementsByType.forEach { (type, count) ->
            println("    $type: $count")
        }
        */

        return coordinator
    }

    /**
     * Example: Parse a simple namespace manually (without ANTLR).
     * This demonstrates the visitor pattern in action.
     */
    fun manualParseExample() {
        val engine = GearshiftEngine()
        KerMLMetamodelLoader.initialize(engine)

        println("=== Manual Parse Example ===")
        println()

        // Create a namespace using the visitor
        val namespaceVisitor = KerMLVisitorFactory.NonFeatureElements.namespace

        // In a real implementation, we'd pass the ANTLR context
        // For now, we'll create instances directly to demonstrate the pattern
        val (namespaceId, _) = engine.createInstance("Namespace", "namespace-1")
        engine.setProperty(namespaceId, "name", "ExampleNamespace")

        println("Created namespace: $namespaceId")
        println("  name: ${engine.getProperty(namespaceId, "name")}")
        println()

        // Create a class within the namespace
        val classVisitor = KerMLVisitorFactory.NonFeatureElements.classElement
        val (classId, _) = engine.createInstance("Class", "class-1")
        engine.setProperty(classId, "name", "ExampleClass")
        engine.setProperty(classId, "isAbstract", false)

        println("Created class: $classId")
        println("  name: ${engine.getProperty(classId, "name")}")
        println("  isAbstract: ${engine.getProperty(classId, "isAbstract")}")
        println()

        // Create a feature within the class
        val featureVisitor = KerMLVisitorFactory.FeatureElements.feature
        val (featureId, _) = engine.createInstance("Feature", "feature-1")
        engine.setProperty(featureId, "name", "exampleFeature")
        engine.setProperty(featureId, "isComposite", true)

        println("Created feature: $featureId")
        println("  name: ${engine.getProperty(featureId, "name")}")
        println("  isComposite: ${engine.getProperty(featureId, "isComposite")}")
        println()

        // Show repository statistics
        val stats = engine.getStatistics()
        println("Repository statistics:")
        println("  Total instances: ${stats.objects.totalObjects}")
        println("  By type: ${stats.objects.typeDistribution}")
        println("  Total links: ${stats.links.totalLinks}")
    }

    /**
     * Example visitor implementation pattern (template).
     * Shows how to implement a complete visitor once ANTLR parser is available.
     */
    /*
    class ExampleNamespaceVisitorImpl : BaseKerMLVisitor<KerMLParser.NamespaceContext>() {
        override fun visit(ctx: KerMLParser.NamespaceContext, engine: GearshiftEngine): Any? {
            // Step 1: Create the instance
            val (instanceId, instance) = createInstance(engine, "Namespace")

            // Step 2: Parse and set identification
            ctx.namespaceDeclaration().identification()?.let { id ->
                id.declaredName?.text?.let { name ->
                    setProperty(engine, instanceId, "name", name)
                }
                id.declaredShortName?.text?.let { shortName ->
                    setProperty(engine, instanceId, "shortName", shortName)
                }
            }

            // Step 3: Parse prefix metadata members
            ctx.prefixMetadataMember()?.forEach { member ->
                val visitor = KerMLVisitorFactory.AnnotatingElements.prefixMetadataAnnotation
                visitor.visit(member, engine)
            }

            // Step 4: Parse namespace body
            ctx.namespaceBody()?.namespaceBodyElement()?.forEach { bodyElement ->
                when {
                    bodyElement.namespaceMember() != null -> {
                        parseNamespaceMember(bodyElement.namespaceMember(), engine, instanceId)
                    }
                    bodyElement.aliasMember() != null -> {
                        parseAliasMember(bodyElement.aliasMember(), engine, instanceId)
                    }
                    bodyElement.import_() != null -> {
                        parseImport(bodyElement.import_(), engine, instanceId)
                    }
                }
            }

            // Step 5: Return the instance ID
            return instanceId
        }

        private fun parseNamespaceMember(
            ctx: KerMLParser.NamespaceMemberContext,
            engine: GearshiftEngine,
            parentId: String
        ) {
            // Delegate to appropriate visitor based on member type
            when {
                ctx.nonFeatureMember() != null -> {
                    val memberCtx = ctx.nonFeatureMember()
                    // Parse visibility
                    val visibility = memberCtx.memberPrefix().visibilityIndicator()?.text

                    // Parse member element and delegate to appropriate visitor
                    // This would involve checking the member element type and calling
                    // the corresponding visitor from KerMLVisitorFactory
                }
                ctx.namespaceFeatureMember() != null -> {
                    // Parse feature member
                }
            }
        }

        private fun parseAliasMember(
            ctx: KerMLParser.AliasMemberContext,
            engine: GearshiftEngine,
            parentId: String
        ) {
            val (aliasId, _) = createInstance(engine, "AliasMember")

            ctx.memberShortName?.text?.let { shortName ->
                setProperty(engine, aliasId, "shortName", shortName)
            }

            ctx.memberName?.text?.let { name ->
                setProperty(engine, aliasId, "name", name)
            }

            // The alias refers to another element - record for later resolution
            val targetQualifiedName = ctx.memberElement.text
            // Record unresolved reference in coordinator
        }

        private fun parseImport(
            ctx: KerMLParser.Import_Context,
            engine: GearshiftEngine,
            parentId: String
        ) {
            val (importId, _) = createInstance(engine, "Import")

            val visibility = ctx.visibilityIndicator()?.text
            setProperty(engine, importId, "visibility", visibility ?: "public")

            val isImportAll = ctx.isImportAll != null
            setProperty(engine, importId, "isImportAll", isImportAll)

            // Parse import declaration
            ctx.importDeclaration()?.let { decl ->
                when {
                    decl.membershipImport() != null -> {
                        parseMembershipImport(decl.membershipImport(), engine, importId)
                    }
                    decl.namespaceImport() != null -> {
                        parseNamespaceImport(decl.namespaceImport(), engine, importId)
                    }
                }
            }
        }

        private fun parseMembershipImport(
            ctx: KerMLParser.MembershipImportContext,
            engine: GearshiftEngine,
            importId: String
        ) {
            val targetQualifiedName = ctx.importedMembership.text
            val isRecursive = ctx.isRecursive != null

            setProperty(engine, importId, "isRecursive", isRecursive)
            // Record reference to imported membership for later resolution
        }

        private fun parseNamespaceImport(
            ctx: KerMLParser.NamespaceImportContext,
            engine: GearshiftEngine,
            importId: String
        ) {
            ctx.importedNamespace?.let { namespace ->
                val targetQualifiedName = namespace.text
                // Record reference for later resolution
            }

            val isRecursive = ctx.isRecursive != null
            setProperty(engine, importId, "isRecursive", isRecursive)
        }
    }
    */
}

/**
 * Main function to run the examples.
 */
fun main() {
    println("KerML Parser Visitor Example")
    println("=" * 70)
    println()

    KerMLParserExample.manualParseExample()

    println()
    println("=" * 70)
    println()
    println("To parse actual KerML files:")
    println("1. Generate ANTLR parser: antlr4 -Dlanguage=Kotlin KerML.g4")
    println("2. Implement visitor methods with actual ANTLR context types")
    println("3. Use KerMLParserExample.parseKerMLFile(\"yourfile.kerml\")")
}

private operator fun String.times(n: Int): String = repeat(n)
