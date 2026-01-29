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
package org.openmbee.gearshift.framework.runtime

import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaClass

/**
 * Lifecycle events fired by MDMEngine during model operations.
 *
 * These events allow domain-specific handlers to react to model changes
 * and perform semantic processing such as creating implied relationships,
 * validating constraints, or maintaining derived state.
 *
 * Analogous to EMF's notification system but with typed events.
 */
sealed interface LifecycleEvent {

    /**
     * Fired after an instance is created and stored, but before it's returned to the caller.
     *
     * Handlers can use this to:
     * - Create implied relationships (e.g., implicit specializations)
     * - Initialize derived properties
     * - Register the instance with external systems
     *
     * @property instance The newly created instance
     * @property metaClass The metaclass of the instance
     */
    data class InstanceCreated(
        val instance: MDMObject,
        val metaClass: MetaClass
    ) : LifecycleEvent

    /**
     * Fired after a link is created and stored.
     *
     * Handlers can use this to:
     * - Re-evaluate conditional implied relationships
     * - Clean up redundant implied relationships
     * - Update derived properties that depend on relationships
     *
     * @property link The newly created link
     * @property source The source instance of the link
     * @property target The target instance of the link
     * @property association The association defining this link type
     */
    data class LinkCreated(
        val link: MDMLink,
        val source: MDMObject,
        val target: MDMObject,
        val association: MetaAssociation
    ) : LifecycleEvent

    /**
     * Fired before an instance is deleted.
     *
     * Handlers can use this to:
     * - Clean up related elements
     * - Remove references from external systems
     * - Perform cascade operations not handled by the engine
     *
     * @property instance The instance about to be deleted
     */
    data class InstanceDeleting(
        val instance: MDMObject
    ) : LifecycleEvent

    /**
     * Fired before a link is deleted.
     *
     * Handlers can use this to:
     * - Re-evaluate implied relationships that may need to be restored
     * - Update derived state
     *
     * @property link The link about to be deleted
     * @property source The source instance of the link
     * @property target The target instance of the link
     * @property association The association defining this link type
     */
    data class LinkDeleting(
        val link: MDMLink,
        val source: MDMObject,
        val target: MDMObject,
        val association: MetaAssociation
    ) : LifecycleEvent

    /**
     * Fired after a property value is changed on an instance.
     *
     * Handlers can use this to:
     * - Re-evaluate conditional constraints
     * - Update dependent derived properties
     * - Trigger semantic rules based on property values
     *
     * @property instance The instance whose property changed
     * @property propertyName The name of the changed property
     * @property oldValue The previous value (may be null)
     * @property newValue The new value (may be null)
     */
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
 * Implementations react to model changes and perform domain-specific semantic processing.
 * Multiple handlers can be registered and are invoked in priority order.
 *
 * This is analogous to EMF's Adapter pattern but with:
 * - Typed events instead of generic notifications
 * - Priority ordering for deterministic execution
 * - Access to the model for creating additional elements
 *
 * Example usage:
 * ```kotlin
 * class MySemanticHandler : LifecycleHandler {
 *     override fun handle(event: LifecycleEvent, model: MDMEngine) {
 *         when (event) {
 *             is LifecycleEvent.InstanceCreated -> {
 *                 // Create implied relationships
 *             }
 *             is LifecycleEvent.LinkCreated -> {
 *                 // Re-evaluate conditions
 *             }
 *             else -> { /* ignore */ }
 *         }
 *     }
 * }
 * ```
 */
interface LifecycleHandler {

    /**
     * Priority for ordering multiple handlers. Lower values run first.
     * Default is 100. Use lower values (e.g., 50) for handlers that must run early,
     * higher values (e.g., 200) for handlers that should run after others.
     */
    val priority: Int get() = 100

    /**
     * Handle a lifecycle event.
     *
     * Implementations should be careful about:
     * - Infinite loops: Creating elements in response to InstanceCreated will fire more events
     * - Performance: Handlers are called synchronously, blocking the operation
     * - Exceptions: Uncaught exceptions will propagate to the caller
     *
     * @param event The event that occurred
     * @param model The MDMEngine, for querying or modifying the model
     */
    fun handle(event: LifecycleEvent, model: MDMEngine)
}
