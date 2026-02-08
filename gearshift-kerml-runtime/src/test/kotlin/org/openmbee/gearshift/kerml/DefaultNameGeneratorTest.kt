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

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DefaultNameGeneratorTest {

    @Test
    fun `default name from type uses camelCase`() {
        assertEquals("engine", DefaultNameGenerator.defaultNameFromType("Engine"))
        assertEquals("powerSystem", DefaultNameGenerator.defaultNameFromType("PowerSystem"))
        assertEquals("a", DefaultNameGenerator.defaultNameFromType("A"))
        assertEquals("element", DefaultNameGenerator.defaultNameFromType(""))
    }

    @Test
    fun `default name from type uses snake_case`() {
        assertEquals("engine", DefaultNameGenerator.defaultNameFromType("Engine", NameStrategy.SNAKE_CASE))
        assertEquals("power_system", DefaultNameGenerator.defaultNameFromType("PowerSystem", NameStrategy.SNAKE_CASE))
        assertEquals("http_client", DefaultNameGenerator.defaultNameFromType("HTTPClient", NameStrategy.SNAKE_CASE))
    }

    @Test
    fun `unique name appends index on collision`() {
        assertEquals("engine", DefaultNameGenerator.uniqueName("engine", emptySet()))
        assertEquals("engine2", DefaultNameGenerator.uniqueName("engine", setOf("engine")))
        assertEquals("engine3", DefaultNameGenerator.uniqueName("engine", setOf("engine", "engine2")))
        assertEquals("engine4", DefaultNameGenerator.uniqueName("engine", setOf("engine", "engine2", "engine3")))
    }

    @Test
    fun `default short name uses type letter prefix`() {
        assertEquals("e1", DefaultNameGenerator.defaultShortName("Engine", emptySet()))
        assertEquals("e2", DefaultNameGenerator.defaultShortName("Engine", setOf("e1")))
        assertEquals("p1", DefaultNameGenerator.defaultShortName("Port", emptySet()))
        assertEquals("p2", DefaultNameGenerator.defaultShortName("Port", setOf("p1")))
    }

    @Test
    fun `default short name uses e prefix for untyped`() {
        assertEquals("e1", DefaultNameGenerator.defaultShortName(null, emptySet()))
        assertEquals("e2", DefaultNameGenerator.defaultShortName(null, setOf("e1")))
    }

    @Test
    fun `settings presets`() {
        // DEFAULT is strict round-trip — no auto-naming
        assertEquals(false, GearshiftSettings.DEFAULT.autoNameFeatures)

        // EDITOR enables auto-naming
        assertEquals(true, GearshiftSettings.EDITOR.autoNameFeatures)
        assertEquals(true, GearshiftSettings.EDITOR.autoShortNames)
    }

    @Test
    fun `auto-naming disabled by default does not modify names`() {
        val kerml = """
            package Test {
                class A {
                    feature;
                }
            }
        """.trimIndent()

        val model = KerMLModel()
        val pkg = model.parseString(kerml)
        assertNotNull(pkg)

        val features = model.allOfType<org.openmbee.gearshift.generated.interfaces.Feature>()
        val anonymous = features.filter { it.declaredName == null }
        // With DEFAULT settings, anonymous features stay anonymous
        assert(anonymous.isNotEmpty()) { "Should have anonymous features with DEFAULT settings" }
    }

    @Test
    fun `auto-naming with EDITOR settings assigns names`() {
        val kerml = """
            package Test {
                class Engine;
                class Car {
                    feature : Engine;
                    feature : Engine;
                }
            }
        """.trimIndent()

        val model = KerMLModel(settings = GearshiftSettings.EDITOR)
        val pkg = model.parseString(kerml)
        assertNotNull(pkg)

        val features = model.allOfType<org.openmbee.gearshift.generated.interfaces.Feature>()
        val carFeatures = features.filter {
            it.owningNamespace?.declaredName == "Car"
        }

        // Both features should now have names
        val names = carFeatures.mapNotNull { it.declaredName }
        assert(names.isNotEmpty()) { "Features should have been auto-named" }

        // Short names should also be assigned
        val shortNames = carFeatures.mapNotNull { it.declaredShortName }
        assert(shortNames.isNotEmpty()) { "Features should have short names" }
    }
}
