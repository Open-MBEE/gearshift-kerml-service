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
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Tests for parsing KerML features using the new typed visitor infrastructure.
 */
class KerMLFeatureParseTest : DescribeSpec({

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

    /**
     * Helper to get features from a class.
     */
    fun getFeatures(cls: KerMLClass): List<Feature> {
        return cls.ownedMembership
            .mapNotNull { it.memberElement as? Feature }
    }

    describe("FeatureVisitor") {

        context("parsing simple features") {

            it("should parse a feature with just a name") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature speed;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().declaredName shouldBe "speed"
            }

            it("should parse a feature with short name") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature <s> speed;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                val feature = features.first()
                feature.declaredName shouldBe "speed"
                feature.declaredShortName shouldBe "s"
            }

            it("should parse multiple features") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature speed;
                        feature weight;
                        feature color;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 3
                features.map { it.declaredName } shouldBe listOf("speed", "weight", "color")
            }
        }

        context("parsing feature modifiers") {

            it("should parse derived feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        derived feature totalCost;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isDerived.shouldBeTrue()
            }

            it("should parse abstract feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        abstract feature propulsion;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isAbstract.shouldBeTrue()
            }

            it("should parse composite feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        composite feature engine;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isComposite.shouldBeTrue()
            }

            it("should parse portion feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        portion feature frontSection;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isPortion.shouldBeTrue()
            }

            it("should parse variable feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        var feature mileage;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isVariable.shouldBeTrue()
            }

            it("should parse const feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        const feature vin;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                val feature = features.first()
                feature.isConstant.shouldBeTrue()
                feature.isVariable.shouldBeTrue() // const implies variable
            }

            it("should parse end feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        end feature endpoint;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isEnd.shouldBeTrue()
            }

            it("should parse feature with direction in") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        in feature fuel;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().direction shouldBe "in"
            }

            it("should parse feature with direction out") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        out feature exhaust;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().direction shouldBe "out"
            }

            it("should parse feature with direction inout") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        inout feature data;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().direction shouldBe "inout"
            }

            it("should parse feature with multiple modifiers") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        in derived composite feature engine;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                val feature = features.first()
                feature.direction shouldBe "in"
                feature.isDerived.shouldBeTrue()
                feature.isComposite.shouldBeTrue()
            }
        }

        context("parsing feature specializations") {

            it("should parse feature with ordered modifier") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature wheels ordered;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isOrdered.shouldBeTrue()
            }

            it("should parse feature with nonunique modifier") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature passengers nonunique;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                features.first().isNonunique.shouldBeTrue()
            }

            it("should parse feature with ordered nonunique") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature items ordered nonunique;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                val feature = features.first()
                feature.isOrdered.shouldBeTrue()
                feature.isNonunique.shouldBeTrue()
            }
        }

        context("parsing features without explicit feature keyword") {

            it("should parse feature using basic prefix without keyword") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        composite engine;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val features = getFeatures(cls)
                features shouldHaveSize 1
                val feature = features.first()
                feature.declaredName shouldBe "engine"
                feature.isComposite.shouldBeTrue()
            }
        }

        context("parsing feature membership") {

            it("should create proper membership for feature") {
                val (root, _) = parseKerML("""
                    class Vehicle {
                        feature speed;
                    }
                """.trimIndent())

                val cls = root.ownedMembership
                    .mapNotNull { it.memberElement as? KerMLClass }
                    .first()

                val memberships = cls.ownedMembership
                    .filter { it.memberElement is Feature }

                memberships shouldHaveSize 1
                memberships.first().memberName shouldBe "speed"
            }
        }
    }
})
