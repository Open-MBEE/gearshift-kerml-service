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
package org.openmbee.mdm.framework.constraints

import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Listener interface for instance lifecycle events.
 * Implementations can react to instance creation, updates, and deletion.
 */
interface InstanceLifecycleListener {
    /**
     * Called when a new instance is created.
     *
     * @param instanceId The ID of the created instance
     * @param instance The created instance
     */
    fun onInstanceCreated(instanceId: String, instance: MDMObject) {}

    /**
     * Called when an instance property is updated.
     *
     * @param instanceId The ID of the updated instance
     * @param instance The updated instance
     * @param propertyName The name of the property that was changed
     * @param newValue The new value of the property
     */
    fun onInstanceUpdated(instanceId: String, instance: MDMObject, propertyName: String, newValue: Any?) {}

    /**
     * Called when an instance is deleted.
     *
     * @param instanceId The ID of the deleted instance
     */
    fun onInstanceDeleted(instanceId: String) {}
}
