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

package org.openmbee.gearshift.generated

import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.ModelElement

/**
 * Factory for typed model element access.
 *
 * In the new architecture where Impl extends MDMObject, this is primarily
 * a cast operation - the MDMObject IS already the typed Impl.
 */
object Wrappers {

    /**
     * Get a typed view of an MDMObject.
     *
     * If the object is already a ModelElement (which it should be in the new
     * architecture where Impl extends MDMObject), simply returns it.
     */
    fun wrap(obj: MDMObject, engine: MDMEngine): ModelElement {
        // In the new architecture, MDMObjects ARE ModelElements (Impls extend MDMObject)
        if (obj is ModelElement) {
            return obj
        }

        // Fallback for any legacy code paths - shouldn't happen in normal use
        throw IllegalStateException(
            "MDMObject '${obj.className}' is not a typed ModelElement. " +
            "Use the factory to create typed instances."
        )
    }

    /**
     * Get a typed view with explicit type parameter.
     */
    inline fun <reified T : ModelElement> wrapAs(obj: MDMObject, engine: MDMEngine): T {
        return wrap(obj, engine) as T
    }
}
