/*
 * Copyright 2026 Charles Galey
 */
package org.openmbee.gearshift.kerml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.openmbee.gearshift.engine.LifecycleEvent
import org.openmbee.gearshift.engine.LifecycleHandler
import org.openmbee.gearshift.engine.MDMEngine
import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.generated.interfaces.Subsetting

class LifecycleHandlerDebugTest : DescribeSpec({

    describe("Lifecycle handler debugging") {

        it("should call handler when instances are created via KerMLModelFactory") {
            // Create a simple tracking handler
            var instancesCreated = mutableListOf<String>()

            val trackingHandler = object : LifecycleHandler {
                override val priority: Int = 10
                override fun handle(event: LifecycleEvent, engine: MDMEngine) {
                    if (event is LifecycleEvent.InstanceCreated) {
                        instancesCreated.add(event.instance.className)
                    }
                }
            }

            val factory = KerMLModelFactory()

            // Add our tracking handler AFTER factory is created
            // (the semantic handler is added in the factory's init)
            factory.engine.addLifecycleHandler(trackingHandler)

            // Parse some KerML
            factory.parseString("""
                package Test {
                    class MyClass;
                }
            """.trimIndent())

            // Check what instances were created
            println("Instances created: $instancesCreated")

            // We should have at least Package, Class, and some memberships
            instancesCreated.size shouldBeGreaterThan 0
            instancesCreated.contains("Class") shouldBe true
        }

        it("should have handler count greater than zero in MDMEngine") {
            val factory = KerMLModelFactory()

            // Check handler count via mdmEngine (through GearshiftEngine)
            // The semantic handler should already be registered

            // We need a way to check handler count - let's create an instance
            // and see if the events are firing

            var eventFired = false
            val testHandler = object : LifecycleHandler {
                override val priority: Int = 10
                override fun handle(event: LifecycleEvent, engine: MDMEngine) {
                    eventFired = true
                }
            }
            factory.engine.addLifecycleHandler(testHandler)

            // Create an instance manually
            factory.engine.createInstance("Package")

            eventFired shouldBe true
        }

        it("should have SemanticBinding on Classifier metaclass") {
            val factory = KerMLModelFactory()

            // Get the Classifier metaclass
            val classifierMetaClass = factory.engine.getMetaClass("Classifier")!!
            println("Classifier semanticBindings: ${classifierMetaClass.semanticBindings.map { it.name to it.baseConcept }}")

            // Check that Classifier has a SemanticBinding to Base::Anything
            val hasAnythingBinding = classifierMetaClass.semanticBindings.any {
                it.baseConcept == "Base::Anything"
            }
            hasAnythingBinding shouldBe true
        }

        it("should find Base::Anything after loading library") {
            val factory = KerMLModelFactory()

            // Load the Base library
            KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            // Try to find Base::Anything
            val anything = factory.findByQualifiedName("Base::Anything")

            anything shouldNotBe null
            anything?.id shouldNotBe null
        }

        it("should create implied subclassification when library is loaded before parsing") {
            val factory = KerMLModelFactory()

            // Load the Base library FIRST
            KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            // Parse a class
            factory.parseString("""
                package Test {
                    class MyClass;
                }
            """.trimIndent())

            // Debug: Check if Base::Anything was loaded
            val anything = factory.findByQualifiedName("Base::Anything")
            println("DEBUG: Base::Anything found: ${anything != null}, id=${anything?.id}")

            // Check all Subclassification instances
            val subclassifications = factory.allOfType<Subclassification>()
            println("DEBUG: Total subclassifications: ${subclassifications.size}")

            // Find implied subclassifications
            val impliedSubs = subclassifications.filter { it.isImplied == true }
            println("DEBUG: Implied subclassifications: ${impliedSubs.size}")
            for (sub in impliedSubs) {
                println("DEBUG:   Subclassification id=${sub.id}")
            }

            // We expect MyClass to have an implied subclassification to Base::Anything
            impliedSubs.shouldNotBeEmpty()
        }

        it("should resolve Base::things") {
            val factory = KerMLModelFactory()

            // Load the Base library
            KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            // Try to find 'things'
            val things = factory.findByQualifiedName("Base::things")
            things shouldNotBe null
            things?.id shouldNotBe null
        }

        it("should create implied subsetting for feature typed by DataType") {
            val factory = KerMLModelFactory()

            // Load the Base library
            KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            // Parse a structure with a feature typed by a DataType
            factory.parseString("""
                package Test {
                    datatype Temperature;
                    class Sensor {
                        feature currentTemp : Temperature;
                    }
                }
            """.trimIndent())

            // Find the currentTemp feature
            val currentTemp = factory.findByName<Feature>("currentTemp")
            currentTemp.shouldNotBeNull()

            // All features should have implied subsetting to Base::things
            val subsettings = factory.allOfType<Subsetting>()
            val currentTempSubsettings = subsettings.filter { sub ->
                val subsettingFeature = sub.subsettingFeature
                subsettingFeature.name == "currentTemp" || subsettingFeature.declaredName == "currentTemp"
            }

            // Should have at least implied subsetting to things
            val toThings = currentTempSubsettings.filter { sub ->
                sub.isImplied == true &&
                (sub.subsettedFeature.name == "things" || sub.subsettedFeature.declaredName == "things")
            }
            toThings.shouldNotBeEmpty()

            // For DataType-typed features, should also have implied subsetting to dataValues
            // Note: This requires the conditional binding to be evaluated when FeatureTyping is linked
            val toDataValues = currentTempSubsettings.filter { sub ->
                sub.isImplied == true &&
                (sub.subsettedFeature.name == "dataValues" || sub.subsettedFeature.declaredName == "dataValues")
            }
            // This tests the conditional TypedBy("DataType") binding
            toDataValues.shouldNotBeEmpty()
        }

        it("should create implied subsetting with correct links") {
            val factory = KerMLModelFactory()

            // Load the Base library
            KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            // Parse a class with a feature
            factory.parseString("""
                package Test {
                    class Vehicle {
                        feature wheels;
                    }
                }
            """.trimIndent())

            // Find wheels
            val wheels = factory.findByName<Feature>("wheels")
            wheels shouldNotBe null

            // Find all Subsetting instances
            val allSubsettings = factory.allOfType<Subsetting>()

            val debugInfo = StringBuilder()
            debugInfo.appendLine("All Subsettings: ${allSubsettings.size}")

            for (sub in allSubsettings) {
                val subObj = factory.engine.getInstance(sub.id!!)!!
                val isImplied = subObj.getProperty("isImplied") as? Boolean ?: false
                debugInfo.appendLine("  Subsetting id=${sub.id} isImplied=$isImplied className=${subObj.className}")

                // Check subsettingFeature links
                val subsettingFeatureLinks = factory.engine.mdmEngine.linkRepository
                    .getByAssociationAndSource("subsettingSubsettingFeatureAssociation", sub.id!!)
                debugInfo.appendLine("    subsettingFeature links: ${subsettingFeatureLinks.size}")
                for (link in subsettingFeatureLinks) {
                    val target = factory.engine.getInstance(link.targetId)
                    debugInfo.appendLine("      -> ${target?.getProperty("declaredName")} (${target?.className})")
                }

                // Check subsettedFeature links
                val subsettedFeatureLinks = factory.engine.mdmEngine.linkRepository
                    .getByAssociationAndSource("supersettingSubsettedFeatureAssociation", sub.id!!)
                debugInfo.appendLine("    subsettedFeature links: ${subsettedFeatureLinks.size}")
                for (link in subsettedFeatureLinks) {
                    val target = factory.engine.getInstance(link.targetId)
                    debugInfo.appendLine("      -> ${target?.getProperty("declaredName")} (${target?.className})")
                }
            }

            // Find implied subsettings
            val impliedSubsettings = allSubsettings.filter {
                val obj = factory.engine.getInstance(it.id!!)!!
                (obj.getProperty("isImplied") as? Boolean) == true
            }
            debugInfo.appendLine("Implied Subsettings: ${impliedSubsettings.size}")

            if (impliedSubsettings.isEmpty() || impliedSubsettings.any {
                    val obj = factory.engine.getInstance(it.id!!)!!
                    factory.engine.mdmEngine.linkRepository
                        .getByAssociationAndSource("supersettingSubsettedFeatureAssociation", it.id!!).isEmpty()
                }) {
                throw AssertionError("Debug info:\n$debugInfo")
            }
        }
    }
})
