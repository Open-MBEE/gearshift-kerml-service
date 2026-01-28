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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.ModelElement

/**
 * Base implementation for all generated model element wrappers.
 */
open class BaseModelElementImpl(
    internal val wrapped: MDMObject,
    internal val engine: GearshiftEngine
) : ModelElement {

    override val id: String?
        get() = wrapped.id

    override val className: String
        get() = wrapped.className

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseModelElementImpl) return false
        return wrapped.id == other.wrapped.id
    }

    override fun hashCode(): Int = wrapped.id?.hashCode() ?: 0

    override fun toString(): String = "${className}(${id})"
}

