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

    // Ktor (web server)
    val ktorVersion = "2.3.7"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

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
    mainClass.set("org.openmbee.gearshift.ApplicationKt")
}

// Task to run the Demo API server
tasks.register<JavaExec>("runDemoApi") {
    description = "Run the Demo API server"
    group = "application"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.openmbee.gearshift.api.DemoApiKt")

    // Allow passing port as argument: ./gradlew runDemoApi --args="9090"
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

// Bootstrap Stubs Task
// Creates minimal stub files when generated code doesn't exist (needed for initial compilation)
tasks.register("createBootstrapStubs") {
    description = "Create minimal stub files for generated code when missing"
    group = "build"

    val interfacesDir = file("${project.projectDir}/src/main/kotlin/org/openmbee/gearshift/generated/interfaces")
    val implDir = file("${project.projectDir}/src/main/kotlin/org/openmbee/gearshift/generated/impl")
    val utilDir = file("${project.projectDir}/src/main/kotlin/org/openmbee/gearshift/generated")

    // Only run if generated directories are missing or empty
    onlyIf {
        !interfacesDir.exists() || !implDir.exists() ||
        interfacesDir.listFiles()?.isEmpty() ?: true ||
        implDir.listFiles()?.isEmpty() ?: true
    }

    doLast {
        interfacesDir.mkdirs()
        implDir.mkdirs()

        // Create ModelElement stub interface
        file("$interfacesDir/ModelElement.kt").writeText("""
            |package org.openmbee.gearshift.generated.interfaces
            |
            |// Bootstrap stub - will be replaced by generated code
            |interface ModelElement {
            |    val id: String?
            |    val className: String
            |}
            |
            |interface Element : ModelElement {
            |    val name: String?
            |    val declaredName: String?
            |}
            |interface Package : Element
            |interface Namespace : Element
            |interface Relationship : Element
            |interface Membership : Relationship
        """.trimMargin())

        // Create BaseModelElementImpl stub
        file("$implDir/BaseModelElementImpl.kt").writeText("""
            |package org.openmbee.gearshift.generated.impl
            |
            |import org.openmbee.gearshift.GearshiftEngine
            |import org.openmbee.gearshift.engine.MDMObject
            |import org.openmbee.gearshift.generated.interfaces.ModelElement
            |
            |// Bootstrap stub - will be replaced by generated code
            |open class BaseModelElementImpl(
            |    internal val wrapped: MDMObject,
            |    internal val engine: GearshiftEngine
            |) : ModelElement {
            |    override val id: String?
            |        get() = wrapped.id
            |    override val className: String
            |        get() = wrapped.className
            |}
        """.trimMargin())

        // Create Wrappers stub
        file("$utilDir/Wrappers.kt").writeText("""
            |package org.openmbee.gearshift.generated
            |
            |import org.openmbee.gearshift.GearshiftEngine
            |import org.openmbee.gearshift.engine.MDMObject
            |import org.openmbee.gearshift.generated.interfaces.ModelElement
            |import org.openmbee.gearshift.generated.impl.BaseModelElementImpl
            |
            |// Bootstrap stub - will be replaced by generated code
            |object Wrappers {
            |    fun wrap(obj: MDMObject, engine: GearshiftEngine): ModelElement {
            |        return BaseModelElementImpl(obj, engine)
            |    }
            |
            |    inline fun <reified T : ModelElement> wrapAs(obj: MDMObject, engine: GearshiftEngine): T {
            |        return wrap(obj, engine) as T
            |    }
            |}
        """.trimMargin())

        println("Created bootstrap stubs for generated code")
    }
}

// Ensure bootstrap stubs are created before compilation if needed
tasks.named("compileKotlin") {
    dependsOn("createBootstrapStubs")
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
