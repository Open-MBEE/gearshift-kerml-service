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

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Interaction
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.Predicate
import org.openmbee.gearshift.generated.interfaces.Step
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Tests for parsing KerML behaviors, functions, predicates, and related elements.
 */
class KerMLBehaviorParseTest : DescribeSpec({

    fun parseKerML(source: String): Pair<Namespace, GearshiftEngine> {
        val engine = GearshiftEngine()
        KerMLMetamodelLoader.initialize(engine)

        val lexer = KerMLLexer(CharStreams.fromString(source))
        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)

        val tree = parser.rootNamespace()

        val parseContext = ParseContext(engine)
        val rootNamespace = RootNamespaceVisitor().visit(tree, parseContext)

        return rootNamespace to engine
    }

    describe("BehaviorVisitor") {

        it("should parse a simple behavior") {
            val (root, _) = parseKerML("""
                package Behaviors {
                    behavior DoSomething;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behaviors = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .filter { it !is KerMLFunction }

            behaviors shouldHaveSize 1
            behaviors.first().declaredName shouldBe "DoSomething"
        }

        it("should parse abstract behavior") {
            val (root, _) = parseKerML("""
                package Behaviors {
                    abstract behavior Process;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behaviors = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .filter { it !is KerMLFunction }

            behaviors shouldHaveSize 1
            behaviors.first().isAbstract.shouldBeTrue()
        }

        it("should parse behavior with steps") {
            val (root, _) = parseKerML("""
                package Behaviors {
                    behavior Process {
                        step initialize;
                        step execute;
                        step finalize;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behavior = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .filter { it !is KerMLFunction }
                .first()

            behavior.declaredName shouldBe "Process"

            val steps = behavior.ownedMembership
                .mapNotNull { it.memberElement as? Step }

            steps shouldHaveSize 3
            steps.map { it.declaredName } shouldBe listOf("initialize", "execute", "finalize")
        }
    }

    describe("FunctionVisitor") {

        it("should parse a simple function") {
            val (root, _) = parseKerML("""
                package Functions {
                    function compute;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val functions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? KerMLFunction }
                .filter { it !is Predicate }

            functions shouldHaveSize 1
            functions.first().declaredName shouldBe "compute"
        }

        it("should parse abstract function") {
            val (root, _) = parseKerML("""
                package Functions {
                    abstract function calculate;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val functions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? KerMLFunction }
                .filter { it !is Predicate }

            functions shouldHaveSize 1
            functions.first().isAbstract.shouldBeTrue()
        }

        it("should parse function with parameters") {
            val (root, _) = parseKerML("""
                package Functions {
                    function add {
                        in feature x;
                        in feature y;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val function = pkg.ownedMembership
                .mapNotNull { it.memberElement as? KerMLFunction }
                .filter { it !is Predicate }
                .first()

            function.declaredName shouldBe "add"

            val features = function.ownedMembership
                .mapNotNull { it.memberElement as? Feature }

            features shouldHaveSize 2
            features.all { it.direction == "in" }.shouldBeTrue()
        }
    }

    describe("PredicateVisitor") {

        it("should parse a simple predicate") {
            val (root, _) = parseKerML("""
                package Predicates {
                    predicate isValid;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val predicates = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Predicate }

            predicates shouldHaveSize 1
            predicates.first().declaredName shouldBe "isValid"
        }

        it("should parse abstract predicate") {
            val (root, _) = parseKerML("""
                package Predicates {
                    abstract predicate check;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val predicates = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Predicate }

            predicates shouldHaveSize 1
            predicates.first().isAbstract.shouldBeTrue()
        }
    }

    describe("InteractionVisitor") {

        it("should parse a simple interaction") {
            val (root, _) = parseKerML("""
                package Interactions {
                    interaction MessageExchange;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val interactions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Interaction }

            interactions shouldHaveSize 1
            interactions.first().declaredName shouldBe "MessageExchange"
        }

        it("should parse abstract interaction") {
            val (root, _) = parseKerML("""
                package Interactions {
                    abstract interaction Communication;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val interactions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Interaction }

            interactions shouldHaveSize 1
            interactions.first().isAbstract.shouldBeTrue()
        }
    }

    describe("StepVisitor") {

        it("should parse a simple step") {
            val (root, _) = parseKerML("""
                package Behaviors {
                    behavior Process {
                        step doWork;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behavior = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .first()

            val steps = behavior.ownedMembership
                .mapNotNull { it.memberElement as? Step }

            steps shouldHaveSize 1
            steps.first().declaredName shouldBe "doWork"
        }

        it("should parse step with modifiers") {
            val (root, _) = parseKerML("""
                package Behaviors {
                    behavior Process {
                        abstract step action;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behavior = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .first()

            val steps = behavior.ownedMembership
                .mapNotNull { it.memberElement as? Step }

            steps shouldHaveSize 1
            steps.first().isAbstract.shouldBeTrue()
        }
    }

    describe("mixed behaviors") {

        it("should parse package with multiple behavior types") {
            val (root, _) = parseKerML("""
                package Model {
                    behavior Process;
                    function compute;
                    predicate isValid;
                    interaction Exchange;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val behaviors = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Behavior }
                .filter { it !is KerMLFunction && it !is Interaction }
            val functions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? KerMLFunction }
                .filter { it !is Predicate }
            val predicates = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Predicate }
            val interactions = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Interaction }

            behaviors shouldHaveSize 1
            functions shouldHaveSize 1
            predicates shouldHaveSize 1
            interactions shouldHaveSize 1

            behaviors.first().declaredName shouldBe "Process"
            functions.first().declaredName shouldBe "compute"
            predicates.first().declaredName shouldBe "isValid"
            interactions.first().declaredName shouldBe "Exchange"
        }
    }
})
