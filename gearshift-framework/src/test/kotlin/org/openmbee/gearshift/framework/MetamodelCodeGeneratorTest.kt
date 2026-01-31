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
package org.openmbee.gearshift.framework.codegen

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.openmbee.gearshift.framework.runtime.MetamodelRegistry
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaOperation
import org.openmbee.gearshift.framework.meta.MetaParameter
import org.openmbee.gearshift.framework.meta.MetaProperty
import java.nio.file.Paths

class MetamodelCodeGeneratorTest : DescribeSpec({

    val testOutputDir = Paths.get("build/test-codegen")

    fun createConfig() = CodeGenConfig(
        outputDir = testOutputDir,
        interfacePackage = "test.interfaces",
        implPackage = "test.impl",
        utilPackage = "test"
    )

    fun createGenerator() = MetamodelCodeGenerator(createConfig())

    fun createTestRegistry(): MetamodelRegistry {
        val registry = MetamodelRegistry()

        // Base Element class
        val element = MetaClass(
            name = "Element",
            isAbstract = true,
            attributes = listOf(
                MetaProperty(
                    name = "declaredName",
                    type = "String",
                    lowerBound = 0,
                    upperBound = 1
                ),
                MetaProperty(
                    name = "name",
                    type = "String",
                    isDerived = true,
                    derivationConstraint = "deriveElementName",
                    lowerBound = 0,
                    upperBound = 1,
                    description = "The effective name of this element"
                )
            ),
            operations = listOf(
                MetaOperation(
                    name = "effectiveName",
                    returnType = "String",
                    description = "Returns the effective name of this element"
                )
            ),
            description = "The abstract base for all model elements"
        )

        // Relationship class extending Element
        val relationship = MetaClass(
            name = "Relationship",
            isAbstract = true,
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(
                    name = "source",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = 1,
                    description = "The source element"
                ),
                MetaProperty(
                    name = "target",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = 1,
                    description = "The target element"
                ),
                MetaProperty(
                    name = "relatedElement",
                    type = "Element",
                    isDerived = true,
                    lowerBound = 0,
                    upperBound = -1,
                    description = "All related elements"
                )
            )
        )

        // Concrete Namespace class
        val namespace = MetaClass(
            name = "Namespace",
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(
                    name = "member",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1,
                    isOrdered = false,
                    isUnique = true,
                    description = "Members of this namespace"
                )
            ),
            operations = listOf(
                MetaOperation(
                    name = "resolve",
                    returnType = "Element",
                    returnLowerBound = 0,  // Nullable return
                    returnUpperBound = 1,
                    parameters = listOf(
                        MetaParameter(name = "qualifiedName", type = "String", lowerBound = 0)  // Nullable param
                    ),
                    description = "Resolve a qualified name to an element"
                )
            )
        )

        // Package extends Namespace
        val pkg = MetaClass(
            name = "Package",
            superclasses = listOf("Namespace"),
            description = "A container for model elements"
        )

        registry.registerClass(element)
        registry.registerClass(relationship)
        registry.registerClass(namespace)
        registry.registerClass(pkg)

        return registry
    }

    describe("TypeMapper") {

        it("should map primitive types") {
            TypeMapper.mapToKotlinType("String") shouldBe "String?"
            TypeMapper.mapToKotlinType("Boolean") shouldBe "Boolean?"
            TypeMapper.mapToKotlinType("Int") shouldBe "Int?"
            TypeMapper.mapToKotlinType("Integer") shouldBe "Int?"
            TypeMapper.mapToKotlinType("Double") shouldBe "Double?"
            TypeMapper.mapToKotlinType("Real") shouldBe "Double?"
        }

        it("should map non-nullable types") {
            TypeMapper.mapToKotlinType("String", isNullable = false) shouldBe "String"
            TypeMapper.mapToKotlinType("Boolean", isNullable = false) shouldBe "Boolean"
        }

        it("should map multi-valued types (defaults to Set for unique unordered)") {
            // With defaults (isOrdered=false, isUnique=true), multi-valued maps to Set
            TypeMapper.mapToKotlinType("String", isMultiValued = true) shouldBe "Set<String>"
            TypeMapper.mapToKotlinType("Element", isMultiValued = true) shouldBe "Set<Element>"
        }

        it("should map unique unordered multi-valued types to Set") {
            TypeMapper.mapToKotlinType("String", isMultiValued = true, isOrdered = false, isUnique = true) shouldBe "Set<String>"
        }

        it("should map ordered multi-valued types to List") {
            TypeMapper.mapToKotlinType("String", isMultiValued = true, isOrdered = true, isUnique = true) shouldBe "List<String>"
        }

        it("should identify model element types") {
            TypeMapper.isPrimitive("String") shouldBe true
            TypeMapper.isPrimitive("Boolean") shouldBe true
            TypeMapper.isPrimitive("Element") shouldBe false
            TypeMapper.isPrimitive("Relationship") shouldBe false
            TypeMapper.isModelElementType("Element") shouldBe true
            TypeMapper.isModelElementType("String") shouldBe false
        }

        it("should provide default values") {
            TypeMapper.getDefaultValue("String") shouldBe "null"
            TypeMapper.getDefaultValue("Boolean") shouldBe "false"
            TypeMapper.getDefaultValue("Int") shouldBe "0"
            TypeMapper.getDefaultValue("Element") shouldBe "null"
            TypeMapper.getDefaultValue("String", isMultiValued = true) shouldBe "emptyList()"
        }
    }

    describe("Interface generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should generate interface with correct package") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "package test.interfaces"
        }

        it("should generate interface with superinterface") {
            val relationship = registry.getClass("Relationship")!!
            val code = generator.generateInterface(relationship, registry)

            code shouldContain "interface Relationship : Element"
        }

        it("should generate interface without superclasses using ModelElement") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "interface Element : ModelElement"
        }

        it("should generate val for derived properties") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "val name: String?"
            code shouldContain "var declaredName: String?"
        }

        it("should generate multi-valued property types") {
            val relationship = registry.getClass("Relationship")!!
            val code = generator.generateInterface(relationship, registry)

            // relatedElement is unique/unordered by default, so generates Set
            code shouldContain "val relatedElement: Set<Element>"
        }

        it("should generate operation signatures") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "fun effectiveName(): String"
        }

        it("should generate operation with parameters") {
            val namespace = registry.getClass("Namespace")!!
            val code = generator.generateInterface(namespace, registry)

            // Parameter and return types are nullable by default
            code shouldContain "fun resolve(qualifiedName: String?): Element?"
        }

        it("should include KDoc comments") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "The abstract base for all model elements"
            code shouldContain "The effective name of this element"
        }
    }

    describe("Implementation generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should generate implementation with correct package") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            code shouldContain "package test.impl"
        }

        it("should generate abstract class for abstract metaclass") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            code shouldContain "abstract class ElementImpl"
        }

        it("should generate open class for concrete metaclass") {
            val pkg = registry.getClass("Package")!!
            val code = generator.generateImplementation(pkg, registry)

            code shouldContain "open class PackageImpl"
        }

        it("should extend base class for root elements") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            code shouldContain ": BaseModelElementImpl(wrapped, engine)"
        }

        it("should extend superclass implementation") {
            val relationship = registry.getClass("Relationship")!!
            val code = generator.generateImplementation(relationship, registry)

            code shouldContain ": ElementImpl(wrapped, engine)"
        }

        it("should generate derived property getter using engine.getProperty") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            // Derived properties must use engine.getProperty for constraint evaluation
            code shouldContain "override val name: String?"
            code shouldContain "engine.getProperty(wrapped.id!!, \"name\")"
        }

        it("should generate regular property getter using wrapped.getProperty") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            // Regular properties access wrapped directly
            code shouldContain "override var declaredName: String?"
            code shouldContain "wrapped.getProperty(\"declaredName\")"
        }

        it("should generate property setter using engine.setProperty") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            code shouldContain "engine.setProperty(wrapped.id!!, \"declaredName\", value)"
        }

        it("should not generate setter for derived properties") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            // Count occurrences of name setter - should be 0
            val nameSetterCount = Regex("set\\(value\\).*\\n.*\"name\"").findAll(code).count()
            nameSetterCount shouldBe 0
        }

        it("should generate reference property with wrapping") {
            val relationship = registry.getClass("Relationship")!!
            val code = generator.generateImplementation(relationship, registry)

            code shouldContain "Wrappers.wrap(it, engine)"
        }

        it("should generate operation delegating to engine.invokeOperation") {
            val element = registry.getClass("Element")!!
            val code = generator.generateImplementation(element, registry)

            code shouldContain "override fun effectiveName(): String"
            code shouldContain "engine.invokeOperation(wrapped.id!!, \"effectiveName\")"
        }

        it("should generate operation with parameters") {
            val namespace = registry.getClass("Namespace")!!
            val code = generator.generateImplementation(namespace, registry)

            // Parameter and return types are nullable by default
            code shouldContain "override fun resolve(qualifiedName: String?): Element?"
            code shouldContain "mapOf(\"qualifiedName\" to qualifiedName)"
        }
    }

    describe("Wrappers generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should generate wrapper factory") {
            val classes = registry.getAllClasses().toList()
            val code = generator.generateWrappers(classes)

            code shouldContain "object Wrappers"
            code shouldContain "fun wrap(obj: MDMObject, engine: MDMEngine): ModelElement"
        }

        it("should generate cases for concrete classes only") {
            val classes = registry.getAllClasses().toList()
            val code = generator.generateWrappers(classes)

            // Concrete classes should have cases
            code shouldContain "\"Namespace\" -> NamespaceImpl(obj, engine)"
            code shouldContain "\"Package\" -> PackageImpl(obj, engine)"

            // Abstract classes should not have cases
            code shouldNotContain "\"Element\" -> ElementImpl"
            code shouldNotContain "\"Relationship\" -> RelationshipImpl"
        }

        it("should have default case") {
            val classes = registry.getAllClasses().toList()
            val code = generator.generateWrappers(classes)

            code shouldContain "else -> BaseModelElementImpl(obj, engine)"
        }
    }

    describe("Base classes generation") {
        val generator = createGenerator()

        it("should generate ModelElement interface") {
            val (interfaceCode, _) = generator.generateBaseClasses()

            interfaceCode shouldContain "interface ModelElement"
            interfaceCode shouldContain "val id: String?"
            interfaceCode shouldContain "val className: String"
        }

        it("should generate BaseModelElementImpl") {
            val (_, implCode) = generator.generateBaseClasses()

            implCode shouldContain "open class BaseModelElementImpl"
            implCode shouldContain "internal val wrapped: MDMObject"
            implCode shouldContain "internal val engine: MDMEngine"
            implCode shouldContain "override val id: String?"
            implCode shouldContain "override fun equals(other: Any?): Boolean"
            implCode shouldContain "override fun hashCode(): Int"
        }
    }

    describe("CodeGenConfig") {

        it("should compute output directories correctly") {
            val config = createConfig()

            config.interfaceOutputDir.toString() shouldBe "build/test-codegen/test/interfaces"
            config.implOutputDir.toString() shouldBe "build/test-codegen/test/impl"
            config.utilOutputDir.toString() shouldBe "build/test-codegen/test"
        }

        it("should filter classes by include list") {
            val config = CodeGenConfig(
                outputDir = testOutputDir,
                includeClasses = setOf("Element", "Package")
            )

            config.shouldGenerate("Element") shouldBe true
            config.shouldGenerate("Package") shouldBe true
            config.shouldGenerate("Relationship") shouldBe false
        }

        it("should filter classes by exclude list") {
            val config = CodeGenConfig(
                outputDir = testOutputDir,
                excludeClasses = setOf("Package")
            )

            config.shouldGenerate("Element") shouldBe true
            config.shouldGenerate("Package") shouldBe false
        }

        it("should prefer exclude over include") {
            val config = CodeGenConfig(
                outputDir = testOutputDir,
                includeClasses = setOf("Element", "Package"),
                excludeClasses = setOf("Package")
            )

            config.shouldGenerate("Element") shouldBe true
            config.shouldGenerate("Package") shouldBe false
        }
    }
})
