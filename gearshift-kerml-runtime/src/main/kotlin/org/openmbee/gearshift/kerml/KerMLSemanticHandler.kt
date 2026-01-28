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
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.runtime.LifecycleEvent
import org.openmbee.gearshift.framework.runtime.LifecycleHandler
import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.SemanticBinding

private val logger = KotlinLogging.logger {}

/**
 * Handles KerML-specific semantic rules as defined in the KerML specification.
 *
 * This handler implements:
 * - Implicit specializations (Table 8: Core Semantics)
 * - Implicit subsettings (Table 9: Kernel Semantics)
 * - TypeFeaturing relationships
 * - Cleanup of redundant implied relationships
 *
 * All KerML domain knowledge (metaclass names, association names) is encapsulated here,
 * keeping MDMEngine domain-agnostic.
 *
 * @see <a href="https://www.omg.org/spec/KerML">KerML Specification</a>
 */
class KerMLSemanticHandler(
    private val gearshiftEngine: GearshiftEngine,
    private val getParsedElements: () -> Map<String, String> = { emptyMap() }
) : LifecycleHandler {

    override val priority: Int = 50 // Run early to establish implied relationships

    // ===== Reentrant Processing Guard =====

    /**
     * Track instances currently being processed to prevent infinite recursion.
     * When creating implied relationships, link creation triggers handleLinkCreated
     * which could trigger more relationship creation.
     */
    private val processingInstances = mutableSetOf<String>()

    // ===== KerML-specific Constants =====

    /**
     * KerML association names used for specialization relationships.
     */
    private object Associations {
        const val SPECIALIZATION_SPECIFIC = "specializationSpecificAssociation"
        const val GENERALIZATION_GENERAL = "generalizationGeneralAssociation"
        const val SUBCLASSIFICATION_SUBCLASSIFIER = "subclassificationSubclassifierAssociation"
        const val SUPERCLASSIFICATION_SUPERCLASSIFIER = "superclassificationSuperclassifierAssociation"
        const val SUBSETTING_SUBSETTING_FEATURE = "subsettingSubsettingFeatureAssociation"
        const val SUPERSETTING_SUBSETTED_FEATURE = "supersettingSubsettedFeatureAssociation"
        const val OWNING_OWNED_RELATIONSHIP = "owningRelatedElementOwnedRelationshipAssociation"
        const val TYPE_FEATURING_FEATURE_OF_TYPE = "typeFeaturingFeatureOfTypeAssociation"
        const val TYPE_FEATURING_OF_TYPE_FEATURING_TYPE = "typeFeaturingOfTypeFeaturingTypeAssociation"
        const val OWNING_MEMBERSHIP_OWNED_MEMBER_ELEMENT = "owningMembershipOwnedMemberElementAssociation"
        const val MEMBERSHIP_OWNING_NAMESPACE = "membershipOwningNamespaceOwnedMembershipAssociation"
        const val TYPING_BY_TYPE_TYPE = "typingByTypeTypeAssociation"
        const val OWNING_FEATURE_OWNED_TYPING = "owningFeatureOwnedTypingAssociation"
        const val TYPING_TYPED_FEATURE = "typingTypedFeatureAssociation"
    }

    /**
     * KerML metaclass names.
     */
    private object MetaClasses {
        const val SPECIALIZATION = "Specialization"
        const val SUBCLASSIFICATION = "Subclassification"
        const val SUBSETTING = "Subsetting"
        const val TYPE_FEATURING = "TypeFeaturing"
        const val CLASSIFIER = "Classifier"
        const val FEATURE = "Feature"
        const val TYPE = "Type"
        const val NAMESPACE = "Namespace"
        const val FEATURE_MEMBERSHIP = "FeatureMembership"
        const val OWNING_MEMBERSHIP = "OwningMembership"
        const val FEATURE_TYPING = "FeatureTyping"
    }

    // ===== Lifecycle Handler Implementation =====

    override fun handle(event: LifecycleEvent, engine: MDMEngine) {
        when (event) {
            is LifecycleEvent.InstanceCreated -> {
                handleInstanceCreated(event, engine)
            }
            is LifecycleEvent.LinkCreated -> {
                handleLinkCreated(event, engine)
            }
            is LifecycleEvent.InstanceDeleting -> {
                // Currently no specific handling needed
            }
            is LifecycleEvent.LinkDeleting -> {
                // Could restore implied relationships if explicit ones are removed
            }
            is LifecycleEvent.PropertyChanged -> {
                // Could re-evaluate conditional constraints based on property values
            }
        }
    }

    // ===== Instance Created Handling =====

    private fun handleInstanceCreated(event: LifecycleEvent.InstanceCreated, engine: MDMEngine) {
        println("KerMLSemanticHandler: InstanceCreated for ${event.instance.className}")

        // Skip relationship types to avoid infinite recursion
        // When we create implied Subclassification/Subsetting, this handler fires again
        // for those instances - we don't want to process them for implied relationships
        val isRelationshipType = event.instance.className in setOf(
            MetaClasses.SPECIALIZATION,
            MetaClasses.SUBCLASSIFICATION,
            MetaClasses.SUBSETTING,
            MetaClasses.TYPE_FEATURING
        )
        if (isRelationshipType) {
            println("  Skipping relationship type")
            return
        }

        processImplicitSpecializations(event.instance, event.metaClass, engine)
    }

    // ===== Link Created Handling =====

    private fun handleLinkCreated(event: LifecycleEvent.LinkCreated, engine: MDMEngine) {
        val assocName = event.association.name

        // Check for specialization relationships being completed
        // When the general/superclassifier end is linked, we may need to clean up redundant implied relationships
        if (assocName == Associations.GENERALIZATION_GENERAL ||
            assocName == Associations.SUPERCLASSIFICATION_SUPERCLASSIFIER
        ) {
            val specificTypeId = getSpecificTypeId(event.link.sourceId, engine)
            if (specificTypeId != null) {
                cleanupRedundantImplicitSpecializations(specificTypeId, engine)
            }
        }

        // Check for subsetting relationships being completed
        // When a subsettedFeature link is created, clean up redundant implied subsettings
        if (assocName == Associations.SUPERSETTING_SUBSETTED_FEATURE) {
            val subsettingId = event.link.sourceId
            val subsetting = engine.objectRepository.get(subsettingId)
            // Only trigger cleanup for explicit (non-implied) subsettings
            if (subsetting != null && subsetting.getProperty("isImplied") != true) {
                val subsettingFeatureId = getLinkTarget(subsettingId, Associations.SUBSETTING_SUBSETTING_FEATURE, engine)
                if (subsettingFeatureId != null) {
                    cleanupRedundantImplicitSubsettings(subsettingFeatureId, engine)
                }
            }
        }

        // When a FeatureTyping gets its type linked, re-evaluate the owning Feature
        // This is needed for conditional constraints like checkFeatureDataValueSpecialization
        if (assocName == Associations.TYPING_BY_TYPE_TYPE) {
            val featureTypingId = event.link.sourceId
            // Find the feature that owns this FeatureTyping
            // The link is Feature -> FeatureTyping via owningFeatureOwnedTypingAssociation
            val featureLinks = engine.linkRepository.getByAssociationAndTarget(
                Associations.OWNING_FEATURE_OWNED_TYPING, featureTypingId
            )
            for (featureLink in featureLinks) {
                reevaluateConditionalImplicitSpecializations(featureLink.sourceId, engine)
            }
        }

        // When a Feature gets its FeatureTyping linked, check if the FeatureTyping already has a type
        // and re-evaluate the Feature if so. This handles the case where type is linked before ownership.
        if (assocName == Associations.OWNING_FEATURE_OWNED_TYPING) {
            val featureId = event.link.sourceId
            val featureTypingId = event.link.targetId
            // Check if this FeatureTyping already has a type linked
            val typeLinks = engine.linkRepository.getByAssociationAndSource(
                Associations.TYPING_BY_TYPE_TYPE, featureTypingId
            )
            if (typeLinks.isNotEmpty()) {
                reevaluateConditionalImplicitSpecializations(featureId, engine)
            }
        }

        // Re-evaluate conditional implicit specializations for both source and target
        // This handles cases like Association's conditionalImplicitBinaryLink which depends on associationEnd count
        reevaluateConditionalImplicitSpecializations(event.source.id!!, engine)
        reevaluateConditionalImplicitSpecializations(event.target.id!!, engine)
    }

    // ===== Semantic Binding Processing =====

    /**
     * Process SemanticBindings for an instance and create the implied relationships.
     * Uses the new SemanticBinding-based approach instead of OCL constraints.
     */
    private fun processImplicitSpecializations(
        instance: MDMObject,
        metaClass: MetaClass,
        engine: MDMEngine
    ) {
        val instanceId = instance.id ?: return

        // Prevent reentrant processing
        if (instanceId in processingInstances) {
            logger.debug { "Skipping reentrant processing for ${instance.className}" }
            return
        }

        processingInstances.add(instanceId)
        try {
            val allBindings = getAllSemanticBindings(metaClass, engine, mutableSetOf())

            logger.debug { "Processing ${allBindings.size} semantic bindings for ${instance.className}" }

            val processedLibraryTypes = mutableSetOf<String>()

            for (binding in allBindings) {
                if (evaluateBindingCondition(binding.condition, instance, engine)) {
                    logger.debug { "  Binding ${binding.name} condition met, creating relationship to ${binding.baseConcept}" }
                    processSemanticBinding(instance, binding, processedLibraryTypes, engine)
                } else {
                    logger.debug { "  Binding ${binding.name} condition not met" }
                }
            }
        } finally {
            processingInstances.remove(instanceId)
        }
    }

    /**
     * Get all semantic bindings from a metaclass including inherited ones.
     * Uses visited set to prevent infinite recursion from circular metaclass hierarchies.
     */
    private fun getAllSemanticBindings(
        metaClass: MetaClass,
        engine: MDMEngine,
        visited: MutableSet<String>
    ): List<SemanticBinding> {
        // Prevent cycles in metaclass hierarchy
        if (metaClass.name in visited) {
            return emptyList()
        }
        visited.add(metaClass.name)

        val allBindings = metaClass.semanticBindings.toMutableList()

        for (superclassName in metaClass.superclasses) {
            engine.registry.getClass(superclassName)?.let { superclass ->
                allBindings.addAll(getAllSemanticBindings(superclass, engine, visited))
            }
        }

        return allBindings
    }

    /**
     * Process a single semantic binding to create the implied relationship.
     */
    private fun processSemanticBinding(
        instance: MDMObject,
        binding: SemanticBinding,
        processedLibraryTypes: MutableSet<String>,
        engine: MDMEngine
    ) {
        val libraryTypeName = binding.baseConcept

        // Skip if already processed this library type for this instance
        if (libraryTypeName in processedLibraryTypes) {
            logger.debug { "    Already processed $libraryTypeName for ${instance.className}" }
            return
        }
        processedLibraryTypes.add(libraryTypeName)

        val instanceId = instance.id ?: return

        // Resolve the library element
        val targetId = resolveLibraryElement(libraryTypeName, engine)
        if (targetId == null) {
            logger.debug { "    Library element not found: $libraryTypeName" }
            return
        }

        // Don't create self-references
        if (instanceId == targetId) {
            logger.debug { "    Skipping self-reference" }
            return
        }

        when (binding.bindingKind) {
            BindingKind.SPECIALIZES -> {
                // Skip if already has explicit superclassifier
                if (hasAnyExplicitSuperclassifier(instanceId, engine)) {
                    logger.debug { "    Skipping: has explicit superclassifier" }
                    return
                }
                // Skip if already has this subclassification
                if (hasSubclassificationTo(instanceId, targetId, engine)) {
                    logger.debug { "    Skipping: already has subclassification to target" }
                    return
                }
                logger.debug { "    Creating implied subclassification" }
                createImpliedSubclassification(instanceId, targetId, engine)
            }
            BindingKind.SUBSETS -> {
                // Skip if already has explicit subsetted feature
                if (hasAnyExplicitSubsettedFeature(instanceId, engine)) {
                    logger.debug { "    Skipping: has explicit subsetted feature" }
                    return
                }
                // Skip if already has this subsetting
                if (hasSubsettingTo(instanceId, targetId, engine)) {
                    logger.debug { "    Skipping: already has subsetting to target" }
                    return
                }
                logger.debug { "    Creating implied subsetting" }
                createImpliedSubsetting(instanceId, targetId, engine)
            }
        }
    }

    /**
     * Re-evaluate conditional semantic bindings for an instance.
     * Called reactively when links are created that might affect conditions.
     */
    private fun reevaluateConditionalImplicitSpecializations(instanceId: String, engine: MDMEngine) {
        // Prevent reentrant processing
        if (instanceId in processingInstances) {
            return
        }

        val instance = engine.objectRepository.get(instanceId) ?: return
        val metaClass = instance.metaClass

        processingInstances.add(instanceId)
        try {
            val allBindings = getAllSemanticBindings(metaClass, engine, mutableSetOf())

            for (binding in allBindings) {
                // Only re-evaluate bindings with non-default conditions
                if (binding.condition == BindingCondition.Default) continue

                val conditionMet = evaluateBindingCondition(binding.condition, instance, engine)
                val targetId = resolveLibraryElement(binding.baseConcept, engine) ?: continue

                val hasRelationship = when (binding.bindingKind) {
                    BindingKind.SPECIALIZES -> hasSubclassificationTo(instanceId, targetId, engine)
                    BindingKind.SUBSETS -> hasSubsettingTo(instanceId, targetId, engine)
                }

                if (conditionMet && !hasRelationship) {
                    // Condition is now true but we don't have the relationship - create it
                    when (binding.bindingKind) {
                        BindingKind.SPECIALIZES -> createImpliedSubclassification(instanceId, targetId, engine)
                        BindingKind.SUBSETS -> createImpliedSubsetting(instanceId, targetId, engine)
                    }
                }
            }
        } finally {
            processingInstances.remove(instanceId)
        }
    }

    // ===== Binding Condition Evaluation =====

    /**
     * Evaluate a BindingCondition using direct Kotlin logic.
     * This replaces the complex OCL evaluation with simple, debuggable code.
     */
    private fun evaluateBindingCondition(
        condition: BindingCondition,
        instance: MDMObject,
        engine: MDMEngine
    ): Boolean {
        return when (condition) {
            is BindingCondition.Default -> true

            is BindingCondition.TypedBy -> {
                // Check if the instance (Feature) is typed by the specified metaclass
                val types = getFeatureTypes(instance, engine)
                types.any { type ->
                    type.className == condition.metaclass ||
                            engine.registry.isSubclassOf(type.className, condition.metaclass)
                }
            }

            is BindingCondition.OwningTypeIs -> {
                // Check if the owningType is the specified metaclass
                val owningType = getOwningType(instance, engine)
                owningType != null && (
                        owningType.className == condition.metaclass ||
                                engine.registry.isSubclassOf(owningType.className, condition.metaclass)
                        )
            }

            is BindingCondition.OwningTypeTypedBy -> {
                // Check if the owningType is the metaclass OR is a Feature typed by the metaclass
                val owningType = getOwningType(instance, engine) ?: return false

                // Direct match
                if (owningType.className == condition.metaclass ||
                    engine.registry.isSubclassOf(owningType.className, condition.metaclass)
                ) {
                    return true
                }

                // Check if owningType is a Feature typed by the metaclass
                if (isFeatureType(owningType.className, engine)) {
                    val owningTypeTypes = getFeatureTypes(owningType, engine)
                    return owningTypeTypes.any { type ->
                        type.className == condition.metaclass ||
                                engine.registry.isSubclassOf(type.className, condition.metaclass)
                    }
                }

                false
            }

            is BindingCondition.PropertyEquals -> {
                instance.getProperty(condition.property) == condition.value
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

            is BindingCondition.And -> {
                condition.conditions.all { evaluateBindingCondition(it, instance, engine) }
            }

            is BindingCondition.Or -> {
                condition.conditions.any { evaluateBindingCondition(it, instance, engine) }
            }

            is BindingCondition.Not -> {
                !evaluateBindingCondition(condition.condition, instance, engine)
            }
        }
    }

    /**
     * Get the types of a Feature via its FeatureTyping relationships.
     */
    private fun getFeatureTypes(feature: MDMObject, engine: MDMEngine): List<MDMObject> {
        val featureId = feature.id ?: return emptyList()
        val result = mutableListOf<MDMObject>()

        // Get FeatureTypings owned by this feature
        val typingLinks = engine.linkRepository.getByAssociationAndSource(
            Associations.OWNING_FEATURE_OWNED_TYPING, featureId
        )

        for (typingLink in typingLinks) {
            val featureTyping = engine.objectRepository.get(typingLink.targetId) ?: continue

            // Get the type from the FeatureTyping
            val typeLinks = engine.linkRepository.getByAssociationAndSource(
                Associations.TYPING_BY_TYPE_TYPE, featureTyping.id!!
            )

            for (typeLink in typeLinks) {
                val type = engine.objectRepository.get(typeLink.targetId)
                if (type != null) {
                    result.add(type)
                }
            }
        }

        return result
    }

    /**
     * Get the owning Type of an element (for Features, this is via owningFeatureMembership or direct ownership).
     */
    private fun getOwningType(element: MDMObject, engine: MDMEngine): MDMObject? {
        val elementId = element.id ?: return null

        // Try to find via TypeFeaturing relationship
        val featuringLinks = engine.linkRepository.getByAssociationAndSource(
            Associations.TYPE_FEATURING_FEATURE_OF_TYPE, elementId
        )
        for (link in featuringLinks) {
            val typeFeaturing = engine.objectRepository.get(link.targetId) ?: continue
            val typeLinks = engine.linkRepository.getByAssociationAndSource(
                Associations.TYPE_FEATURING_OF_TYPE_FEATURING_TYPE, typeFeaturing.id!!
            )
            for (typeLink in typeLinks) {
                return engine.objectRepository.get(typeLink.targetId)
            }
        }

        // Try via owningRelatedElement (ownership link)
        val ownershipLinks = engine.linkRepository.getByAssociationAndTarget(
            Associations.OWNING_OWNED_RELATIONSHIP, elementId
        )
        for (link in ownershipLinks) {
            val owner = engine.objectRepository.get(link.sourceId)
            if (owner != null && isTypeType(owner.className, engine)) {
                return owner
            }
        }

        return null
    }

    // ===== Type Checking =====

    /**
     * Check if a metaclass name represents a Classifier type.
     */
    private fun isClassifierType(metaclassName: String, engine: MDMEngine): Boolean {
        return metaclassName == MetaClasses.CLASSIFIER ||
                engine.registry.isSubclassOf(metaclassName, MetaClasses.CLASSIFIER)
    }

    /**
     * Check if a metaclass name represents a Feature type.
     */
    private fun isFeatureType(metaclassName: String, engine: MDMEngine): Boolean {
        return metaclassName == MetaClasses.FEATURE ||
                engine.registry.isSubclassOf(metaclassName, MetaClasses.FEATURE)
    }

    /**
     * Check if a metaclass name represents a Type.
     */
    private fun isTypeType(metaclassName: String, engine: MDMEngine): Boolean {
        return metaclassName == MetaClasses.TYPE ||
                engine.registry.isSubclassOf(metaclassName, MetaClasses.TYPE)
    }

    // ===== Library Element Resolution =====

    /**
     * Resolve a library element by qualified name using the high-level API.
     * Returns the element ID or null if not found.
     *
     * Uses Namespace.resolveGlobal() which properly handles qualified name resolution.
     */
    private fun resolveLibraryElement(qualifiedName: String, engine: MDMEngine): String? {
        logger.debug { "Resolving library element: $qualifiedName" }

        // Find any namespace to use for resolution (typically a package or the root)
        val anyNamespace = engine.objectRepository.getAll().firstOrNull { obj ->
            val className = obj.className
            className == "Package" || className == "LibraryPackage" ||
                    engine.registry.isSubclassOf(className, "Namespace")
        }

        if (anyNamespace == null) {
            logger.debug { "  No namespace found for resolution" }
            return null
        }

        // Wrap the namespace and use the high-level resolveGlobal API
        val wrappedNamespace = Wrappers.wrap(anyNamespace, gearshiftEngine) as? Namespace
        if (wrappedNamespace == null) {
            logger.debug { "  Could not wrap namespace ${anyNamespace.id}" }
            return null
        }

        // Use resolveGlobal which properly handles qualified name resolution
        val membership = wrappedNamespace.resolveGlobal(qualifiedName)
        if (membership == null) {
            logger.debug { "  resolveGlobal returned null for $qualifiedName" }
            return null
        }

        // Get the member element from the membership
        val memberElement = membership.memberElement
        if (memberElement == null) {
            logger.debug { "  Membership has no memberElement" }
            return null
        }

        logger.debug { "  Resolved $qualifiedName -> ${memberElement.id}" }
        return memberElement.id
    }

    // ===== Relationship Checking =====

    /**
     * Check if a classifier has any explicit (non-implied) superclassifier.
     */
    private fun hasAnyExplicitSuperclassifier(classifierId: String, engine: MDMEngine): Boolean {
        val subclassifications = engine.objectRepository.getAll().filter {
            it.className == MetaClasses.SUBCLASSIFICATION
        }
        return subclassifications.any { sub ->
            val subclassifier = getLinkTarget(sub.id!!, Associations.SUBCLASSIFICATION_SUBCLASSIFIER, engine)
            val isImplied = sub.getProperty("isImplied") == true
            subclassifier == classifierId && !isImplied
        }
    }

    /**
     * Check if a feature has any explicit (non-implied) subsetted feature.
     */
    private fun hasAnyExplicitSubsettedFeature(featureId: String, engine: MDMEngine): Boolean {
        val subsettings = engine.objectRepository.getAll().filter {
            it.className == MetaClasses.SUBSETTING
        }
        return subsettings.any { sub ->
            val subsettingFeature = getLinkTarget(sub.id!!, Associations.SUBSETTING_SUBSETTING_FEATURE, engine)
            val isImplied = sub.getProperty("isImplied") == true
            subsettingFeature == featureId && !isImplied
        }
    }

    /**
     * Check if a classifier has a direct subclassification to the target.
     */
    private fun hasSubclassificationTo(classifierId: String, targetId: String, engine: MDMEngine): Boolean {
        val subclassifications = engine.objectRepository.getAll().filter {
            it.className == MetaClasses.SUBCLASSIFICATION
        }
        return subclassifications.any { sub ->
            val subclassifier = getLinkTarget(sub.id!!, Associations.SUBCLASSIFICATION_SUBCLASSIFIER, engine)
            val superclassifier = getLinkTarget(sub.id!!, Associations.SUPERCLASSIFICATION_SUPERCLASSIFIER, engine)
            subclassifier == classifierId && superclassifier == targetId
        }
    }

    /**
     * Check if a feature has a direct subsetting to the target.
     */
    private fun hasSubsettingTo(featureId: String, targetId: String, engine: MDMEngine): Boolean {
        val subsettings = engine.objectRepository.getAll().filter {
            it.className == MetaClasses.SUBSETTING
        }
        return subsettings.any { sub ->
            val subsettingFeature = getLinkTarget(sub.id!!, Associations.SUBSETTING_SUBSETTING_FEATURE, engine)
            val subsettedFeature = getLinkTarget(sub.id!!, Associations.SUPERSETTING_SUBSETTED_FEATURE, engine)
            subsettingFeature == featureId && subsettedFeature == targetId
        }
    }

    /**
     * Check if an instance already has a specialization to a library element (by qualified name).
     */
    private fun hasSpecializationTo(instance: MDMObject, libraryQualifiedName: String, engine: MDMEngine): Boolean {
        val instanceId = instance.id ?: return false

        // Get all specializations where this instance is the specific type
        val specializations = engine.linkRepository.getByAssociationAndTarget(
            Associations.SPECIALIZATION_SPECIFIC, instanceId
        )

        for (link in specializations) {
            val specializationId = link.sourceId
            val generalLinks = engine.linkRepository.getByAssociationAndSource(
                Associations.GENERALIZATION_GENERAL, specializationId
            )
            for (generalLink in generalLinks) {
                val generalType = engine.objectRepository.get(generalLink.targetId)
                if (generalType != null) {
                    val qualifiedName = generalType.getProperty("qualifiedName") as? String
                    if (qualifiedName == libraryQualifiedName) {
                        return true
                    }
                }
            }
        }
        return false
    }

    // ===== Relationship Creation =====

    /**
     * Create an implied Subclassification relationship.
     * If link creation fails, the partially created Subclassification is deleted to avoid orphaned instances.
     */
    private fun createImpliedSubclassification(subclassifierId: String, superclassifierId: String, engine: MDMEngine) {
        val sub = engine.createInstance(MetaClasses.SUBCLASSIFICATION)
        val subId = sub.id!!

        try {
            // Set isImplied = true
            engine.setProperty(sub, "isImplied", true)

            // Link subclassifier (Subclassification -> Classifier)
            engine.createLink(Associations.SUBCLASSIFICATION_SUBCLASSIFIER, subId, subclassifierId)

            // Link superclassifier (Subclassification -> Classifier)
            engine.createLink(Associations.SUPERCLASSIFICATION_SUPERCLASSIFIER, subId, superclassifierId)

            // Set ownership - the subclassifier owns this relationship
            engine.createLink(Associations.OWNING_OWNED_RELATIONSHIP, subclassifierId, subId)

            logger.debug { "Created implied subclassification: $subclassifierId -> $superclassifierId" }
        } catch (e: Exception) {
            // Link creation failed, delete the orphaned Subclassification
            logger.warn { "Failed to create implied subclassification: ${e.message}, deleting orphaned Subclassification $subId" }
            engine.removeAllLinks(subId)
            engine.objectRepository.delete(subId)
        }
    }

    /**
     * Create an implied Subsetting relationship.
     * If link creation fails, the partially created Subsetting is deleted to avoid orphaned instances.
     */
    private fun createImpliedSubsetting(subsettingFeatureId: String, subsettedFeatureId: String, engine: MDMEngine) {
        val sub = engine.createInstance(MetaClasses.SUBSETTING)
        val subId = sub.id!!

        try {
            // Set isImplied = true
            engine.setProperty(sub, "isImplied", true)

            // Link subsettingFeature (Subsetting -> Feature)
            engine.createLink(Associations.SUBSETTING_SUBSETTING_FEATURE, subId, subsettingFeatureId)

            // Link subsettedFeature (Subsetting -> Feature)
            engine.createLink(Associations.SUPERSETTING_SUBSETTED_FEATURE, subId, subsettedFeatureId)

            // Set ownership - the subsetting feature owns this relationship
            engine.createLink(Associations.OWNING_OWNED_RELATIONSHIP, subsettingFeatureId, subId)

            logger.debug { "Created implied subsetting: $subsettingFeatureId -> $subsettedFeatureId" }
        } catch (e: Exception) {
            // Link creation failed, delete the orphaned Subsetting
            logger.warn { "Failed to create implied subsetting: ${e.message}, deleting orphaned Subsetting $subId" }
            engine.removeAllLinks(subId)
            engine.objectRepository.delete(subId)
        }
    }

    // ===== Redundant Relationship Cleanup =====

    /**
     * Clean up redundant implicit specializations after an explicit specialization is added.
     */
    private fun cleanupRedundantImplicitSpecializations(specificTypeId: String, engine: MDMEngine) {
        val specificType = engine.objectRepository.get(specificTypeId) ?: return
        val metaClass = specificType.metaClass

        // Only check if this metaclass has semantic bindings
        val hasSemanticBindings = getAllSemanticBindings(metaClass, engine, mutableSetOf()).isNotEmpty()
        if (!hasSemanticBindings) return

        // Get all specializations where this type is the specific type
        val allSpecializations = getSpecializationsForType(specificTypeId, engine)

        // Find implicit specializations
        val implicitSpecs = allSpecializations.filter { spec ->
            spec.getProperty("isImplied") == true
        }
        if (implicitSpecs.isEmpty()) return

        // Find explicit specializations
        val explicitSpecs = allSpecializations.filter { spec ->
            spec.getProperty("isImplied") != true
        }
        if (explicitSpecs.isEmpty()) return

        // For each implicit specialization, check if it's now covered by an explicit one
        for (implicitSpec in implicitSpecs) {
            val implicitGeneralId = getGeneralTypeId(implicitSpec, engine) ?: continue
            val implicitGeneral = engine.objectRepository.get(implicitGeneralId) ?: continue

            for (explicitSpec in explicitSpecs) {
                val explicitGeneralId = getGeneralTypeId(explicitSpec, engine) ?: continue
                val explicitGeneral = engine.objectRepository.get(explicitGeneralId) ?: continue

                // Check if explicitGeneral transitively specializes implicitGeneral
                if (typeSpecializes(explicitGeneral, implicitGeneral, engine)) {
                    logger.debug {
                        "Removing redundant implicit specialization: ${specificType.className} -> " +
                                "${implicitGeneral.getProperty("qualifiedName")}"
                    }
                    deleteSpecialization(implicitSpec, engine)
                    break
                }
            }
        }
    }

    /**
     * Get all Specialization/Subclassification instances where the given type is specific/subclassifier.
     */
    private fun getSpecializationsForType(typeId: String, engine: MDMEngine): List<MDMObject> {
        val result = mutableListOf<MDMObject>()

        // Check via specializationSpecificAssociation
        val specLinks = engine.linkRepository.getByAssociationAndTarget(Associations.SPECIALIZATION_SPECIFIC, typeId)
        for (link in specLinks) {
            engine.objectRepository.get(link.sourceId)?.let { result.add(it) }
        }

        // Check via subclassificationSubclassifierAssociation
        val subclassLinks = engine.linkRepository.getByAssociationAndTarget(
            Associations.SUBCLASSIFICATION_SUBCLASSIFIER, typeId
        )
        for (link in subclassLinks) {
            engine.objectRepository.get(link.sourceId)?.let { result.add(it) }
        }

        return result
    }

    /**
     * Get the general/superclassifier type ID from a Specialization/Subclassification.
     */
    private fun getGeneralTypeId(specialization: MDMObject, engine: MDMEngine): String? {
        val specId = specialization.id ?: return null

        // Try generalizationGeneralAssociation first (for Specialization)
        val generalLinks = engine.linkRepository.getByAssociationAndSource(Associations.GENERALIZATION_GENERAL, specId)
        if (generalLinks.isNotEmpty()) {
            return generalLinks.first().targetId
        }

        // Try superclassificationSuperclassifierAssociation (for Subclassification)
        val superLinks = engine.linkRepository.getByAssociationAndSource(
            Associations.SUPERCLASSIFICATION_SUPERCLASSIFIER, specId
        )
        if (superLinks.isNotEmpty()) {
            return superLinks.first().targetId
        }

        return null
    }

    /**
     * Get the specific/subclassifier type ID from a Specialization/Subclassification ID.
     */
    private fun getSpecificTypeId(specializationId: String, engine: MDMEngine): String? {
        // Try specializationSpecificAssociation first
        val specLinks = engine.linkRepository.getByAssociationAndSource(
            Associations.SPECIALIZATION_SPECIFIC, specializationId
        )
        if (specLinks.isNotEmpty()) {
            return specLinks.first().targetId
        }

        // Try subclassificationSubclassifierAssociation
        val subclassLinks = engine.linkRepository.getByAssociationAndSource(
            Associations.SUBCLASSIFICATION_SUBCLASSIFIER, specializationId
        )
        if (subclassLinks.isNotEmpty()) {
            return subclassLinks.first().targetId
        }

        return null
    }

    /**
     * Check if sourceType transitively specializes targetType.
     */
    private fun typeSpecializes(sourceType: MDMObject, targetType: MDMObject, engine: MDMEngine): Boolean {
        val sourceId = sourceType.id ?: return false
        val targetId = targetType.id ?: return false

        if (sourceId == targetId) return true

        // BFS to find transitive specialization
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add(sourceId)

        while (queue.isNotEmpty()) {
            val currentId = queue.removeFirst()
            if (currentId in visited) continue
            visited.add(currentId)

            if (currentId == targetId) return true

            // Get all general types of current
            val specs = getSpecializationsForType(currentId, engine)
            for (spec in specs) {
                val generalId = getGeneralTypeId(spec, engine)
                if (generalId != null && generalId !in visited) {
                    queue.add(generalId)
                }
            }
        }

        return false
    }

    /**
     * Delete a Specialization/Subclassification and its links.
     */
    private fun deleteSpecialization(specialization: MDMObject, engine: MDMEngine) {
        val specId = specialization.id ?: return

        // Remove all links involving this specialization
        engine.removeAllLinks(specId)

        // Delete the specialization instance
        engine.objectRepository.delete(specId)
    }

    /**
     * Clean up redundant implicit subsettings after an explicit subsetting is added.
     * If a feature explicitly subsets another feature that transitively subsets Base::things,
     * the direct implied subsetting to Base::things becomes redundant.
     */
    private fun cleanupRedundantImplicitSubsettings(featureId: String, engine: MDMEngine) {
        val feature = engine.objectRepository.get(featureId) ?: return

        // Get all subsettings where this feature is the subsettingFeature
        val allSubsettings = engine.objectRepository.getAll().filter {
            it.className == MetaClasses.SUBSETTING
        }
        val featureSubsettings = allSubsettings.filter { sub ->
            getLinkTarget(sub.id!!, Associations.SUBSETTING_SUBSETTING_FEATURE, engine) == featureId
        }

        // Separate into implicit and explicit
        val implicitSubsettings = featureSubsettings.filter { it.getProperty("isImplied") == true }
        val explicitSubsettings = featureSubsettings.filter { it.getProperty("isImplied") != true }

        if (implicitSubsettings.isEmpty() || explicitSubsettings.isEmpty()) return

        // For each implicit subsetting, check if it's covered by an explicit one
        for (implicitSub in implicitSubsettings) {
            val implicitTargetId = getLinkTarget(implicitSub.id!!, Associations.SUPERSETTING_SUBSETTED_FEATURE, engine)
                ?: continue

            for (explicitSub in explicitSubsettings) {
                val explicitTargetId = getLinkTarget(explicitSub.id!!, Associations.SUPERSETTING_SUBSETTED_FEATURE, engine)
                    ?: continue

                val explicitTarget = engine.objectRepository.get(explicitTargetId) ?: continue
                val implicitTarget = engine.objectRepository.get(implicitTargetId) ?: continue

                // Check if the explicit target transitively subsets the implicit target
                if (featureSubsetsFeature(explicitTarget, implicitTarget, engine)) {
                    logger.debug {
                        "Removing redundant implicit subsetting: ${feature.className} -> " +
                                "${implicitTarget.getProperty("qualifiedName") ?: implicitTarget.getProperty("declaredName")}"
                    }
                    deleteSpecialization(implicitSub, engine)
                    break
                }
            }
        }
    }

    /**
     * Check if sourceFeature transitively subsets targetFeature.
     */
    private fun featureSubsetsFeature(sourceFeature: MDMObject, targetFeature: MDMObject, engine: MDMEngine): Boolean {
        val sourceId = sourceFeature.id ?: return false
        val targetId = targetFeature.id ?: return false

        if (sourceId == targetId) return true

        // BFS to find transitive subsetting
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add(sourceId)

        while (queue.isNotEmpty()) {
            val currentId = queue.removeFirst()
            if (currentId in visited) continue
            visited.add(currentId)

            if (currentId == targetId) return true

            // Get all subsetted features of current
            val subsettings = engine.objectRepository.getAll().filter {
                it.className == MetaClasses.SUBSETTING &&
                        getLinkTarget(it.id!!, Associations.SUBSETTING_SUBSETTING_FEATURE, engine) == currentId
            }
            for (sub in subsettings) {
                val subsettedId = getLinkTarget(sub.id!!, Associations.SUPERSETTING_SUBSETTED_FEATURE, engine)
                if (subsettedId != null && subsettedId !in visited) {
                    queue.add(subsettedId)
                }
            }
        }

        return false
    }

    // ===== Helper Functions =====

    /**
     * Helper to get a link target by association name.
     */
    private fun getLinkTarget(instanceId: String, associationName: String, engine: MDMEngine): String? {
        return engine.getLinks(instanceId)
            .filter { it.associationName == associationName }
            .firstOrNull()?.targetId
    }
}
