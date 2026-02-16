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

plugins {
    kotlin("jvm")
    id("com.github.hierynomus.license")
}

group = "org.openmbee.mdm"
version = "0.1.0-SNAPSHOT"

dependencies {
    // Schema definitions (MetaClass, MetaProperty, etc.)
    implementation(project(":mdm-schema"))

    // Runtime (MetamodelRegistry)
    implementation(project(":mdm-runtime"))

    // Kotlin
    implementation(kotlin("stdlib"))

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

// License header configuration
license {
    header = rootProject.file("LICENSE-HEADER.txt")
    strictCheck = true
    includes(listOf("**/*.kt", "**/*.java"))
    excludes(listOf("**/build/**"))
    ext.set("year", "2026")
    ext.set("owner", "Charles Galey")
    mapping("kt", "SLASHSTAR_STYLE")
    mapping("java", "SLASHSTAR_STYLE")
    skipExistingHeaders = true
}
