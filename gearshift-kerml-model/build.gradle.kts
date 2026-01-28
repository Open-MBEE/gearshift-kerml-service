plugins {
    kotlin("jvm")
    id("com.github.hierynomus.license")
}

group = "org.openmbee.gearshift"
version = "0.1.0-SNAPSHOT"

dependencies {
    implementation(project(":gearshift-framework"))

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
