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
package org.openmbee.mdm.framework.constraints.ocl

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

/**
 * Tests for OCL parser.
 */
class OclParserTest : DescribeSpec({

    describe("let expressions") {

        it("should parse simple let expression") {
            val ast = OclParser.parse("let x = 5 in x")
            ast.shouldBeInstanceOf<LetExp>()
        }

        it("should parse let expression with type annotation") {
            val ast = OclParser.parse("let x : String = 'hello' in x")
            ast.shouldBeInstanceOf<LetExp>()
        }
    }

    describe("if expressions") {

        it("should parse simple if expression") {
            val ast = OclParser.parse("if true then 1 else 2 endif")
            ast.shouldBeInstanceOf<IfExp>()
        }

        it("should parse nested if expression") {
            val ast = OclParser.parse("if true then if false then 1 else 2 endif else 3 endif")
            ast.shouldBeInstanceOf<IfExp>()
        }

        it("should parse if with null comparison") {
            val ast = OclParser.parse("if name <> null then name else 'default' endif")
            ast.shouldBeInstanceOf<IfExp>()
        }
    }

    describe("combined let and if") {

        it("should parse let with if body") {
            val ast = OclParser.parse("let n = name in if n = null then null else n endif")
            ast.shouldBeInstanceOf<LetExp>()
        }
    }

    describe("enum literals") {

        it("should parse enum literal with Type::value syntax") {
            val ast = OclParser.parse("VisibilityKind::private")
            ast.shouldBeInstanceOf<VariableExp>()
            (ast as VariableExp).name shouldBe "VisibilityKind::private"
        }

        it("should parse enum comparison") {
            val ast = OclParser.parse("visibility = VisibilityKind::private")
            ast.shouldBeInstanceOf<InfixExp>()
        }

        it("should parse enum in implies expression") {
            val ast = OclParser.parse("owner = null implies visibility = VisibilityKind::private")
            ast.shouldBeInstanceOf<InfixExp>()
            (ast as InfixExp).operator shouldBe "implies"
        }
    }

    describe("collection literals with arrow operations") {

        it("should parse Set literal with arrow") {
            val ast = OclParser.parse("Set{1, 2, 3}->size()")
            ast.shouldBeInstanceOf<ArrowCallExp>()
        }

        it("should parse OrderedSet literal with arrow") {
            val ast = OclParser.parse("OrderedSet{self}->closure(x | x.parent)")
            ast.shouldBeInstanceOf<IteratorExp>()
        }

        it("should parse let with typed OrderedSet") {
            val ast = OclParser.parse("let types: OrderedSet(Type) = OrderedSet{self} in types")
            ast.shouldBeInstanceOf<LetExp>()
        }
    }

    describe("type operations") {

        it("should parse oclType arrow operation") {
            val ast = OclParser.parse("self->oclType()")
            ast.shouldBeInstanceOf<ArrowCallExp>()
            (ast as ArrowCallExp).operationName shouldBe "oclType"
        }

        it("should parse oclIsKindOf with dynamic type") {
            val ast = OclParser.parse("memberElement.oclIsKindOf(other.memberElement.oclType())")
            ast.shouldBeInstanceOf<DynamicTypeExp>()
            (ast as DynamicTypeExp).operationName shouldBe "oclIsKindOf"
        }

        it("should parse chained oclType call") {
            val ast = OclParser.parse("other.memberElement->oclType()")
            ast.shouldBeInstanceOf<ArrowCallExp>()
        }
    }
})
