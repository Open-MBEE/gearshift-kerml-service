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
package org.openmbee.gearshift.framework.query.gql

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.query.gql.query
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

/**
 * Tests for GQL query execution.
 */
class GqlExecutorTest : DescribeSpec({

    // Helper function to create a test metamodel
    fun createTestMetamodel(): MetamodelRegistry {
        val registry = MetamodelRegistry()

        // Base Element class
        val elementClass = MetaClass(
            name = "Element",
            isAbstract = true,
            attributes = listOf(
                MetaProperty(name = "name", type = "String", lowerBound = 0)
            )
        )
        registry.registerClass(elementClass)

        // Namespace extends Element
        val namespaceClass = MetaClass(
            name = "Namespace",
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(name = "name", type = "String", lowerBound = 0)
            )
        )
        registry.registerClass(namespaceClass)

        // Classifier extends Namespace
        val classifierClass = MetaClass(
            name = "Classifier",
            superclasses = listOf("Namespace"),
            attributes = listOf(
                MetaProperty(name = "isAbstract", type = "Boolean", lowerBound = 0)
            )
        )
        registry.registerClass(classifierClass)

        // Feature extends Element
        val featureClass = MetaClass(
            name = "Feature",
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(name = "isOrdered", type = "Boolean", lowerBound = 0)
            )
        )
        registry.registerClass(featureClass)

        // Association: Namespace owns members
        val ownedMemberAssoc = MetaAssociation(
            name = "NamespaceMembership",
            sourceEnd = MetaAssociationEnd(
                name = "owningNamespace",
                type = "Namespace",
                lowerBound = 0,
                upperBound = 1,
                aggregation = AggregationKind.NONE
            ),
            targetEnd = MetaAssociationEnd(
                name = "ownedMember",
                type = "Element",
                lowerBound = 0,
                upperBound = -1,
                aggregation = AggregationKind.COMPOSITE
            )
        )
        registry.registerAssociation(ownedMemberAssoc)

        // Association: Feature has type
        val featureTypeAssoc = MetaAssociation(
            name = "FeatureTyping",
            sourceEnd = MetaAssociationEnd(
                name = "typedFeature",
                type = "Feature",
                lowerBound = 1,
                upperBound = 1,
                aggregation = AggregationKind.NONE
            ),
            targetEnd = MetaAssociationEnd(
                name = "type",
                type = "Classifier",
                lowerBound = 0,
                upperBound = 1,
                aggregation = AggregationKind.NONE
            )
        )
        registry.registerAssociation(featureTypeAssoc)

        // Association: Classifier superclasses
        val superclassingAssoc = MetaAssociation(
            name = "Superclassing",
            sourceEnd = MetaAssociationEnd(
                name = "subclassifier",
                type = "Classifier",
                lowerBound = 1,
                upperBound = 1,
                aggregation = AggregationKind.NONE
            ),
            targetEnd = MetaAssociationEnd(
                name = "superclassifier",
                type = "Classifier",
                lowerBound = 0,
                upperBound = -1,
                aggregation = AggregationKind.NONE
            )
        )
        registry.registerAssociation(superclassingAssoc)

        return registry
    }

    // Helper function to create test engine with sample data
    fun createTestEngine(): MDMEngine {
        val registry = createTestMetamodel()
        val engine = MDMEngine(registry)

        // Create test data
        val (nsId, ns) = engine.createInstance("Namespace")
        engine.setPropertyValue(ns, "name", "Base")

        val (cls1Id, cls1) = engine.createInstance("Classifier")
        engine.setPropertyValue(cls1, "name", "Anything")
        engine.setPropertyValue(cls1, "isAbstract", true)

        val (cls2Id, cls2) = engine.createInstance("Classifier")
        engine.setPropertyValue(cls2, "name", "DataValue")
        engine.setPropertyValue(cls2, "isAbstract", false)

        val (feat1Id, feat1) = engine.createInstance("Feature")
        engine.setPropertyValue(feat1, "name", "things")
        engine.setPropertyValue(feat1, "isOrdered", false)

        val (feat2Id, feat2) = engine.createInstance("Feature")
        engine.setPropertyValue(feat2, "name", "items")
        engine.setPropertyValue(feat2, "isOrdered", true)

        // Create links
        engine.createLink("NamespaceMembership", nsId, cls1Id)
        engine.createLink("NamespaceMembership", nsId, cls2Id)
        engine.createLink("NamespaceMembership", nsId, feat1Id)
        engine.createLink("NamespaceMembership", nsId, feat2Id)

        engine.createLink("FeatureTyping", feat1Id, cls1Id)
        engine.createLink("FeatureTyping", feat2Id, cls2Id)

        engine.createLink("Superclassing", cls2Id, cls1Id)

        return engine
    }

    describe("GQL Query Execution") {

        context("simple node matching") {

            it("should match nodes by label") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (c:Classifier) RETURN c")

                results.isNotEmpty() shouldBe true
                results.size shouldBe 2
                results.columns shouldContain "c"
            }

            it("should match all nodes when no label specified") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (n) RETURN n")

                results.isNotEmpty() shouldBe true
                // Should have all 5 elements: 1 namespace, 2 classifiers, 2 features
                results.size shouldBe 5
            }

            it("should match nodes with property filter") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (f:Feature {name: 'things'}) RETURN f")

                results.size shouldBe 1
                val feature = results[0]["f"] as MDMObject
                engine.getProperty(feature, "name") shouldBe "things"
            }

            it("should match nodes by multiple labels (disjunction)") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (n:Classifier|Feature) RETURN n")

                results.size shouldBe 4 // 2 classifiers + 2 features
            }

            it("should return node properties") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (c:Classifier) RETURN c.name")

                results.size shouldBe 2
                results.columns shouldContain "c.name"

                val names = results.column("c.name")
                names shouldContain "Anything"
                names shouldContain "DataValue"
            }
        }

        context("edge traversal") {

            it("should traverse outgoing edges") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (n:Namespace)-[e:NamespaceMembership]->(m)
                    RETURN m.name
                """)

                results.size shouldBe 4 // 2 classifiers + 2 features
                val names = results.column("m.name")
                names shouldContain "Anything"
                names shouldContain "DataValue"
                names shouldContain "things"
                names shouldContain "items"
            }

            it("should filter edges by association type") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (f:Feature)-[:FeatureTyping]->(t:Classifier)
                    RETURN f.name, t.name
                """)

                results.size shouldBe 2
            }

            it("should support edge variable binding") {
                val engine = createTestEngine()

                // Note: ISO GQL doesn't have type(r) function like Cypher
                // Instead, we test that edge variables are bound correctly
                val results = engine.query("""
                    MATCH (f:Feature)-[r:FeatureTyping]->(t)
                    RETURN f.name, t.name
                """)

                results.size shouldBe 2
                results.columns shouldContain "f.name"
                results.columns shouldContain "t.name"
            }
        }

        context("path patterns") {

            it("should match multi-hop paths") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (n:Namespace)-[:NamespaceMembership]->(f:Feature)-[:FeatureTyping]->(t:Classifier)
                    RETURN n.name, f.name, t.name
                """)

                results.size shouldBe 2
                results.columns shouldHaveSize 3
            }
        }

        context("WHERE filtering") {

            it("should filter by property comparison") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.isAbstract = true
                    RETURN c.name
                """)

                results.size shouldBe 1
                results[0]["c.name"] shouldBe "Anything"
            }

            it("should filter by string comparison") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (f:Feature)
                    FILTER WHERE f.name = 'things'
                    RETURN f
                """)

                results.size shouldBe 1
            }

            it("should support AND conditions") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (f:Feature)
                    FILTER WHERE f.isOrdered = false AND f.name = 'things'
                    RETURN f.name
                """)

                results.size shouldBe 1
                results[0]["f.name"] shouldBe "things"
            }

            it("should support OR conditions") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.name = 'Anything' OR c.name = 'DataValue'
                    RETURN c.name
                """)

                results.size shouldBe 2
            }

            it("should support NOT operator") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE NOT c.isAbstract = true
                    RETURN c.name
                """)

                results.size shouldBe 1
                results[0]["c.name"] shouldBe "DataValue"
            }

            it("should support IS NULL check") {
                val engine = createTestEngine()
                // Create a classifier without a name
                val registry = createTestMetamodel()
                val engine2 = MDMEngine(registry)
                val (_, noName) = engine2.createInstance("Classifier")
                // Don't set the name property

                val results = engine2.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.name IS NULL
                    RETURN c
                """)

                results.size shouldBe 1
            }

            it("should support IS NOT NULL check") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.name IS NOT NULL
                    RETURN c.name
                """)

                results.size shouldBe 2
            }

            it("should support numeric comparisons") {
                val registry = MetamodelRegistry()
                val cls = MetaClass(
                    name = "Item",
                    attributes = listOf(
                        // Note: 'count' is a reserved keyword in ISO GQL, use 'quantity' instead
                        MetaProperty(name = "quantity", type = "Integer", lowerBound = 0)
                    )
                )
                registry.registerClass(cls)

                val engine = MDMEngine(registry)
                repeat(5) { i ->
                    val (_, item) = engine.createInstance("Item")
                    engine.setPropertyValue(item, "quantity", i.toLong())
                }

                val results = engine.query("""
                    MATCH (i:Item)
                    FILTER WHERE i.quantity > 2
                    RETURN i.quantity
                """)

                results.size shouldBe 2 // quantity = 3 and quantity = 4
            }
        }

        context("RETURN clause") {

            it("should support RETURN *") {
                val engine = createTestEngine()

                val results = engine.query("MATCH (c:Classifier) RETURN *")

                results.size shouldBe 2
                results.columns shouldContain "c"
            }

            it("should support RETURN DISTINCT") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (n:Namespace)-[:NamespaceMembership]->(m)
                    RETURN DISTINCT n.name
                """)

                results.size shouldBe 1 // Only one namespace
                results[0]["n.name"] shouldBe "Base"
            }

            it("should support aliases") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    RETURN c.name AS className
                """)

                results.columns shouldContain "className"
                val names = results.column("className")
                names shouldContain "Anything"
            }

            it("should support expressions in RETURN") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    RETURN c.name, c.isAbstract = true AS isAbstract
                """)

                results.columns shouldContain "isAbstract"
            }
        }

        context("aggregate functions") {

            it("should support COUNT(*)") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    RETURN COUNT(*)
                """)

                results.size shouldBe 2 // One per row since we don't have GROUP BY yet
            }
        }

        context("functions") {

            it("should support ELEMENT_ID() function") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier {name: 'Anything'})
                    RETURN element_id(c) AS id
                """)

                results.size shouldBe 1
                results[0]["id"] shouldNotBe null
            }

            // Note: TYPE() and LABELS() are Cypher functions, not ISO GQL
            // ISO GQL uses different mechanisms for type/label access

            it("should support string functions") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.name = 'Anything'
                    RETURN upper(c.name) AS upperName, lower(c.name) AS lowerName
                """)

                results.size shouldBe 1
                results[0]["upperName"] shouldBe "ANYTHING"
                results[0]["lowerName"] shouldBe "anything"
            }

            it("should support CHAR_LENGTH() function on strings") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier {name: 'Anything'})
                    RETURN char_length(c.name) AS nameLength
                """)

                results.size shouldBe 1
                results[0]["nameLength"] shouldBe 8L
            }

            it("should support COALESCE() function") {
                val registry = createTestMetamodel()
                val engine = MDMEngine(registry)

                val (_, cls) = engine.createInstance("Classifier")
                // Don't set name

                val results = engine.query("""
                    MATCH (c:Classifier)
                    RETURN coalesce(c.name, 'Unknown') AS displayName
                """)

                results.size shouldBe 1
                results[0]["displayName"] shouldBe "Unknown"
            }
        }

        context("OPTIONAL MATCH") {

            it("should return null for non-matching optional patterns") {
                val engine = createTestEngine()

                // Note: Classifier extends Namespace, so MATCH (n:Namespace) returns 3 elements.
                // OPTIONAL MATCH returns null for non-matching patterns instead of filtering out.
                // Using property filter inline to get only 'Base' namespace.
                val results = engine.query("""
                    MATCH (n:Namespace {name: 'Base'})
                    OPTIONAL MATCH (n)-[:NonExistentAssoc]->(x)
                    RETURN n.name, x
                """)

                results.size shouldBe 1
                results[0]["n.name"] shouldBe "Base"
                results[0]["x"] shouldBe null
            }
        }

        context("empty results") {

            it("should return empty table when no matches") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (n:NonExistentClass)
                    RETURN n
                """)

                results.isEmpty() shouldBe true
                results.size shouldBe 0
            }

            it("should return empty table when WHERE filters all results") {
                val engine = createTestEngine()

                val results = engine.query("""
                    MATCH (c:Classifier)
                    FILTER WHERE c.name = 'DoesNotExist'
                    RETURN c
                """)

                results.isEmpty() shouldBe true
            }
        }
    }

    describe("GQL Parser") {

        it("should parse simple MATCH-RETURN query") {
            val engine = createTestEngine()

            // This tests that parsing succeeds
            val results = engine.query("MATCH (n) RETURN n")
            results shouldNotBe null
        }

        it("should parse query with FILTER WHERE clause") {
            val engine = createTestEngine()

            val results = engine.query("""
                MATCH (n:Classifier)
                FILTER WHERE n.name = 'Test'
                RETURN n
            """)
            results shouldNotBe null
        }

        it("should parse query with multiple MATCH clauses") {
            val engine = createTestEngine()

            val results = engine.query("""
                MATCH (a:Classifier)
                MATCH (b:Feature)
                RETURN a.name, b.name
            """)
            results shouldNotBe null
        }
    }

    describe("BindingTable") {

        it("should support scalar() for single-value results") {
            val engine = createTestEngine()

            val results = engine.query("""
                MATCH (c:Classifier {name: 'Anything'})
                RETURN c.name
            """)

            results.scalar() shouldBe "Anything"
        }

        it("should support firstOrNull()") {
            val engine = createTestEngine()

            val results = engine.query("MATCH (c:Classifier) RETURN c.name")
            val first = results.firstOrNull()

            first shouldNotBe null
            first!!.containsKey("c.name") shouldBe true
        }

        it("should support iteration") {
            val engine = createTestEngine()

            val results = engine.query("MATCH (c:Classifier) RETURN c.name")
            var count = 0
            for (row in results) {
                count++
                row.containsKey("c.name") shouldBe true
            }
            count shouldBe 2
        }

        it("should support column extraction") {
            val engine = createTestEngine()

            val results = engine.query("MATCH (c:Classifier) RETURN c.name")
            val names = results.column("c.name")

            names shouldHaveSize 2
            names shouldContain "Anything"
            names shouldContain "DataValue"
        }
    }
})
