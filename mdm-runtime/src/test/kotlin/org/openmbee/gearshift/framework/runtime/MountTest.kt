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

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.runtime.*

/**
 * Unit tests for the mount infrastructure.
 */
class MountTest {

    private lateinit var schema: MetamodelRegistry

    @BeforeEach
    fun setup() {
        // Clear the mount registry before each test
        MountRegistry.clear()

        // Create a simple schema for testing
        schema = MetamodelRegistry()
        schema.registerClass(
            MetaClass(
                name = "Element",
                superclasses = emptyList(),
                attributes = listOf(
                    MetaProperty(name = "name", type = "String", lowerBound = 0, upperBound = 1)
                )
            )
        )
        schema.registerClass(
            MetaClass(
                name = "Namespace",
                superclasses = listOf("Element"),
                attributes = emptyList()
            )
        )
        schema.registerClass(
            MetaClass(
                name = "Class",
                superclasses = listOf("Namespace"),
                attributes = emptyList()
            )
        )
    }

    @AfterEach
    fun teardown() {
        MountRegistry.clear()
    }

    // ===== MountRegistry Tests =====

    @Test
    fun `register and retrieve mount`() {
        val engine = MDMEngine(schema)
        val mount = MountRegistry.register(
            id = "test-mount",
            name = "Test Mount",
            engine = engine,
            priority = 100,
            isImplicit = false
        )

        assertEquals("test-mount", mount.id)
        assertEquals("Test Mount", mount.name)
        assertEquals(100, mount.priority)
        assertFalse(mount.isImplicit)

        val retrieved = MountRegistry.get("test-mount")
        assertNotNull(retrieved)
        assertEquals(mount.id, retrieved?.id)
    }

    @Test
    fun `duplicate registration throws exception`() {
        val engine = MDMEngine(schema)
        MountRegistry.register(
            id = "test-mount",
            name = "Test Mount",
            engine = engine
        )

        assertThrows<IllegalArgumentException> {
            MountRegistry.register(
                id = "test-mount",
                name = "Another Mount",
                engine = engine
            )
        }
    }

    @Test
    fun `unregister removes mount`() {
        val engine = MDMEngine(schema)
        MountRegistry.register(
            id = "test-mount",
            name = "Test Mount",
            engine = engine
        )

        assertTrue(MountRegistry.isRegistered("test-mount"))
        val removed = MountRegistry.unregister("test-mount")
        assertNotNull(removed)
        assertFalse(MountRegistry.isRegistered("test-mount"))
    }

    @Test
    fun `getImplicitMounts returns only implicit mounts`() {
        val engine1 = MDMEngine(schema)
        val engine2 = MDMEngine(schema)

        MountRegistry.register("explicit", "Explicit", engine1, 100, isImplicit = false)
        MountRegistry.register("implicit", "Implicit", engine2, 1000, isImplicit = true)

        val implicit = MountRegistry.getImplicitMounts()
        assertEquals(1, implicit.size)
        assertEquals("implicit", implicit[0].id)
    }

    @Test
    fun `getExplicitMounts returns only explicit mounts`() {
        val engine1 = MDMEngine(schema)
        val engine2 = MDMEngine(schema)

        MountRegistry.register("explicit", "Explicit", engine1, 100, isImplicit = false)
        MountRegistry.register("implicit", "Implicit", engine2, 1000, isImplicit = true)

        val explicit = MountRegistry.getExplicitMounts()
        assertEquals(1, explicit.size)
        assertEquals("explicit", explicit[0].id)
    }

    @Test
    fun `qualifiedNameToId converts correctly`() {
        assertEquals("Base", MountRegistry.qualifiedNameToId("Base"))
        assertEquals("KerML_Base", MountRegistry.qualifiedNameToId("KerML::Base"))
        assertEquals("Kernel_Semantic_Library_Base", MountRegistry.qualifiedNameToId("Kernel Semantic Library::Base"))
    }

    // ===== MountableEngine Tests =====

    @Test
    fun `mount adds mount to active mounts`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        MountRegistry.register("test-lib", "Test Library", libraryEngine)

        assertFalse(engine.isMounted("test-lib"))
        engine.mount("test-lib")
        assertTrue(engine.isMounted("test-lib"))
    }

    @Test
    fun `unmount removes mount`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        MountRegistry.register("test-lib", "Test Library", libraryEngine)

        engine.mount("test-lib")
        assertTrue(engine.isMounted("test-lib"))

        val result = engine.unmount("test-lib")
        assertTrue(result)
        assertFalse(engine.isMounted("test-lib"))
    }

    @Test
    fun `getElement finds local elements first`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        val libElement = libraryEngine.createElement("Element")
        libraryEngine.setPropertyValue(libElement, "name", "LibElement")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Create local element
        val localElement = engine.createElement("Element")
        engine.setPropertyValue(localElement, "name", "LocalElement")

        // Local element should be found
        val found = engine.getElement(localElement.id!!)
        assertNotNull(found)
        assertEquals("LocalElement", engine.getPropertyValue(found!!, "name"))
    }

    @Test
    fun `getElement finds mounted elements`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        val libElement = libraryEngine.createElement("Element")
        libraryEngine.setPropertyValue(libElement, "name", "LibElement")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Should find the library element
        val found = engine.getElement(libElement.id!!)
        assertNotNull(found)
        assertEquals("LibElement", engine.getPropertyValue(found!!, "name"))
    }

    @Test
    fun `getAllElements includes mounted elements`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        libraryEngine.createElement("Element")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Create local element
        engine.createElement("Element")

        // Should include both
        val all = engine.getAllElements()
        assertEquals(2, all.size)
    }

    @Test
    fun `getLocalElements excludes mounted elements`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        libraryEngine.createElement("Element")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Create local element
        engine.createElement("Element")

        // Local should only include the one we created in engine
        val local = engine.getLocalElements()
        assertEquals(1, local.size)
    }

    @Test
    fun `setPropertyValue throws for mounted elements`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        val libElement = libraryEngine.createElement("Element")
        libraryEngine.setPropertyValue(libElement, "name", "LibElement")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Should throw when trying to modify
        assertThrows<MountedElementReadOnlyException> {
            engine.setPropertyValue(libElement, "name", "Modified")
        }
    }

    @Test
    fun `removeElement throws for mounted elements`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        val libElement = libraryEngine.createElement("Element")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Should throw when trying to delete
        assertThrows<MountedElementReadOnlyException> {
            engine.removeElement(libElement.id!!)
        }
    }

    @Test
    fun `isMountedElement returns correct value`() {
        val engine = MountableEngine(schema)
        val libraryEngine = MDMEngine(schema)

        // Create element in library
        val libElement = libraryEngine.createElement("Element")

        MountRegistry.register("test-lib", "Test Library", libraryEngine)
        engine.mount("test-lib")

        // Create local element
        val localElement = engine.createElement("Element")

        assertTrue(engine.isMountedElement(libElement.id!!))
        assertFalse(engine.isMountedElement(localElement.id!!))
    }

    @Test
    fun `mountImplicit mounts all implicit mounts`() {
        val libraryEngine1 = MDMEngine(schema)
        val libraryEngine2 = MDMEngine(schema)
        val explicitEngine = MDMEngine(schema)

        MountRegistry.register("implicit1", "Implicit 1", libraryEngine1, 1000, isImplicit = true)
        MountRegistry.register("implicit2", "Implicit 2", libraryEngine2, 1001, isImplicit = true)
        MountRegistry.register("explicit", "Explicit", explicitEngine, 100, isImplicit = false)

        val engine = MountableEngine(schema)
        engine.mountImplicit()

        assertTrue(engine.isMounted("implicit1"))
        assertTrue(engine.isMounted("implicit2"))
        assertFalse(engine.isMounted("explicit"))
    }

    @Test
    fun `mounts are searched by priority order`() {
        val engine = MountableEngine(schema)

        // Create two library engines with elements of different names
        val highPriorityEngine = MDMEngine(schema)
        val lowPriorityEngine = MDMEngine(schema)

        val highElement = highPriorityEngine.createElement("Element")
        highPriorityEngine.setPropertyValue(highElement, "name", "HighPriority")

        val lowElement = lowPriorityEngine.createElement("Element")
        lowPriorityEngine.setPropertyValue(lowElement, "name", "LowPriority")

        // Register with priorities (lower = searched first)
        MountRegistry.register("high", "High Priority", highPriorityEngine, 50)
        MountRegistry.register("low", "Low Priority", lowPriorityEngine, 100)

        engine.mount("low")
        engine.mount("high")

        // Verify priority order in active mounts
        val mounts = engine.getActiveMounts()
        assertEquals("high", mounts[0].id)  // Lower priority = first
        assertEquals("low", mounts[1].id)
    }

    // ===== Session Mount Tests =====

    @Test
    fun `session with mounts can mount and unmount`() {
        val libraryEngine = MDMEngine(schema)
        MountRegistry.register("test-lib", "Test Library", libraryEngine)

        val session = Session.createWithMounts("Test Session", schema)

        assertTrue(session.supportsMounts())
        session.mount("test-lib")
        assertTrue(session.getActiveMounts().contains("test-lib"))

        session.unmount("test-lib")
        assertFalse(session.getActiveMounts().contains("test-lib"))
    }

    @Test
    fun `session without mounts throws on mount operations`() {
        val session = Session.create("Test Session", schema)

        assertFalse(session.supportsMounts())
        assertThrows<IllegalStateException> {
            session.mount("test-lib")
        }
    }

    @Test
    fun `session statistics includes mount info`() {
        val libraryEngine = MDMEngine(schema)
        libraryEngine.createElement("Element")

        MountRegistry.register("test-lib", "Test Library", libraryEngine, isImplicit = true)

        val session = Session.createWithMounts("Test Session", schema)
        // Implicit mount should be auto-mounted

        val stats = session.getStatistics()
        assertTrue(stats.containsKey("activeMounts"))
        assertTrue(stats.containsKey("localElementCount"))
    }

    // ===== StandardMount Tests =====

    @Test
    fun `containsElement returns correct value`() {
        val engine = MDMEngine(schema)
        val element = engine.createElement("Element")

        val mount = StandardMount("test", "Test", engine)

        assertTrue(mount.containsElement(element.id!!))
        assertFalse(mount.containsElement("nonexistent"))
    }

    @Test
    fun `getRootNamespaces delegates to engine`() {
        val engine = MDMEngine(schema)
        // Create a root namespace (no owner)
        val ns = engine.createElement("Namespace")
        engine.setPropertyValue(ns, "name", "RootNs")

        val mount = StandardMount("test", "Test", engine)
        val roots = mount.getRootNamespaces()

        assertEquals(1, roots.size)
    }
}
