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
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.interfaces.Association
import org.openmbee.gearshift.generated.interfaces.AssociationStructure
import org.openmbee.gearshift.generated.interfaces.Connector
import org.openmbee.gearshift.generated.interfaces.DataType
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.Structure
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Tests for parsing KerML classifiers (DataType, Structure, Association, Connector)
 * using the new typed visitor infrastructure.
 */
class KerMLClassifierParseTest : DescribeSpec({

    /**
     * Helper to parse KerML source and return the root namespace.
     */
    fun parseKerML(source: String): Pair<Namespace, GearshiftEngine> {
        val engine = GearshiftEngine()
        KerMLMetamodelLoader.initialize(engine.metamodelRegistry)

        val lexer = KerMLLexer(CharStreams.fromString(source))
        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)

        val tree = parser.rootNamespace()

        val parseContext = ParseContext(engine)
        val rootNamespace = RootNamespaceVisitor().visit(tree, parseContext)

        return rootNamespace to engine
    }

    describe("DataTypeVisitor") {

        context("parsing datatype declarations") {

            it("should parse a simple datatype") {
                val (root, _) = parseKerML("""
                    package Types {
                        datatype Integer;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val dataTypes = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? DataType }

                dataTypes shouldHaveSize 1
                dataTypes.first().declaredName shouldBe "Integer"
            }

            it("should parse abstract datatype") {
                val (root, _) = parseKerML("""
                    package Types {
                        abstract datatype Number;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val dataTypes = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? DataType }

                dataTypes shouldHaveSize 1
                dataTypes.first().isAbstract.shouldBeTrue()
            }

            it("should parse datatype with features") {
                val (root, _) = parseKerML("""
                    package Types {
                        datatype Point {
                            feature x;
                            feature y;
                        }
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val dataType = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? DataType }
                    .first()

                dataType.declaredName shouldBe "Point"

                val features = dataType.ownedMembership
                    .mapNotNull { it.memberElement as? Feature }

                features shouldHaveSize 2
                features.map { it.declaredName } shouldBe listOf("x", "y")
            }
        }
    }

    describe("StructureVisitor") {

        context("parsing structure declarations") {

            it("should parse a simple structure") {
                val (root, _) = parseKerML("""
                    package Models {
                        struct Person;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val structures = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Structure }

                structures shouldHaveSize 1
                structures.first().declaredName shouldBe "Person"
            }

            it("should parse abstract structure") {
                val (root, _) = parseKerML("""
                    package Models {
                        abstract struct Entity;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val structures = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Structure }

                structures shouldHaveSize 1
                structures.first().isAbstract.shouldBeTrue()
            }

            it("should parse structure with features") {
                val (root, _) = parseKerML("""
                    package Models {
                        struct Address {
                            feature street;
                            feature city;
                            feature zipCode;
                        }
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val structure = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Structure }
                    .first()

                structure.declaredName shouldBe "Address"

                val features = structure.ownedMembership
                    .mapNotNull { it.memberElement as? Feature }

                features shouldHaveSize 3
                features.map { it.declaredName } shouldBe listOf("street", "city", "zipCode")
            }
        }
    }

    describe("AssociationVisitor") {

        context("parsing association declarations") {

            it("should parse a simple association") {
                val (root, _) = parseKerML("""
                    package Relationships {
                        assoc Ownership;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val associations = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Association }

                associations shouldHaveSize 1
                associations.first().declaredName shouldBe "Ownership"
            }

            it("should parse abstract association") {
                val (root, _) = parseKerML("""
                    package Relationships {
                        abstract assoc Link;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val associations = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Association }

                associations shouldHaveSize 1
                associations.first().isAbstract.shouldBeTrue()
            }

            it("should parse association with end features") {
                val (root, _) = parseKerML("""
                    package Relationships {
                        assoc Employment {
                            end feature employer;
                            end feature employee;
                        }
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val association = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? Association }
                    .first()

                association.declaredName shouldBe "Employment"

                val features = association.ownedMembership
                    .mapNotNull { it.memberElement as? Feature }

                features shouldHaveSize 2
                features.all { it.isEnd }.shouldBeTrue()
                features.map { it.declaredName } shouldBe listOf("employer", "employee")
            }
        }
    }

    describe("AssociationStructureVisitor") {

        context("parsing association structure declarations") {

            it("should parse a simple association structure") {
                val (root, _) = parseKerML("""
                    package Relationships {
                        assoc struct Marriage;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val assocStructs = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? AssociationStructure }

                assocStructs shouldHaveSize 1
                assocStructs.first().declaredName shouldBe "Marriage"
            }

            it("should parse abstract association structure") {
                val (root, _) = parseKerML("""
                    package Relationships {
                        abstract assoc struct Connection;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val assocStructs = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? AssociationStructure }

                assocStructs shouldHaveSize 1
                assocStructs.first().isAbstract.shouldBeTrue()
            }
        }
    }

    describe("ConnectorVisitor") {

        context("parsing connector declarations") {

            it("should parse a simple connector in a class") {
                val (root, _) = parseKerML("""
                    package System {
                        class Component {
                            connector link;
                        }
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val cls = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val connectors = cls.ownedMembership
                    .mapNotNull { it.memberElement as? Connector }

                connectors shouldHaveSize 1
                connectors.first().declaredName shouldBe "link"
            }

            it("should parse connector with modifiers") {
                val (root, _) = parseKerML("""
                    package System {
                        class Component {
                            composite connector internalLink;
                        }
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val cls = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val connectors = cls.ownedMembership
                    .mapNotNull { it.memberElement as? Connector }

                connectors shouldHaveSize 1
                connectors.first().isComposite.shouldBeTrue()
            }
        }
    }

    describe("mixed classifiers") {

        it("should parse package with multiple classifier types") {
            val (root, _) = parseKerML("""
                package Model {
                    class Vehicle;
                    datatype Speed;
                    struct Engine;
                    assoc Ownership;
                }
            """.trimIndent())

            val pkg = root.ownedMembership
                .mapNotNull { it.memberElement as? KerMLPackage }
                .first()

            pkg.ownedMembership.shouldNotBeNull()

            // Note: Structure extends Class in the KerML metamodel
            // Filter for Class but exclude Structure (which is a subtype)
            val classes = pkg.ownedMembership
                .mapNotNull { it.memberElement as? KerMLClass }
                .filter { it !is Structure }
            val dataTypes = pkg.ownedMembership.mapNotNull { it.memberElement as? DataType }
            val structures = pkg.ownedMembership.mapNotNull { it.memberElement as? Structure }
            val associations = pkg.ownedMembership.mapNotNull { it.memberElement as? Association }

            classes shouldHaveSize 1
            dataTypes shouldHaveSize 1
            structures shouldHaveSize 1
            associations shouldHaveSize 1

            classes.first().declaredName shouldBe "Vehicle"
            dataTypes.first().declaredName shouldBe "Speed"
            structures.first().declaredName shouldBe "Engine"
            associations.first().declaredName shouldBe "Ownership"
        }
    }
})
