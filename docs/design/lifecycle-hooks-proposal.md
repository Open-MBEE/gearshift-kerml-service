# MDMEngine Lifecycle Hooks Design Proposal

## Overview

Extract domain-specific semantic logic from MDMEngine into pluggable handlers,
following EMF's pattern of keeping the core runtime domain-agnostic.

## Proposed Interface

```kotlin
package org.openmbee.gearshift.engine

/**
 * Lifecycle events that can trigger semantic processing.
 */
sealed interface LifecycleEvent {
    /** Fired after an instance is created but before it's returned to caller. */
    data class InstanceCreated(
        val instance: MDMObject,
        val metaClass: MetaClass
    ) : LifecycleEvent

    /** Fired after a link is created. */
    data class LinkCreated(
        val link: MDMLink,
        val source: MDMObject,
        val target: MDMObject,
        val association: MetaAssociation
    ) : LifecycleEvent

    /** Fired before an instance is deleted. */
    data class InstanceDeleting(
        val instance: MDMObject
    ) : LifecycleEvent

    /** Fired after a property is set. */
    data class PropertyChanged(
        val instance: MDMObject,
        val propertyName: String,
        val oldValue: Any?,
        val newValue: Any?
    ) : LifecycleEvent
}

/**
 * Handler for MDMEngine lifecycle events.
 *
 * Implementations can react to model changes and perform semantic processing
 * such as creating implied relationships, validating constraints, etc.
 *
 * Analogous to EMF's Adapter pattern but with typed events.
 */
interface LifecycleHandler {
    /**
     * Priority for ordering multiple handlers. Lower values run first.
     * Default is 100.
     */
    val priority: Int get() = 100

    /**
     * Handle a lifecycle event.
     *
     * @param event The event that occurred
     * @param engine The MDMEngine (for creating additional elements, links, etc.)
     */
    fun handle(event: LifecycleEvent, engine: MDMEngine)
}
```

## MDMEngine Changes

```kotlin
class MDMEngine(
    val registry: MetamodelRegistry,
    val objectRepository: ModelRepository,
    val linkRepository: LinkRepository
) {
    // NEW: Lifecycle handlers
    private val lifecycleHandlers = mutableListOf<LifecycleHandler>()

    /**
     * Register a lifecycle handler.
     */
    fun addLifecycleHandler(handler: LifecycleHandler) {
        lifecycleHandlers.add(handler)
        lifecycleHandlers.sortBy { it.priority }
    }

    /**
     * Remove a lifecycle handler.
     */
    fun removeLifecycleHandler(handler: LifecycleHandler) {
        lifecycleHandlers.remove(handler)
    }

    private fun fireEvent(event: LifecycleEvent) {
        for (handler in lifecycleHandlers) {
            handler.handle(event, this)
        }
    }

    // MODIFIED: createInstance now fires event
    fun createInstance(
        className: String,
        id: String? = null
    ): MDMObject {
        val metaClass = registry.getClass(className)
            ?: throw IllegalArgumentException("Unknown class: $className")

        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }

        val instance = MDMObject(className, metaClass)
        val instanceId = id ?: UUID.randomUUID().toString()
        instance.id = instanceId
        instances[instanceId] = instance
        objectRepository.store(instanceId, instance)

        // Fire lifecycle event - handlers can create implied relationships
        fireEvent(LifecycleEvent.InstanceCreated(instance, metaClass))

        return instance
    }

    // MODIFIED: createLink now fires event
    fun createLink(associationName: String, sourceId: String, targetId: String): MDMLink {
        // ... existing validation logic ...

        val link = MDMLink(
            id = UUID.randomUUID().toString(),
            association = association,
            sourceId = sourceId,
            targetId = targetId
        )

        linkRepository.store(link)

        // Fire lifecycle event - handlers can react to new relationships
        fireEvent(LifecycleEvent.LinkCreated(link, source, target, association))

        return link
    }

    // REMOVED from MDMEngine:
    // - processImplicitSpecializationConstraints
    // - processImplicitSpecialization
    // - reevaluateConditionalImplicitSpecializations
    // - resolveLibraryElement
    // - hasSpecializationTo
    // - createSpecializationRelationship
    // - isClassifier
    // - cleanupRedundantImplicitSpecializations
    // - getSpecializationsForType
    // - getGeneralTypeId
    // - typeSpecializes
    // - deleteSpecialization
    // - getSpecificTypeId
    // - All the hard-coded association name references
}
```

## KerML Semantic Handler

```kotlin
package org.openmbee.gearshift.kerml

/**
 * Handles KerML-specific semantic rules including:
 * - Implicit specializations (Table 8)
 * - Implicit subsettings (Table 9)
 * - TypeFeaturing relationships
 * - Cleanup of redundant implied relationships
 *
 * This encapsulates all KerML domain knowledge that was previously
 * embedded in MDMEngine.
 */
class KerMLSemanticHandler(
    private val nameResolver: NameResolver
) : LifecycleHandler {

    override val priority: Int = 50  // Run early

    // KerML-specific association names (no longer in MDMEngine)
    private object Associations {
        const val SPECIALIZATION_SPECIFIC = "specializationSpecificAssociation"
        const val GENERALIZATION_GENERAL = "generalizationGeneralAssociation"
        const val SUBCLASSIFICATION_SUBCLASSIFIER = "subclassificationSubclassifierAssociation"
        const val SUPERCLASSIFICATION_SUPERCLASSIFIER = "superclassificationSuperclassifierAssociation"
        const val SUBSETTING_SUBSETTING_FEATURE = "subsettingSubsettingFeatureAssociation"
        const val SUPERSETTING_SUBSETTED_FEATURE = "supersettingSubsettedFeatureAssociation"
        const val OWNING_OWNED_RELATIONSHIP = "owningRelatedElementOwnedRelationshipAssociation"
    }

    // KerML-specific metaclass names
    private object MetaClasses {
        const val SPECIALIZATION = "Specialization"
        const val SUBCLASSIFICATION = "Subclassification"
        const val SUBSETTING = "Subsetting"
        const val CLASSIFIER = "Classifier"
        const val FEATURE = "Feature"
        const val NAMESPACE = "Namespace"
    }

    override fun handle(event: LifecycleEvent, engine: MDMEngine) {
        when (event) {
            is LifecycleEvent.InstanceCreated -> {
                processImplicitSpecializations(event.instance, event.metaClass, engine)
            }
            is LifecycleEvent.LinkCreated -> {
                handleLinkCreated(event, engine)
            }
            is LifecycleEvent.InstanceDeleting -> {
                // Could handle cleanup here if needed
            }
            is LifecycleEvent.PropertyChanged -> {
                // Could re-evaluate conditions based on property changes
            }
        }
    }

    private fun processImplicitSpecializations(
        instance: MDMObject,
        metaClass: MetaClass,
        engine: MDMEngine
    ) {
        val allConstraints = getAllConstraints(metaClass, engine.registry)

        val conditionalConstraints = allConstraints.filter {
            it.type == ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION
        }
        val unconditionalConstraints = allConstraints.filter {
            it.type == ConstraintType.IMPLICIT_SPECIALIZATION
        }

        val processedLibraryTypes = mutableSetOf<String>()

        // Process conditional first
        for (constraint in conditionalConstraints) {
            val libraryTypeName = constraint.libraryTypeName ?: continue
            if (evaluateCondition(instance, constraint.expression, engine)) {
                processImplicitSpecialization(instance, libraryTypeName, processedLibraryTypes, engine)
            }
        }

        // Then unconditional
        for (constraint in unconditionalConstraints) {
            val libraryTypeName = constraint.libraryTypeName ?: continue
            processImplicitSpecialization(instance, libraryTypeName, processedLibraryTypes, engine)
        }
    }

    private fun handleLinkCreated(event: LifecycleEvent.LinkCreated, engine: MDMEngine) {
        val assocName = event.association.name

        // Check for specialization relationships being completed
        if (assocName == Associations.GENERALIZATION_GENERAL ||
            assocName == Associations.SUPERCLASSIFICATION_SUPERCLASSIFIER
        ) {
            val specificTypeId = getSpecificTypeId(event.link.sourceId, engine)
            if (specificTypeId != null) {
                cleanupRedundantImplicitSpecializations(specificTypeId, engine)
            }
        }

        // Re-evaluate conditional implicit specializations
        reevaluateConditionalImplicitSpecializations(event.source.id!!, engine)
        reevaluateConditionalImplicitSpecializations(event.target.id!!, engine)
    }

    // ... rest of the methods moved from MDMEngine and KerMLModelFactory ...
    // All using the Associations and MetaClasses constants above

    private fun isClassifier(obj: MDMObject, registry: MetamodelRegistry): Boolean {
        return obj.className == MetaClasses.CLASSIFIER ||
               registry.isSubclassOf(obj.className, MetaClasses.CLASSIFIER)
    }

    private fun isFeature(obj: MDMObject, registry: MetamodelRegistry): Boolean {
        return obj.className == MetaClasses.FEATURE ||
               registry.isSubclassOf(obj.className, MetaClasses.FEATURE)
    }

    // ... etc
}
```

## Integration in KerMLModelFactory

```kotlin
class KerMLModelFactory(
    val engine: GearshiftEngine = GearshiftEngine()
) {
    private val coordinator = KerMLParseCoordinator(engine)

    init {
        // Load the KerML metamodel
        KerMLMetamodelLoader.initialize(engine)

        // Register KerML semantic handler
        engine.mdmEngine.addLifecycleHandler(
            KerMLSemanticHandler(engine.mdmEngine.nameResolver)
        )
    }

    fun parseString(kermlText: String): KerMLPackage? {
        lastParseResult = coordinator.parseString(kermlText)

        if (lastParseResult!!.errors.isNotEmpty()) {
            return null
        }

        // REMOVED: createImpliedRelationships()
        // Now handled automatically by lifecycle hooks during parsing

        return allOfType<KerMLPackage>().firstOrNull()
    }

    // REMOVED all of:
    // - createImpliedRelationships()
    // - processImplicitSpecializationConstraints()
    // - evaluateCondition()
    // - isClassifierType() / isFeatureType()
    // - hasAnyExplicitSuperclassifier() / hasAnyExplicitSubsettedFeature()
    // - createImpliedSubclassification() / createImpliedSubsetting()
    // - createTypeFeaturings()
    // etc.
}
```

## Benefits

1. **MDMEngine becomes domain-agnostic**
   - No hard-coded KerML association or metaclass names
   - Core runtime is truly generic like EMF's EObject

2. **Single source of truth for KerML semantics**
   - All implied relationship logic in KerMLSemanticHandler
   - No duplication between MDMEngine and KerMLModelFactory

3. **Reactive processing**
   - Implied relationships created as instances are created
   - Cleanup happens as links are established
   - No separate "post-processing" pass needed

4. **Extensible**
   - Other metamodels can register their own handlers
   - Multiple handlers can coexist with priority ordering
   - Easy to add new event types (PropertyChanged, etc.)

5. **Testable**
   - KerMLSemanticHandler can be tested in isolation
   - Can mock the engine for unit tests
   - Clear separation of concerns

## Migration Path

1. Add `LifecycleHandler` interface and `fireEvent` infrastructure to MDMEngine
2. Create `KerMLSemanticHandler` with logic from both MDMEngine and KerMLModelFactory
3. Register handler in KerMLModelFactory.init
4. Remove duplicate logic from MDMEngine (the ~400 lines of KerML-specific code)
5. Remove duplicate logic from KerMLModelFactory
6. Update tests to verify behavior is preserved