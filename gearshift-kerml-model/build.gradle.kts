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

group = "org.openmbee.gearshift"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(project(":mdm-framework"))

    // Kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // Jackson for JSON (used by enum annotations)
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

// Metamodel Code Generation Task
tasks.register<JavaExec>("generateMetamodelCode") {
    description = "Generate typed Kotlin code from KerML metamodel definitions"
    group = "build"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.openmbee.gearshift.kerml.codegen.CodeGeneratorRunner")

    // Output to the kerml-generated module
    args = listOf("${rootProject.projectDir}/kerml-generated/src/main/kotlin")

    dependsOn("compileKotlin")
}

// TypeScript Type Generation Task
tasks.register<JavaExec>("generateTypeScriptTypes") {
    description = "Generate TypeScript interfaces from KerML metamodel definitions"
    group = "build"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.openmbee.gearshift.kerml.codegen.CodeGeneratorRunner")

    // Use the generateTypeScript entry point
    jvmArgs = listOf("-Dgearshift.codegen.mode=typescript")

    // Output directory: override with -PtsOutputDir=... or defaults to build/generated-ts
    val tsOutputDir = providers.gradleProperty("tsOutputDir")
        .orElse("${rootProject.projectDir}/build/generated-ts")
    args = listOf(tsOutputDir.get())

    dependsOn("compileKotlin")
}

// License header configuration
license {
    header = rootProject.file("LICENSE-HEADER.txt")
    strictCheck = true
    includes(listOf("**/*.kt", "**/*.java"))
    excludes(listOf("**/generated-src/**", "**/build/**"))
    ext.set("year", "2026")
    ext.set("owner", "Charles Galey")
    mapping("kt", "SLASHSTAR_STYLE")
    mapping("java", "SLASHSTAR_STYLE")
    skipExistingHeaders = true
}
