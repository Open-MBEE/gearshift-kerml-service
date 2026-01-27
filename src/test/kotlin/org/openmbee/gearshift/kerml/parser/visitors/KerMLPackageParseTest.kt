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
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Tests for parsing KerML packages using the new typed visitor infrastructure.
 */
class KerMLPackageParseTest : DescribeSpec({

    /**
     * Helper to parse KerML source and return the root namespace.
     */
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

    describe("PackageVisitor") {

        context("parsing empty packages") {

            it("should parse an empty package with semicolon") {
                val (root, engine) = parseKerML("package MyPackage;")

                root.shouldNotBeNull()
                root.ownedMembership.shouldNotBeEmpty()

                // Find the package in the root's members
                val packages = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                packages shouldHaveSize 1
                packages.first().declaredName shouldBe "MyPackage"
            }

            it("should parse an empty package with braces") {
                val (root, _) = parseKerML("package EmptyBraces {}")

                val packages = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                packages shouldHaveSize 1
                packages.first().declaredName shouldBe "EmptyBraces"
            }

            it("should parse package with short name") {
                val (root, _) = parseKerML("package <P> MyPackage;")

                val packages = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                packages shouldHaveSize 1
                val pkg = packages.first()
                pkg.declaredName shouldBe "MyPackage"
                pkg.declaredShortName shouldBe "P"
            }
        }

        context("parsing nested packages") {

            it("should parse nested packages") {
                val (root, _) = parseKerML("""
                    package Outer {
                        package Inner;
                    }
                """.trimIndent())

                // Get outer package
                val outerPackages = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                outerPackages shouldHaveSize 1
                val outer = outerPackages.first()
                outer.declaredName shouldBe "Outer"

                // Get inner package from outer's members
                val innerPackages = outer.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                innerPackages shouldHaveSize 1
                innerPackages.first().declaredName shouldBe "Inner"
            }

            it("should parse deeply nested packages") {
                val (root, _) = parseKerML("""
                    package Level1 {
                        package Level2 {
                            package Level3;
                        }
                    }
                """.trimIndent())

                val level1 = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                level1.declaredName shouldBe "Level1"

                val level2 = level1.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                level2.declaredName shouldBe "Level2"

                val level3 = level2.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                level3.declaredName shouldBe "Level3"
            }

            it("should parse multiple sibling packages") {
                val (root, _) = parseKerML("""
                    package A;
                    package B;
                    package C;
                """.trimIndent())

                val packages = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }

                packages shouldHaveSize 3
                packages.map { it.declaredName } shouldBe listOf("A", "B", "C")
            }
        }

        context("parsing packages with classes") {

            it("should parse package containing a class") {
                val (root, _) = parseKerML("""
                    package Vehicles {
                        class Car;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                pkg.declaredName shouldBe "Vehicles"

                val classes = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }

                classes shouldHaveSize 1
                classes.first().declaredName shouldBe "Car"
            }

            it("should parse package with multiple classes") {
                val (root, _) = parseKerML("""
                    package Shapes {
                        class Circle;
                        class Square;
                        class Triangle;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val classes = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }

                classes shouldHaveSize 3
                classes.map { it.declaredName } shouldBe listOf("Circle", "Square", "Triangle")
            }

            it("should parse abstract class") {
                val (root, _) = parseKerML("""
                    package Test {
                        abstract class Shape;
                    }
                """.trimIndent())

                val pkg = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                val classes = pkg.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }

                classes shouldHaveSize 1
                classes.first().declaredName shouldBe "Shape"
                classes.first().isAbstract shouldBe true
            }
        }

        context("membership navigation") {

            it("should be able to navigate from membership to owning namespace") {
                val (root, _) = parseKerML("""
                    package Parent {
                        package Child;
                    }
                """.trimIndent())

                val parent = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLPackage }
                    .first()

                // Get the membership that owns Child
                val childMembership = parent.ownedMembership.first()

                // Navigate back to the owning namespace
                childMembership.membershipOwningNamespace shouldBe parent
            }

            it("should set memberName on membership from element declaredName") {
                val (root, _) = parseKerML("package TestPkg;")

                val membership = root.ownedMembership.first()

                membership.memberName shouldBe "TestPkg"
            }
        }
    }
})
