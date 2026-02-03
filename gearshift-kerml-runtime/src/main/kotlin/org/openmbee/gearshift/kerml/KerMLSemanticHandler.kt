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
package org.openmbee.gearshift.kerml

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.SemanticBinding
import org.openmbee.mdm.framework.runtime.LifecycleEvent
import org.openmbee.mdm.framework.runtime.LifecycleHandler
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.mdm.framework.meta.MetaClass

private val logger = KotlinLogging.logger {}

/**
 * Handles KerML-specific semantic rules as defined in the KerML specification.
 *
 * This handler uses semantic bindings defined in the metamodel to create implied relationships:
 * - SPECIALIZES bindings create implied Subclassification relationships
 * - SUBSETS bindings create implied Subsetting relationships
 *
 * The bindings are evaluated against conditions to determine when they apply.
 * All KerML domain knowledge is encapsulated in the metamodel, keeping this handler generic.
 *
 * @see <a href="https://www.omg.org/spec/KerML">KerML Specification</a>
 */
class KerMLSemanticHandler(
    private val engine: MDMEngine
) : LifecycleHandler {

    override val priority: Int = 50 // Run early to establish implied relationships

    // Cache for resolved base library elements by qualified name
    private val libraryCache = mutableMapOf<String, MDMObject?>()

    // Elements pending implied relationship creation (deferred until library is loaded)
    private val pendingElements = mutableListOf<MDMObject>()

    // Track when Base library elements are available
    private var baseLibraryLoaded = false

    override fun handle(event: LifecycleEvent, model: MDMEngine) {
        when (event) {
            is LifecycleEvent.InstanceCreated -> {
                handleInstanceCreated(event.instance)
            }

            is LifecycleEvent.LinkCreated -> {
                // When explicit relationships are created, may need redundancy check
                // TODO: Implement redundancy elimination
            }

            else -> {
                // No-op for other events
            }
        }
    }

    private fun handleInstanceCreated(instance: MDMObject) {
        // Get all semantic bindings from the metaclass hierarchy
        val allBindings = collectAllSemanticBindings(instance.metaClass)

        // Check if this metaclass (or its parents) has semantic bindings
        if (allBindings.isEmpty()) {
            return
        }

        // Add to pending - we'll process when base library is available
        pendingElements.add(instance)
    }

    /**
     * Collect all semantic bindings from a metaclass and all its superclasses.
     */
    private fun collectAllSemanticBindings(metaClass: MetaClass): List<SemanticBinding> {
        val bindings = mutableListOf<SemanticBinding>()
        val visited = mutableSetOf<String>()
        val toProcess = ArrayDeque<MetaClass>()

        toProcess.add(metaClass)

        while (toProcess.isNotEmpty()) {
            val current = toProcess.removeFirst()
            if (current.name in visited) continue
            visited.add(current.name)

            // Add this class's bindings
            bindings.addAll(current.semanticBindings)

            // Queue superclasses for processing
            for (superclassName in current.superclasses) {
                engine.schema.getClass(superclassName)?.let { superclass ->
                    if (superclass.name !in visited) {
                        toProcess.add(superclass)
                    }
                }
            }
        }

        return bindings
    }

    private fun processPendingElements() {
        val elements = pendingElements.toList()
        pendingElements.clear()

        for (element in elements) {
            val metaClass = element.metaClass
            val allBindings = collectAllSemanticBindings(metaClass)
            if (allBindings.isNotEmpty()) {
                processSemanticBindings(element, allBindings)
            }
        }
    }

    private fun processSemanticBindings(instance: MDMObject, bindings: List<SemanticBinding>) {
        for (binding in bindings) {
            // Check if binding condition applies
            val conditionResult = evaluateCondition(binding.condition, instance)
            if (!conditionResult) {
                continue
            }

            // Resolve the base concept
            val baseConcept = resolveBaseConcept(binding.baseConcept)
            if (baseConcept == null) {
                continue
            }

            // Don't create self-referential relationships
            if (instance.id == baseConcept.id) {
                continue
            }

            // Create the implied relationship based on binding kind
            when (binding.bindingKind) {
                BindingKind.SPECIALIZES -> {
                    // For SPECIALIZES: use Subclassification for Classifiers, Specialization for other Types
                    val instanceIsClassifier = instance.className == "Classifier" ||
                            engine.schema.getClass(instance.className)?.let { metaClass ->
                                metaClass.superclasses.any {
                                    it == "Classifier" ||
                                            engine.schema.getClass(it)?.superclasses?.contains("Classifier") == true
                                }
                            } == true

                    if (instanceIsClassifier) {
                        createImpliedSubclassificationIfNeeded(instance as Classifier, baseConcept as Classifier)
                    }
                    // For non-Classifier Types (like Feature), skip - they get implied relationships via SUBSETS
                }

                BindingKind.SUBSETS -> {
                    createImpliedSubsettingIfNeeded(instance as Feature, baseConcept as Feature)
                }
            }
        }
    }

    private fun evaluateCondition(condition: BindingCondition, instance: MDMObject): Boolean {
        return when (condition) {
            is BindingCondition.Default -> true

            is BindingCondition.TypedBy -> {
                // Check if instance is typed by the specified metaclass
                if (instance is Feature) {
                    instance.type.any { (it as MDMObject).className == condition.metaclass }
                } else false
            }

            is BindingCondition.OwningTypeIs -> {
                // Check if owning type is the specified metaclass
                if (instance is Feature) {
                    val owningType = instance.owningType as? MDMObject
                    owningType?.className == condition.metaclass
                } else false
            }

            is BindingCondition.PropertyEquals -> {
                instance.getProperty(condition.property) == condition.value
            }

            is BindingCondition.And -> {
                condition.conditions.all { evaluateCondition(it, instance) }
            }

            is BindingCondition.Or -> {
                condition.conditions.any { evaluateCondition(it, instance) }
            }

            is BindingCondition.Not -> {
                !evaluateCondition(condition.condition, instance)
            }

            is BindingCondition.CollectionNotEmpty -> {
                val collection = instance.getProperty(condition.property)
                when (collection) {
                    is Collection<*> -> collection.isNotEmpty()
                    else -> false
                }
            }

            is BindingCondition.CollectionSizeEquals -> {
                val collection = instance.getProperty(condition.property)
                when (collection) {
                    is Collection<*> -> collection.size == condition.size
                    else -> false
                }
            }

            is BindingCondition.CollectionSizeNotEquals -> {
                val collection = instance.getProperty(condition.property)
                when (collection) {
                    is Collection<*> -> collection.size != condition.size
                    else -> true
                }
            }

            is BindingCondition.HasElementOfType -> {
                val collection = instance.getProperty(condition.property)
                when (collection) {
                    is Collection<*> -> collection.any {
                        (it as? MDMObject)?.className == condition.metaclass
                    }

                    else -> false
                }
            }

            is BindingCondition.IsEnd -> {
                instance.getProperty("isEnd") == true
            }

            is BindingCondition.IsPortion -> {
                instance.getProperty("isPortion") == true
            }

            is BindingCondition.IsComposite -> {
                instance.getProperty("isComposite") == true
            }

            is BindingCondition.OwningTypeTypedBy -> {
                if (instance is Feature) {
                    val owningType = instance.owningType
                    if (owningType is Feature) {
                        owningType.type.any { (it as MDMObject).className == condition.metaclass }
                    } else {
                        (owningType as? MDMObject)?.className == condition.metaclass
                    }
                } else false
            }
        }
    }

    /**
     * Check if a classifier already has explicit specializations.
     * This implements redundancy elimination per KerML spec:
     * - If the classifier has ANY explicit specialization, the implied relationship is not needed
     * - The explicit path will eventually lead to Base::Anything through the parent's implied relationship
     */
    private fun alreadySpecializesTransitively(classifier: Classifier, general: Classifier): Boolean {
        // If classifier has any explicit specializations, skip the implied one
        // The explicit specialization's target will get its own implied relationship if needed
        val explicitSpecs = classifier.ownedSpecialization.filter { it.isImplied != true }
        return explicitSpecs.isNotEmpty()
    }

    private fun resolveBaseConcept(qualifiedName: String): MDMObject? {
        // Check cache first
        libraryCache[qualifiedName]?.let { return it }

        // Use proper qualified name resolution via Namespace.resolveGlobal operation
        // Get any root namespace to call resolveGlobal on (it walks up to root internally)
        val rootNamespaces = engine.getRootNamespaces()
        if (rootNamespaces.isEmpty()) return null

        // Call the resolveGlobal operation - returns a Membership
        val membership = engine.invokeOperation(
            rootNamespaces.first().id!!,
            "resolveGlobal",
            mapOf("qualifiedName" to qualifiedName)
        ) as? MDMObject

        // Get the memberElement from the membership
        val found = membership?.let {
            engine.getProperty(it.id!!, "memberElement") as? MDMObject
        }

        // Cache for future lookups
        if (found != null) {
            libraryCache[qualifiedName] = found
        }

        return found
    }

    /**
     * Create implied Subclassification if the classifier doesn't already have explicit specializations.
     * Per KerML redundancy elimination rules, no implied relationship is created if there's already
     * a path to the general type through explicit (non-implied) specializations.
     */
    private fun createImpliedSubclassificationIfNeeded(classifier: Classifier, general: Classifier) {
        // Check if classifier already transitively specializes this general type through explicit specializations
        if (alreadySpecializesTransitively(classifier, general)) {
            logger.debug { "Classifier ${classifier.declaredName} already transitively specializes ${general.declaredName}, skipping implied" }
            return
        }

        // Create implied Subclassification
        logger.debug { "Creating implied Subclassification: ${classifier.declaredName} -> ${general.declaredName}" }

        val subclassification = engine.createElement("Subclassification") as Subclassification

        // Set isImplied = true
        engine.setProperty(subclassification.id!!, "isImplied", true)

        // Link for owningRelatedElement via the base Element -> Relationship association
        // This enables navigation from Element.ownedRelationship
        engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningRelatedElementOwnedRelationshipAssociation"
        )


        // Link subclassification to the specific classifier (owning classifier)
        // Uses owningClassifierOwnedSubclassificationAssociation: Classifier -> ownedSubclassification
        engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningClassifierOwnedSubclassificationAssociation"
        )

        // Also link via base association for Specialization.owningType
        engine.link(
            sourceId = classifier.id!!,
            targetId = subclassification.id!!,
            associationName = "owningTypeOwnedSpecializationAssociation"
        )

        // Link to the general classifier (superclassifier)
        // Uses superclassificationSuperclassifierAssociation: Subclassification -> superclassifier
        engine.link(
            sourceId = subclassification.id!!,
            targetId = general.id!!,
            associationName = "superclassificationSuperclassifierAssociation"
        )

        // Also link via base association for Specialization.general
        engine.link(
            sourceId = subclassification.id!!,
            targetId = general.id!!,
            associationName = "generalizationGeneralAssociation"
        )

        // Link to the specific classifier (subclassifier)
        // Uses subclassificationSubclassifierAssociation: Subclassification -> subclassifier
        engine.link(
            sourceId = subclassification.id!!,
            targetId = classifier.id!!,
            associationName = "subclassificationSubclassifierAssociation"
        )

        // Also link via base association for Specialization.specific
        engine.link(
            sourceId = subclassification.id!!,
            targetId = classifier.id!!,
            associationName = "specializationSpecificAssociation"
        )
    }

    /**
     * Check if a feature already has explicit subsettings.
     * This implements redundancy elimination per KerML spec:
     * - If the feature has ANY explicit subsetting, the implied relationship is not needed
     * - The explicit subsetting's target will get its own implied relationship if needed
     */
    private fun alreadySubsetsTransitively(feature: Feature, target: Feature): Boolean {
        // If feature has any explicit subsettings, skip the implied one
        // The explicit subsetting's target will get its own implied relationship if needed
        val explicitSubs = feature.ownedSubsetting.filter { it.isImplied != true }
        return explicitSubs.isNotEmpty()
    }

    /**
     * Create implied Subsetting if the feature doesn't already have explicit subsettings.
     * Per KerML redundancy elimination rules, no implied relationship is created if there's already
     * a path to the subsetted feature through explicit (non-implied) subsettings.
     */
    private fun createImpliedSubsettingIfNeeded(feature: Feature, subsettedFeature: Feature) {
        // Check if feature already transitively subsets this target through explicit subsettings
        if (alreadySubsetsTransitively(feature, subsettedFeature)) {
            logger.debug { "Feature ${feature.declaredName} already transitively subsets ${subsettedFeature.declaredName}, skipping implied" }
            return
        }

        // Create implied Subsetting
        logger.debug { "Creating implied Subsetting: ${feature.declaredName} -> ${subsettedFeature.declaredName}" }

        val subsetting = engine.createElement("Subsetting") as Subsetting

        // Set isImplied = true
        engine.setProperty(subsetting.id!!, "isImplied", true)

        // Link for owningRelatedElement via the base Element -> Relationship association
        // This enables navigation from Feature.ownedRelationship
        engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningRelatedElementOwnedRelationshipAssociation"
        )

        // Link subsetting to the owning feature
        // Uses owningFeatureOwnedSubsettingAssociation: Feature -> ownedSubsetting
        engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningFeatureOwnedSubsettingAssociation"
        )

        // Also link via base association for Specialization.owningType
        engine.link(
            sourceId = feature.id!!,
            targetId = subsetting.id!!,
            associationName = "owningTypeOwnedSpecializationAssociation"
        )

        // Link to the subsetted feature (general)
        // Uses supersettingSubsettedFeatureAssociation: Subsetting -> subsettedFeature
        engine.link(
            sourceId = subsetting.id!!,
            targetId = subsettedFeature.id!!,
            associationName = "supersettingSubsettedFeatureAssociation"
        )

        // Also link via base association for Specialization.general
        engine.link(
            sourceId = subsetting.id!!,
            targetId = subsettedFeature.id!!,
            associationName = "generalizationGeneralAssociation"
        )

        // Link to the subsetting feature (specific)
        // Uses subsettingSubsettingFeatureAssociation: Subsetting -> subsettingFeature
        engine.link(
            sourceId = subsetting.id!!,
            targetId = feature.id!!,
            associationName = "subsettingSubsettingFeatureAssociation"
        )

        // Also link via base association for Specialization.specific
        engine.link(
            sourceId = subsetting.id!!,
            targetId = feature.id!!,
            associationName = "specializationSpecificAssociation"
        )
    }

    /**
     * Manually trigger processing of any pending implied relationships.
     * Call this after loading the Base library if needed.
     *
     * This will search for Base::Anything and Base::things in the model
     * and process all pending elements.
     */
    fun processAllPending() {
        val allElements = engine.getAllElements()

        // Search for library elements if not already cached
        if (libraryCache["Base::Anything"] == null) {
            val anything = allElements.find { elem ->
                val name = elem.getProperty("declaredName") as? String
                name == "Anything"
            }
            if (anything != null) {
                libraryCache["Base::Anything"] = anything
                logger.debug { "Found Base::Anything: ${anything.id}" }
            }
        }

        if (libraryCache["Base::things"] == null) {
            val things = allElements.find { elem ->
                val name = elem.getProperty("declaredName") as? String
                name == "things"
            }
            if (things != null) {
                libraryCache["Base::things"] = things
                logger.debug { "Found Base::things: ${things.id}" }
            }
        }

        baseLibraryLoaded = true
        logger.debug { "Processing ${pendingElements.size} pending elements" }
        processPendingElements()
    }

    /**
     * Clear the library cache (useful for testing).
     */
    fun clearCache() {
        libraryCache.clear()
        pendingElements.clear()
        baseLibraryLoaded = false
    }
}
