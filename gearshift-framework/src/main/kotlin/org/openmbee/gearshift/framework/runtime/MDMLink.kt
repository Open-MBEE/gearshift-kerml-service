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

/**
 * Runtime instance of a MetaAssociation - represents an edge connecting two MDMObjects.
 *
 * In the graph model:
 * - MDMObject instances are nodes
 * - MDMLink instances are edges
 * - MetaAssociation defines the edge type/schema
 *
 * Links are directional (source -> target) but can be traversed in both directions
 * when the association ends are navigable.
 */
data class MDMLink(
    /**
     * Unique identifier for this link instance.
     */
    val id: String,

    /**
     * The MetaAssociation that defines the type of this link.
     */
    val association: MetaAssociation,

    /**
     * ID of the source instance (corresponds to association.sourceEnd).
     */
    val sourceId: String,

    /**
     * ID of the target instance (corresponds to association.targetEnd).
     */
    val targetId: String
) {
    /**
     * Get the association name (convenience method).
     */
    val associationName: String get() = association.name

    /**
     * Check if this link can be navigated from source to target.
     */
    val isForwardNavigable: Boolean get() = association.targetEnd.isNavigable

    /**
     * Check if this link can be navigated from target to source.
     */
    val isReverseNavigable: Boolean get() = association.sourceEnd.isNavigable

    /**
     * Check if the target end has composite aggregation (target is part of source).
     */
    val isTargetComposite: Boolean
        get() = association.targetEnd.aggregation == org.openmbee.gearshift.framework.meta.AggregationKind.COMPOSITE

    /**
     * Check if the source end has composite aggregation (source is part of target).
     */
    val isSourceComposite: Boolean
        get() = association.sourceEnd.aggregation == org.openmbee.gearshift.framework.meta.AggregationKind.COMPOSITE

    override fun toString(): String {
        return "MDMLink(id='$id', association='${association.name}', " +
               "source='$sourceId' --[${association.sourceEnd.name}/${association.targetEnd.name}]--> target='$targetId')"
    }
}
