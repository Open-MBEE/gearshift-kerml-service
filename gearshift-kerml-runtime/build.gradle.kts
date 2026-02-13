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
    kotlin("plugin.serialization")
    antlr
    application
    id("com.github.hierynomus.license")
}

group = "org.openmbee.gearshift"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(project(":mdm-framework"))
    implementation(project(":gearshift-kerml-model"))
    implementation(project(":kerml-generated"))

    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // Kotlin Scripting for runtime code evaluation
    implementation(kotlin("scripting-jsr223"))
    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")

    // Jackson for JSON
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

    // ANTLR for grammars
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Ktor (web server)
    val ktorVersion = "2.3.7"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

// ── GearshiftSettings defaults ────────────────────────────────────────
// Override via Gradle project properties (-P) or system properties (-D).
// Example: ./gradlew runDemoApi -Pgearshift.autoNameFeatures=true
//
// Property                              Default
// ──────────────────────────────────────────────
// gearshift.autoNameFeatures            false
// gearshift.nameStrategy                CAMEL_CASE   (CAMEL_CASE | SNAKE_CASE)
// gearshift.autoShortNames              true
// gearshift.processImpliedRelationships true
// gearshift.autoMountLibraries          true
// gearshift.maxExecutionSteps           1000
// gearshift.preferSymbolicSyntax        true
// gearshift.emitImpliedRelationships    false
// gearshift.writerIndent                "    " (4 spaces)
// gearshift.deterministicElementIds     false
// gearshift.dataDir                     null   (path to persistence directory)
// gearshift.log.level                   INFO   (ERROR | WARN | INFO | DEBUG | TRACE)
// root.log.level                        WARN   (ERROR | WARN | INFO | DEBUG | TRACE)
// ──────────────────────────────────────────────────────────────────────

val gearshiftProperties = listOf(
    "gearshift.autoNameFeatures",
    "gearshift.nameStrategy",
    "gearshift.autoShortNames",
    "gearshift.processImpliedRelationships",
    "gearshift.autoMountLibraries",
    "gearshift.maxExecutionSteps",
    "gearshift.preferSymbolicSyntax",
    "gearshift.emitImpliedRelationships",
    "gearshift.writerIndent",
    "gearshift.deterministicElementIds",
    "gearshift.dataDir",
    "gearshift.log.level",
    "root.log.level"
)

fun JavaForkOptions.forwardGearshiftProperties() {
    for (prop in gearshiftProperties) {
        val value = project.findProperty(prop)?.toString()
            ?: System.getProperty(prop)
        if (value != null) {
            systemProperty(prop, value)
        }
    }
}

tasks.test {
    useJUnitPlatform()
    forwardGearshiftProperties()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.openmbee.gearshift.api.DemoApiKt")
}

// Task to run the Demo API server
tasks.register<JavaExec>("runDemoApi") {
    description = "Run the Demo API server"
    group = "application"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.openmbee.gearshift.api.DemoApiKt")
    forwardGearshiftProperties()
}

tasks.named<JavaExec>("run") {
    forwardGearshiftProperties()
}


// Configure ANTLR to generate Kotlin code
tasks.named<AntlrTask>("generateGrammarSource") {
    maxHeapSize = "64m"
    arguments = arguments + listOf(
        "-visitor",
        "-package", "org.openmbee.gearshift.kerml.antlr"
    )
    outputDirectory = file("${project.buildDir}/generated-src/antlr/main/org/openmbee/gearshift/kerml/antlr")
}

// Ensure ANTLR generates before Kotlin compilation
tasks.named("compileKotlin") {
    dependsOn("generateGrammarSource")
}

// Add generated sources to source sets
sourceSets {
    main {
        java {
            srcDir("${project.buildDir}/generated-src/antlr/main")
        }
    }
}


// License header configuration
license {
    header = rootProject.file("LICENSE-HEADER.txt")
    strictCheck = true
    includes(listOf("**/*.kt", "**/*.java"))
    excludes(
        listOf(
            "**/generated-src/**",
            "**/build/**",
            "**/.antlr/**",
            "**/antlr/**"
        )
    )
    ext.set("year", "2026")
    ext.set("owner", "Charles Galey")
    mapping("kt", "SLASHSTAR_STYLE")
    mapping("java", "SLASHSTAR_STYLE")
    skipExistingHeaders = true
}
