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
package org.openmbee.mdm.framework.codegen

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import java.nio.file.Paths

class TypeScriptCodeGeneratorTest : DescribeSpec({

    val testOutputDir = Paths.get("build/test-ts-codegen")

    fun createConfig() = TypeScriptCodeGenConfig(
        outputDir = testOutputDir,
        metamodelName = "KerML",
        generateDocs = true
    )

    fun createGenerator() = TypeScriptCodeGenerator(createConfig())

    fun createTestRegistry(): MetamodelRegistry {
        val registry = MetamodelRegistry()

        val element = MetaClass(
            name = "Element",
            isAbstract = true,
            attributes = listOf(
                MetaProperty(
                    name = "elementId",
                    type = "String",
                    lowerBound = 1,
                    upperBound = 1
                ),
                MetaProperty(
                    name = "aliasIds",
                    type = "String",
                    lowerBound = 0,
                    upperBound = -1,
                    isOrdered = true
                ),
                MetaProperty(
                    name = "declaredName",
                    type = "String",
                    lowerBound = 0,
                    upperBound = 1
                ),
                MetaProperty(
                    name = "isImpliedIncluded",
                    type = "Boolean",
                    lowerBound = 1,
                    upperBound = 1
                ),
                MetaProperty(
                    name = "name",
                    type = "String",
                    isDerived = true,
                    lowerBound = 0,
                    upperBound = 1,
                    description = "The effective name of this element"
                ),
                MetaProperty(
                    name = "qualifiedName",
                    type = "String",
                    isDerived = true,
                    lowerBound = 0,
                    upperBound = 1
                )
            ),
            description = "The abstract base for all model elements"
        )

        val relationship = MetaClass(
            name = "Relationship",
            isAbstract = true,
            superclasses = listOf("Element"),
            description = "A directed association between elements"
        )

        val namespace = MetaClass(
            name = "Namespace",
            isAbstract = true,
            superclasses = listOf("Element"),
            attributes = listOf(
                MetaProperty(
                    name = "member",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1,
                    isDerived = true,
                    description = "Members of this namespace"
                )
            )
        )

        // Concrete class
        val pkg = MetaClass(
            name = "Package",
            superclasses = listOf("Namespace"),
            description = "A container for model elements"
        )

        // Concrete class with multiple inheritance
        val membership = MetaClass(
            name = "Membership",
            superclasses = listOf("Relationship"),
            attributes = listOf(
                MetaProperty(
                    name = "memberName",
                    type = "String",
                    lowerBound = 0,
                    upperBound = 1
                ),
                MetaProperty(
                    name = "visibility",
                    type = "VisibilityKind",
                    lowerBound = 1,
                    upperBound = 1
                )
            )
        )

        registry.registerClass(element)
        registry.registerClass(relationship)
        registry.registerClass(namespace)
        registry.registerClass(pkg)
        registry.registerClass(membership)

        // Register an association with navigable ends
        registry.registerAssociation(
            org.openmbee.mdm.framework.meta.MetaAssociation(
                name = "ElementOwnership",
                sourceEnd = MetaAssociationEnd(
                    name = "owner",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = 1,
                    isDerived = true
                ),
                targetEnd = MetaAssociationEnd(
                    name = "ownedElement",
                    type = "Element",
                    lowerBound = 0,
                    upperBound = -1,
                    isDerived = true
                )
            )
        )

        return registry
    }

    describe("TypeScriptTypeMapper") {

        it("should map primitive types to TypeScript equivalents") {
            TypeScriptTypeMapper.mapType("String") shouldBe "string"
            TypeScriptTypeMapper.mapType("Boolean") shouldBe "boolean"
            TypeScriptTypeMapper.mapType("Integer") shouldBe "number"
            TypeScriptTypeMapper.mapType("Int") shouldBe "number"
            TypeScriptTypeMapper.mapType("Double") shouldBe "number"
            TypeScriptTypeMapper.mapType("Real") shouldBe "number"
        }

        it("should map types regardless of optionality (? handled by generator)") {
            // Optionality is handled via ? on the property name, not in the type
            TypeScriptTypeMapper.mapType("String") shouldBe "string"
            TypeScriptTypeMapper.mapType("Boolean") shouldBe "boolean"
        }

        it("should map multi-valued types to arrays") {
            TypeScriptTypeMapper.mapType("String", isMultiValued = true) shouldBe "string[]"
            TypeScriptTypeMapper.mapType("Integer", isMultiValued = true) shouldBe "number[]"
        }

        it("should map non-primitive types to ElementRef") {
            TypeScriptTypeMapper.mapType("Element") shouldBe "ElementRef"
            TypeScriptTypeMapper.mapType("Namespace") shouldBe "ElementRef"
        }

        it("should map non-primitive multi-valued to ElementRef array") {
            TypeScriptTypeMapper.mapType("Element", isMultiValued = true) shouldBe "ElementRef[]"
        }

        it("should map non-primitive single to ElementRef") {
            TypeScriptTypeMapper.mapType("Element") shouldBe "ElementRef"
        }

        it("should identify primitive vs model element types") {
            TypeScriptTypeMapper.isPrimitive("String") shouldBe true
            TypeScriptTypeMapper.isPrimitive("Boolean") shouldBe true
            TypeScriptTypeMapper.isPrimitive("Element") shouldBe false
            TypeScriptTypeMapper.isModelElementType("Element") shouldBe true
            TypeScriptTypeMapper.isModelElementType("String") shouldBe false
        }

        it("should map enum types to string") {
            TypeScriptTypeMapper.mapType("VisibilityKind") shouldBe "string"
            TypeScriptTypeMapper.mapType("FeatureDirectionKind") shouldBe "string"
        }

        it("should map MetaProperty correctly") {
            val required = MetaProperty(name = "id", type = "String", lowerBound = 1, upperBound = 1)
            TypeScriptTypeMapper.mapPropertyType(required) shouldBe "string"

            val optional = MetaProperty(name = "name", type = "String", lowerBound = 0, upperBound = 1)
            TypeScriptTypeMapper.mapPropertyType(optional) shouldBe "string"

            val multiValued = MetaProperty(name = "ids", type = "String", lowerBound = 0, upperBound = -1)
            TypeScriptTypeMapper.mapPropertyType(multiValued) shouldBe "string[]"

            val reference = MetaProperty(name = "owner", type = "Element", lowerBound = 0, upperBound = 1)
            TypeScriptTypeMapper.mapPropertyType(reference) shouldBe "ElementRef"
        }

        it("should map MetaAssociationEnd correctly") {
            val singleOptional = MetaAssociationEnd(name = "owner", type = "Element", lowerBound = 0, upperBound = 1)
            TypeScriptTypeMapper.mapAssociationEndType(singleOptional) shouldBe "ElementRef"

            val multiValued = MetaAssociationEnd(name = "members", type = "Element", lowerBound = 0, upperBound = -1)
            TypeScriptTypeMapper.mapAssociationEndType(multiValued) shouldBe "ElementRef[]"

            val required = MetaAssociationEnd(name = "target", type = "Element", lowerBound = 1, upperBound = 1)
            TypeScriptTypeMapper.mapAssociationEndType(required) shouldBe "ElementRef"
        }
    }

    describe("Interface generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should generate interface with extends clause") {
            val pkg = registry.getClass("Package")!!
            val code = generator.generateInterface(pkg, registry)

            code shouldContain "export interface Package extends Namespace {"
        }

        it("should add @id and @type to root element") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "'@id': string;"
            code shouldContain "'@type': KerMLMetaclass;"
        }

        it("should not add @id and @type to non-root elements") {
            val relationship = registry.getClass("Relationship")!!
            val code = generator.generateInterface(relationship, registry)

            code shouldNotContain "'@id'"
            code shouldNotContain "'@type'"
        }

        it("should generate readonly for derived properties") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "readonly name?: string;"
            code shouldContain "readonly qualifiedName?: string;"
        }

        it("should generate optional marker for optional properties") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "declaredName?: string"
        }

        it("should generate required properties without optional marker") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "  elementId: string;"
            code shouldContain "  isImpliedIncluded: boolean;"
        }

        it("should generate array types for multi-valued properties") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "aliasIds: string[];"
        }

        it("should generate JSDoc comments when enabled") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "The abstract base for all model elements"
            code shouldContain "The effective name of this element"
        }

        it("should generate association ends as ElementRef") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldContain "readonly owner?: ElementRef;"
            code shouldContain "readonly ownedElement: ElementRef[];"
        }

        it("should map enum types to string") {
            val membership = registry.getClass("Membership")!!
            val code = generator.generateInterface(membership, registry)

            code shouldContain "visibility: string;"
        }

        it("should generate properties in alphabetical order") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            val aliasPos = code.indexOf("aliasIds")
            val declaredNamePos = code.indexOf("declaredName")
            val elementIdPos = code.indexOf("elementId")
            val isImpliedPos = code.indexOf("isImpliedIncluded")
            val namePos = code.indexOf("  readonly name")  // Avoid matching declaredName
            val qualifiedPos = code.indexOf("qualifiedName")

            // Verify alphabetical ordering
            (aliasPos < declaredNamePos) shouldBe true
            (declaredNamePos < elementIdPos) shouldBe true
            (elementIdPos < isImpliedPos) shouldBe true
            (isImpliedPos < namePos) shouldBe true
            (namePos < qualifiedPos) shouldBe true
        }
    }

    describe("Model file generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should include file header") {
            val classes = registry.getAllClasses()
            val code = generator.generateModelFile(classes, registry)

            code shouldContain "Auto-generated from KerML metamodel"
        }

        it("should include ElementRef type definition") {
            val classes = registry.getAllClasses()
            val code = generator.generateModelFile(classes, registry)

            code shouldContain "export interface ElementRef {"
            code shouldContain "'@id': string;"
        }

        it("should import metaclass type") {
            val classes = registry.getAllClasses()
            val code = generator.generateModelFile(classes, registry)

            code shouldContain "import { KerMLMetaclass } from './kerml-metaclass.type';"
        }

        it("should generate interfaces in topological order") {
            val classes = registry.getAllClasses()
            val code = generator.generateModelFile(classes, registry)

            // Element must come before Relationship, Namespace, Package, Membership
            val elementPos = code.indexOf("export interface Element ")
            val relationshipPos = code.indexOf("export interface Relationship ")
            val namespacePos = code.indexOf("export interface Namespace ")
            val packagePos = code.indexOf("export interface Package ")
            val membershipPos = code.indexOf("export interface Membership ")

            (elementPos < relationshipPos) shouldBe true
            (elementPos < namespacePos) shouldBe true
            (namespacePos < packagePos) shouldBe true
            (relationshipPos < membershipPos) shouldBe true
        }
    }

    describe("Metaclass type file generation") {
        val registry = createTestRegistry()
        val generator = createGenerator()

        it("should only include concrete (non-abstract) classes") {
            val classes = registry.getAllClasses()
            val code = generator.generateMetaclassTypeFile(classes)

            code shouldContain "'Membership'"
            code shouldContain "'Package'"
            // Abstract classes should NOT appear in the union
            code shouldNotContain "'Element'"
            code shouldNotContain "'Relationship'"
            code shouldNotContain "'Namespace'"
        }

        it("should generate a type alias") {
            val classes = registry.getAllClasses()
            val code = generator.generateMetaclassTypeFile(classes)

            code shouldContain "export type KerMLMetaclass ="
        }

        it("should include file header") {
            val classes = registry.getAllClasses()
            val code = generator.generateMetaclassTypeFile(classes)

            code shouldContain "Auto-generated from KerML metamodel"
        }

        it("should list concrete classes in alphabetical order") {
            val classes = registry.getAllClasses()
            val code = generator.generateMetaclassTypeFile(classes)

            val membershipPos = code.indexOf("'Membership'")
            val packagePos = code.indexOf("'Package'")

            (membershipPos < packagePos) shouldBe true
        }
    }

    describe("TypeScriptCodeGenConfig") {

        it("should compute model file name from metamodel name") {
            val config = TypeScriptCodeGenConfig(outputDir = testOutputDir, metamodelName = "KerML")
            config.modelFileName shouldBe "kerml.model.ts"
            config.metaclassTypeFileName shouldBe "kerml-metaclass.type.ts"
        }

        it("should compute file names for different metamodel names") {
            val config = TypeScriptCodeGenConfig(outputDir = testOutputDir, metamodelName = "SysML")
            config.modelFileName shouldBe "sysml.model.ts"
            config.metaclassTypeFileName shouldBe "sysml-metaclass.type.ts"
        }

        it("should filter excluded classes") {
            val config = TypeScriptCodeGenConfig(
                outputDir = testOutputDir,
                excludeClasses = setOf("Package")
            )

            config.shouldGenerate("Element") shouldBe true
            config.shouldGenerate("Package") shouldBe false
        }
    }

    describe("JSDoc suppression") {
        val registry = createTestRegistry()
        val config = TypeScriptCodeGenConfig(
            outputDir = testOutputDir,
            generateDocs = false
        )
        val generator = TypeScriptCodeGenerator(config)

        it("should not generate JSDoc when disabled") {
            val element = registry.getClass("Element")!!
            val code = generator.generateInterface(element, registry)

            code shouldNotContain "/**"
            code shouldNotContain "*/"
        }
    }
})
