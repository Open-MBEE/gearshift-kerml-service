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
package org.openmbee.gearshift.kerml.docgen

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Expose
import org.openmbee.gearshift.generated.interfaces.RenderingFeature
import org.openmbee.gearshift.generated.interfaces.SubView
import org.openmbee.gearshift.generated.interfaces.View
import org.openmbee.gearshift.generated.interfaces.ViewpointPredicate
import org.openmbee.mdm.framework.runtime.MDMEngine

private val logger = KotlinLogging.logger {}

/**
 * Resolves a View element into its constituent parts for document generation.
 *
 * Uses the generated typed interfaces which automatically evaluate OCL derivation
 * constraints via [MDMEngine.getProperty]. The derived properties on [View]:
 * - expose — Expose memberships (via ownedRelationship->selectByKind(Expose))
 * - exposedElement — Elements exposed by the View (via expose.importedElement)
 * - [View.viewRendering] — The View's RenderingFeature (if any)
 * - [View.satisfiedViewpoint] — Viewpoints the View satisfies
 * - [View.subview] — Nested sub-Views
 *
 * Fallback: when a View has no Expose children, the View itself is used as context.
 */
class ViewResolverService(private val engine: MDMEngine) {

    /**
     * Resolve a View element by ID into a [ResolvedView].
     *
     * @param viewElementId The element ID of the View to resolve
     * @return The resolved View with all derived properties evaluated
     * @throws ViewNotFoundException if no element exists with the given ID
     * @throws NotAViewException if the element is not a View
     */
    fun resolveView(viewElementId: String): ResolvedView {
        val obj = engine.getElement(viewElementId)
            ?: throw ViewNotFoundException(viewElementId)

        if (obj !is View) {
            throw NotAViewException(viewElementId, obj.className)
        }

        // Exposes: find via ownedRelationship since expose/exposedElement are
        // constraint-only (no association ends, so no generated properties).
        val exposes = obj.ownedRelationship.filterIsInstance<Expose>()
        val exposedElements = exposes.mapNotNull { expose ->
            try {
                expose.importedElement
            } catch (_: Exception) {
                // importedElement is non-null typed but may fail if derivation
                // cannot resolve (e.g., importedMembership not set)
                null
            }
        }

        val renderingFeature = obj.viewRendering
        val viewpoints = obj.satisfiedViewpoint.toList()
        val subviews = obj.subview.toList()

        logger.debug {
            "Resolved View '${obj.declaredName}': " +
                "${exposedElements.size} exposed elements, " +
                "rendering=${renderingFeature != null}, " +
                "${viewpoints.size} viewpoints, " +
                "${subviews.size} subviews"
        }

        return ResolvedView(
            view = obj,
            exposedElements = exposedElements.ifEmpty { listOf(obj) },
            renderingFeature = renderingFeature,
            viewpoints = viewpoints,
            subviews = subviews
        )
    }
}

/**
 * The result of resolving a View element.
 *
 * @param view The View element itself
 * @param exposedElements Elements exposed by the View (falls back to [view] if none)
 * @param renderingFeature The View's RenderingFeature, or null if none
 * @param viewpoints Viewpoints satisfied by the View
 * @param subviews Nested sub-Views
 */
data class ResolvedView(
    val view: View,
    val exposedElements: List<Element>,
    val renderingFeature: RenderingFeature?,
    val viewpoints: List<ViewpointPredicate>,
    val subviews: List<SubView>
)

class ViewNotFoundException(val viewId: String) :
    RuntimeException("View not found: $viewId")

class NotAViewException(val elementId: String, val actualType: String) :
    RuntimeException("Element $elementId is $actualType, not a View")
