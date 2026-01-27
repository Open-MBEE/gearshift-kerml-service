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
import org.openmbee.gearshift.generated.interfaces.Association
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.generated.interfaces.DataType
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.Structure
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Tests for parsing KerML generic Type and Classifier elements.
 */
class KerMLTypeClassifierParseTest : DescribeSpec({

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

    describe("TypeVisitor") {

        it("should parse a simple type") {
            val (root, _) = parseKerML("""
                package Types {
                    type MyType :> Base;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val types = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Type }
                .filter { it !is Classifier }

            types shouldHaveSize 1
            types.first().declaredName shouldBe "MyType"
        }

        it("should parse abstract type") {
            val (root, _) = parseKerML("""
                package Types {
                    abstract type AbstractType :> Base;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val types = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Type }
                .filter { it !is Classifier }

            types shouldHaveSize 1
            types.first().isAbstract.shouldBeTrue()
        }

        it("should parse type with features") {
            val (root, _) = parseKerML("""
                package Types {
                    type Container :> Base {
                        feature item;
                        feature count;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val type = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Type }
                .filter { it !is Classifier }
                .first()

            type.declaredName shouldBe "Container"

            val features = type.ownedMembership
                .mapNotNull { it.memberElement as? Feature }

            features shouldHaveSize 2
            features.map { it.declaredName } shouldBe listOf("item", "count")
        }
    }

    describe("ClassifierVisitor") {

        it("should parse a simple classifier") {
            val (root, _) = parseKerML("""
                package Classifiers {
                    classifier MyClassifier;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val classifiers = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Classifier }
                .filter { it !is KerMLClass && it !is DataType && it !is Structure && it !is Association && it !is Behavior }

            classifiers shouldHaveSize 1
            classifiers.first().declaredName shouldBe "MyClassifier"
        }

        it("should parse abstract classifier") {
            val (root, _) = parseKerML("""
                package Classifiers {
                    abstract classifier AbstractClassifier;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val classifiers = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Classifier }
                .filter { it !is KerMLClass && it !is DataType && it !is Structure && it !is Association && it !is Behavior }

            classifiers shouldHaveSize 1
            classifiers.first().isAbstract.shouldBeTrue()
        }

        it("should parse classifier with features") {
            val (root, _) = parseKerML("""
                package Classifiers {
                    classifier Vehicle {
                        feature wheels;
                        feature engine;
                    }
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val classifier = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Classifier }
                .filter { it !is KerMLClass && it !is DataType && it !is Structure && it !is Association && it !is Behavior }
                .first()

            classifier.declaredName shouldBe "Vehicle"

            val features = classifier.ownedMembership
                .mapNotNull { it.memberElement as? Feature }

            features shouldHaveSize 2
            features.map { it.declaredName } shouldBe listOf("wheels", "engine")
        }

        it("should parse classifier with superclassing") {
            val (root, _) = parseKerML("""
                package Classifiers {
                    classifier Car :> Vehicle;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val classifiers = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Classifier }
                .filter { it !is KerMLClass && it !is DataType && it !is Structure && it !is Association && it !is Behavior }

            classifiers shouldHaveSize 1
            classifiers.first().declaredName shouldBe "Car"
        }
    }

    describe("mixed types and classifiers") {

        it("should parse package with types and classifiers") {
            val (root, _) = parseKerML("""
                package Model {
                    type BaseType :> Anything;
                    classifier BaseClassifier;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            val types = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Type }
                .filter { it !is Classifier }

            val classifiers = pkg.ownedMembership
                .mapNotNull { it.memberElement as? Classifier }
                .filter { it !is KerMLClass && it !is DataType && it !is Structure && it !is Association && it !is Behavior }

            types shouldHaveSize 1
            classifiers shouldHaveSize 1

            types.first().declaredName shouldBe "BaseType"
            classifiers.first().declaredName shouldBe "BaseClassifier"
        }
    }
})
