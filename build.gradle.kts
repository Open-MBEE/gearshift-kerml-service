import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.*

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
    antlr
    application
    id("com.github.hierynomus.license") version "0.16.1"
}

group = "org.openmbee.gearshift"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

application {
    mainClass.set("org.openmbee.gearshift.kerml.ApplicationKt")
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

// Metamodel Code Generation Task
// Generates typed Kotlin interfaces and implementations from KerML metamodel
tasks.register<JavaExec>("generateMetamodelCode") {
    description = "Generate typed Kotlin code from KerML metamodel definitions"
    group = "build"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.openmbee.gearshift.codegen.CodeGeneratorRunner")

    // Default output directory (base src path - package structure is added by generator)
    args = listOf("${project.projectDir}/src/main/kotlin")

    // Ensure main sources are compiled first
    dependsOn("compileKotlin")
}

// License header configuration
license {
    header = file("LICENSE-HEADER.txt")
    strictCheck = true

    // Include source files
    includes(listOf("**/*.kt", "**/*.java", "**/*.gradle.kts"))

    // Exclude generated and third-party files
    excludes(listOf(
        "**/generated-src/**",
        "**/build/**",
        "**/.antlr/**",
        "**/third-party/**",
        "**/gradle/**",
        "**/antlr/**",
        "gradlew",
        "gradlew.bat"
    ))

    // Set copyright year and owner
    ext.set("year", "2026")
    ext.set("owner", "Charles Galey")

    // Use slash-star style for Kotlin/Java files
    mapping("kt", "SLASHSTAR_STYLE")
    mapping("java", "SLASHSTAR_STYLE")

    // Skip license check for ANTLR-generated files
    skipExistingHeaders = true
}
