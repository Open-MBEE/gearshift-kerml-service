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

import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.QualifiedNameConfig

/**
 * KerML-specific configuration for qualified name computation.
 *
 * Defines how KerML elements are named (declaredName / declaredShortName),
 * escaped (single-quote wrapping for non-basic names), and owned
 * (ownedMembership → ownedMemberElement traversal).
 */
object KerMLQualifiedNameConfig : QualifiedNameConfig {

    override val separator = "::"

    private val BASIC_NAME_REGEX = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")

    override fun getLocalName(element: MDMObject): String? {
        // Read stored properties directly — no OCL
        val declaredName = element.getProperty("declaredName") as? String
        val declaredShortName = element.getProperty("declaredShortName") as? String
        return declaredName ?: declaredShortName
    }

    override fun escapeName(name: String): String {
        // KerML basic name: starts with letter/underscore, contains only letters/digits/underscores
        return if (name.matches(BASIC_NAME_REGEX)) name else "'$name'"
    }

    override fun getOwnedElements(engine: MDMEngine, element: MDMObject): List<MDMObject> {
        val elementId = element.id ?: return emptyList()
        // Navigate: element → ownedMembership → ownedMemberElement
        // Uses navigateAssociation which handles subsets/redefines
        // (e.g., ownedFeatureMembership subsets ownedMembership)
        val memberships = engine.navigateAssociation(element, "ownedMembership")
        return memberships.flatMap { membership ->
            try {
                engine.navigateAssociation(membership, "ownedMemberElement")
            } catch (_: Exception) {
                // Some memberships may not have ownedMemberElement set
                emptyList()
            }
        }
    }
}
