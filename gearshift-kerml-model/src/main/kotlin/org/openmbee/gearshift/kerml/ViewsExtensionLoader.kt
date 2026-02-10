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
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.gearshift.kerml.metamodel.associations.createViewsAssociations
import org.openmbee.gearshift.kerml.metamodel.classes.views.*

private val viewsLogger = KotlinLogging.logger {}

/**
 * Extension loader for the Views package (non-normative).
 *
 * Registers View, Rendering, Viewpoint, and related metaclasses that are
 * collapsed from SysML into the KerML metamodel. This is a Gearshift-specific
 * extension — not part of the normative KerML specification.
 *
 * Must be called after [KerMLMetamodelLoader.initialize] since these classes
 * depend on core KerML types (Structure, Predicate, BooleanExpression, etc.).
 *
 * Usage:
 * ```
 * KerMLMetamodelLoader.initialize(registry)
 * ViewsExtensionLoader.initialize(registry)  // opt-in
 * ```
 */
object ViewsExtensionLoader {

    fun initialize(registry: MetamodelRegistry) {
        viewsLogger.info { "Loading Views extension..." }

        registerClasses(registry)
        registerAssociations(registry)

        val classNames = getViewsClasses()
        viewsLogger.info { "Views extension loaded: ${classNames.size} classes, ${createViewsAssociations().size} associations" }
    }

    private fun registerClasses(registry: MetamodelRegistry) {
        // Expose types (depend on Import from root package)
        registry.registerClass(createExposeMetaClass())
        registry.registerClass(createMembershipExposeMetaClass())
        registry.registerClass(createNamespaceExposeMetaClass())

        // View and Rendering (depend on Structure from kernel package)
        registry.registerClass(createRenderingMetaClass())
        registry.registerClass(createViewMetaClass())
        registry.registerClass(createViewRenderingMembershipMetaClass())

        // Viewpoint and ViewpointPredicate (depend on Predicate, BooleanExpression)
        registry.registerClass(createViewpointMetaClass())
        registry.registerClass(createViewpointPredicateMetaClass())
    }

    private fun registerAssociations(registry: MetamodelRegistry) {
        createViewsAssociations().forEach { registry.registerAssociation(it) }
    }

    fun getViewsClasses() = setOf(
        "Expose", "MembershipExpose", "NamespaceExpose",
        "Rendering", "View", "ViewRenderingMembership",
        "Viewpoint", "ViewpointPredicate"
    )
}
