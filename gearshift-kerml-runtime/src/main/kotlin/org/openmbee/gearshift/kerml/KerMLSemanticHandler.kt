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
import org.openmbee.gearshift.generated.interfaces.Redefinition
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.gearshift.generated.interfaces.TypeFeaturing
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
            val instanceName = instance.getProperty("declaredName") as? String ?: instance.id
            logger.debug { "Binding ${binding.name} for $instanceName: condition=${binding.condition}, result=$conditionResult" }
            if (!conditionResult) {
                continue
            }

            // Resolve the base concept (may be dynamic for TYPE_FEATURES/REDEFINES)
            val baseConcept = resolveBindingTarget(binding, instance)
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

                BindingKind.REDEFINES -> {
                    createImpliedRedefinitionIfNeeded(instance as Feature, baseConcept as Feature)
                }

                BindingKind.TYPE_FEATURES -> {
                    createImpliedTypeFeaturingIfNeeded(instance as Feature, baseConcept as Type)
                }
            }
        }
    }

    /**
     * Resolve the target of a semantic binding.
     * For most bindings, this is a fixed library element (baseConcept qualified name).
     * For TYPE_FEATURES and REDEFINES, the target may be dynamic.
     */
    private fun resolveBindingTarget(binding: SemanticBinding, instance: MDMObject): MDMObject? {
        return when {
            // Dynamic target: use owningType for TypeFeaturing
            binding.baseConcept == "self::owningType" && instance is Feature -> {
                instance.owningType as? MDMObject
            }

            // Dynamic target: find corresponding end feature in supertypes
            binding.baseConcept == "self::supertypeEndFeature" && instance is Feature -> {
                resolveSupertypeEndFeature(instance)
            }

            // Dynamic target: use defaultFeaturingType for Connector TypeFeaturing
            binding.baseConcept == "self::defaultFeaturingType" -> {
                // Get defaultFeaturingType via derived property
                instance.getProperty("defaultFeaturingType") as? MDMObject
            }

            // Standard case: resolve library element by qualified name
            else -> resolveBaseConcept(binding.baseConcept)
        }
    }

    /**
     * For an end feature, find the corresponding end feature at the same position
     * in the supertypes of the owning type.
     */
    private fun resolveSupertypeEndFeature(feature: Feature): MDMObject? {
        val owningType = feature.owningType ?: return null
        // Use ownedEndFeature (local only) instead of endFeature (includes inherited),
        // avoiding the Feature.type → featureMembership → inheritedMembership cascade.
        val endFeatures = owningType.ownedEndFeature
        val position = endFeatures.indexOf(feature)
        if (position < 0) return null

        // Find corresponding end feature in supertypes
        for (specialization in owningType.ownedSpecialization) {
            if (specialization.isImplied) continue
            val general = specialization.general ?: continue
            val supertypeEndFeatures = general.ownedEndFeature
            if (position < supertypeEndFeatures.size) {
                return supertypeEndFeatures[position] as? MDMObject
            }
        }
        return null
    }

    private fun evaluateCondition(condition: BindingCondition, instance: MDMObject): Boolean {
        return when (condition) {
            is BindingCondition.Default -> true

            is BindingCondition.TypedBy -> {
                // Check if instance is typed by the specified metaclass.
                // Use the stored ownedTyping → FeatureTyping.type navigable path instead of
                // the derived Feature.type which triggers Type.allInstances() and cascades
                // into inheritedMemberships → supertypes → nonPrivateMemberships recursion.
                if (instance is Feature) {
                    instance.typing.any { typing ->
                        val typedBy = (typing.type as? MDMObject)?.className
                        typedBy == condition.metaclass ||
                            engine.schema.getAllSuperclasses(typedBy ?: "").contains(condition.metaclass)
                    }
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

            is BindingCondition.CollectionEmpty -> {
                val collection = instance.getProperty(condition.property)
                when (collection) {
                    is Collection<*> -> collection.isEmpty()
                    null -> true
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
                        owningType.typing.any { typing ->
                            val typedBy = (typing.type as? MDMObject)?.className
                            typedBy == condition.metaclass ||
                                engine.schema.getAllSuperclasses(typedBy ?: "").contains(condition.metaclass)
                        }
                    } else {
                        (owningType as? MDMObject)?.className == condition.metaclass
                    }
                } else false
            }

            is BindingCondition.HasOwningFeatureMembership -> {
                if (instance is Feature) {
                    val owningFM = instance.owningFeatureMembership
                    val owningMem = instance.owningMembership
                    logger.debug { "HasOwningFeatureMembership check for ${instance.declaredName}: owningFeatureMembership=$owningFM, owningMembership=$owningMem (${owningMem?.let { (it as? MDMObject)?.className }})" }
                    owningFM != null
                } else false
            }

            is BindingCondition.IsEndWithOwningType -> {
                if (instance is Feature) {
                    instance.isEnd && instance.owningType != null
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
        val explicitSpecs = classifier.ownedSpecialization.filter { !it.isImplied }
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

        // Create implied Subclassification using high-level API
        logger.debug { "Creating implied Subclassification: ${classifier.declaredName} -> ${general.declaredName}" }

        val subclassification = engine.createElement("Subclassification") as Subclassification

        // Set properties using high-level API
        subclassification.isImplied = true
        subclassification.owningRelatedElement = classifier
        subclassification.subclassifier = classifier
        subclassification.superclassifier = general
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
        val explicitSubs = feature.ownedSubsetting.filter { !it.isImplied }
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

        // Create implied Subsetting using high-level API
        logger.debug { "Creating implied Subsetting: ${feature.declaredName} -> ${subsettedFeature.declaredName}" }

        val subsetting = engine.createElement("Subsetting") as Subsetting

        // Set properties using high-level API
        subsetting.isImplied = true
        subsetting.owningRelatedElement = feature
        subsetting.subsettingFeature = feature
        subsetting.subsettedFeature = subsettedFeature
    }

    /**
     * Check if a feature already has explicit redefinitions to the target.
     */
    private fun alreadyRedefinesTransitively(feature: Feature, target: Feature): Boolean {
        val explicitRedefs = feature.ownedRedefinition.filter { !it.isImplied }
        return explicitRedefs.isNotEmpty()
    }

    /**
     * Create implied Redefinition if the feature doesn't already have explicit redefinitions.
     * Per KerML redundancy elimination rules, no implied relationship is created if there's already
     * a path to the redefined feature through explicit (non-implied) redefinitions.
     */
    private fun createImpliedRedefinitionIfNeeded(feature: Feature, redefinedFeature: Feature) {
        // Check if feature already transitively redefines this target through explicit redefinitions
        if (alreadyRedefinesTransitively(feature, redefinedFeature)) {
            logger.debug { "Feature ${feature.declaredName} already transitively redefines ${redefinedFeature.declaredName}, skipping implied" }
            return
        }

        // Create implied Redefinition using high-level API
        logger.debug { "Creating implied Redefinition: ${feature.declaredName} -> ${redefinedFeature.declaredName}" }

        val redefinition = engine.createElement("Redefinition") as Redefinition

        // Set properties using high-level API
        redefinition.isImplied = true
        redefinition.owningRelatedElement = feature
        redefinition.redefiningFeature = feature
        redefinition.redefinedFeature = redefinedFeature
    }

    /**
     * Check if a feature already has explicit type featurings to the target type.
     */
    private fun alreadyTypeFeaturesTransitively(feature: Feature, featuringType: Type): Boolean {
        val explicitTypeFeaturings = feature.ownedTypeFeaturing.filter { !it.isImplied }
        return explicitTypeFeaturings.isNotEmpty()
    }

    /**
     * Create implied TypeFeaturing if the feature doesn't already have explicit type featurings.
     * Per KerML redundancy elimination rules, no implied relationship is created if there's already
     * a path to the featuring type through explicit (non-implied) type featurings.
     */
    private fun createImpliedTypeFeaturingIfNeeded(feature: Feature, featuringType: Type) {
        // Check if feature already has explicit type featurings
        if (alreadyTypeFeaturesTransitively(feature, featuringType)) {
            logger.debug { "Feature ${feature.declaredName} already has explicit TypeFeaturing, skipping implied" }
            return
        }

        // Create implied TypeFeaturing using high-level API
        logger.debug { "Creating implied TypeFeaturing: ${feature.declaredName} -> ${featuringType.declaredName}" }

        val typeFeaturing = engine.createElement("TypeFeaturing") as TypeFeaturing

        // Set properties using high-level API
        typeFeaturing.isImplied = true
        typeFeaturing.owningRelatedElement = feature
        typeFeaturing.featureOfType = feature
        typeFeaturing.featuringType = featuringType
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
