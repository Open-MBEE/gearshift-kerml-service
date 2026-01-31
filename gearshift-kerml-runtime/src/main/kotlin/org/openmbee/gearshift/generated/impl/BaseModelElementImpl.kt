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

package org.openmbee.gearshift.generated.impl

import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.ModelElement

/**
 * Base implementation for all generated model elements.
 * Extends MDMObject directly - the impl IS the model object.
 *
 * MDMObject already provides id and className, satisfying ModelElement.
 */
open class BaseModelElementImpl : MDMObject, ModelElement {

    internal val engine: MDMEngine

    /**
     * Primary constructor - creates a new instance with the given class info.
     */
    constructor(className: String, metaClass: MetaClass, engine: MDMEngine) : super(className, metaClass) {
        this.engine = engine
    }

    /**
     * Migration constructor - for backward compatibility with existing Impl code.
     * Takes an MDMObject and copies its properties to this instance.
     *
     * Note: In the new architecture, you should use the primary constructor
     * and create typed instances directly via the factory.
     */
    constructor(wrapped: MDMObject, engine: MDMEngine) : super(wrapped.className, wrapped.metaClass) {
        this.engine = engine
        // Copy all properties from the wrapped object
        wrapped.getAllProperties().forEach { (k, v) -> setProperty(k, v) }
        this.id = wrapped.id
    }

    /**
     * Alias for backward compatibility - returns this since impl IS the object.
     */
    internal val wrapped: MDMObject get() = this
}
