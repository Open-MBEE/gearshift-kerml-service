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

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Tests for KerMLNames utility functions.
 */
class KerMLNamesTest : DescribeSpec({

    describe("needsQuoting") {

        it("should return false for valid basic names") {
            KerMLNames.needsQuoting("foo") shouldBe false
            KerMLNames.needsQuoting("Foo") shouldBe false
            KerMLNames.needsQuoting("_foo") shouldBe false
            KerMLNames.needsQuoting("foo123") shouldBe false
            KerMLNames.needsQuoting("Foo_Bar_123") shouldBe false
        }

        it("should return true for names starting with digits") {
            KerMLNames.needsQuoting("123foo") shouldBe true
            KerMLNames.needsQuoting("1") shouldBe true
        }

        it("should return true for names with special characters") {
            KerMLNames.needsQuoting("==") shouldBe true
            KerMLNames.needsQuoting("+") shouldBe true
            KerMLNames.needsQuoting("foo-bar") shouldBe true
            KerMLNames.needsQuoting("foo bar") shouldBe true
            KerMLNames.needsQuoting("foo.bar") shouldBe true
        }

        it("should return true for empty names") {
            KerMLNames.needsQuoting("") shouldBe true
        }
    }

    describe("quoteName") {

        it("should not quote valid basic names") {
            KerMLNames.quoteName("foo") shouldBe "foo"
            KerMLNames.quoteName("Foo_Bar") shouldBe "Foo_Bar"
        }

        it("should quote names with special characters") {
            KerMLNames.quoteName("==") shouldBe "'=='"
            KerMLNames.quoteName("+") shouldBe "'+'"
            KerMLNames.quoteName("foo bar") shouldBe "'foo bar'"
        }

        it("should escape single quotes in names") {
            KerMLNames.quoteName("it's") shouldBe "'it\\'s'"
        }

        it("should escape backslashes in names") {
            KerMLNames.quoteName("a\\b") shouldBe "'a\\\\b'"
        }
    }

    describe("unescapeName") {

        it("should return basic names unchanged") {
            KerMLNames.unescapeName("foo") shouldBe "foo"
            KerMLNames.unescapeName("Foo_Bar") shouldBe "Foo_Bar"
        }

        it("should strip quotes from unrestricted names") {
            KerMLNames.unescapeName("'=='") shouldBe "=="
            KerMLNames.unescapeName("'+'") shouldBe "+"
            KerMLNames.unescapeName("'foo bar'") shouldBe "foo bar"
        }

        it("should unescape escape sequences") {
            KerMLNames.unescapeName("'it\\'s'") shouldBe "it's"
            KerMLNames.unescapeName("'a\\\\b'") shouldBe "a\\b"
            KerMLNames.unescapeName("'line1\\nline2'") shouldBe "line1\nline2"
        }

        it("should handle edge cases") {
            KerMLNames.unescapeName("''") shouldBe ""  // empty unrestricted name
            KerMLNames.unescapeName("'") shouldBe "'"  // single quote alone (not valid, but shouldn't crash)
        }
    }

    describe("buildQualifiedName") {

        it("should join basic names with ::") {
            KerMLNames.buildQualifiedName("Base", "Anything") shouldBe "Base::Anything"
            KerMLNames.buildQualifiedName("A", "B", "C") shouldBe "A::B::C"
        }

        it("should quote segments that need it") {
            KerMLNames.buildQualifiedName("BaseFunctions", "==") shouldBe "BaseFunctions::'=='"
            KerMLNames.buildQualifiedName("BooleanFunctions", "&") shouldBe "BooleanFunctions::'&'"
        }

        it("should handle single segment") {
            KerMLNames.buildQualifiedName("Base") shouldBe "Base"
            KerMLNames.buildQualifiedName("==") shouldBe "'=='"
        }
    }

    describe("parseQualifiedName") {

        it("should split basic qualified names") {
            KerMLNames.parseQualifiedName("Base::Anything") shouldBe listOf("Base", "Anything")
            KerMLNames.parseQualifiedName("A::B::C") shouldBe listOf("A", "B", "C")
        }

        it("should unescape quoted segments") {
            KerMLNames.parseQualifiedName("BaseFunctions::'=='") shouldBe listOf("BaseFunctions", "==")
            KerMLNames.parseQualifiedName("BooleanFunctions::'&'") shouldBe listOf("BooleanFunctions", "&")
        }

        it("should handle single segment") {
            KerMLNames.parseQualifiedName("Base") shouldBe listOf("Base")
            KerMLNames.parseQualifiedName("'=='") shouldBe listOf("==")
        }
    }

    describe("round-trip") {

        it("should preserve names through quote/unescape cycle") {
            val names = listOf("foo", "==", "+", "foo bar", "it's", "a\\b")
            for (name in names) {
                val quoted = KerMLNames.quoteName(name)
                val unescaped = KerMLNames.unescapeName(quoted)
                unescaped shouldBe name
            }
        }

        it("should preserve qualified names through build/parse cycle") {
            val segments = listOf("BaseFunctions", "==")
            val built = KerMLNames.buildQualifiedName(*segments.toTypedArray())
            val parsed = KerMLNames.parseQualifiedName(built)
            parsed shouldBe segments
        }
    }
})
