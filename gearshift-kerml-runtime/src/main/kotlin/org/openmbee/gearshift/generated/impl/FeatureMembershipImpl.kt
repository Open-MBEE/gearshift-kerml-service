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
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Annotation as KerMLAnnotation
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Implementation of FeatureMembership.
 * A membership where the member is a feature
 */
open class FeatureMembershipImpl(
    wrapped: MDMObject,
    engine: GearshiftEngine
) : OwningMembershipImpl(wrapped, engine), FeatureMembership {

    override val ownedMemberFeature: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedMemberFeature")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Feature }
        }

    override val owningType: Type
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningType")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Type }
        }
}

