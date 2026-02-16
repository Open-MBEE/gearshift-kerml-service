plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij.platform")
}

group = "org.openmbee.gearshift"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

dependencies {
    implementation(project(":gearshift-kerml-runtime"))
    implementation(project(":gearshift-kerml-model"))
    implementation(project(":kerml-generated"))
    implementation(project(":mdm-runtime"))
    implementation("org.antlr:antlr4-runtime:4.13.1")
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.3")

    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        pluginVerifier()
        instrumentationTools()
    }
}

kotlin {
    jvmToolchain(21)
}

intellijPlatform {
    pluginConfiguration {
        id = "org.openmbee.gearshift.kerml"
        name = "KerML Language Support"
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "243"
            untilBuild = "251.*"
        }
    }
}
