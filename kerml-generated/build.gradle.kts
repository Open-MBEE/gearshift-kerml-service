plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Framework for MDMEngine, MDMObject, etc.
    implementation(project(":mdm-framework"))

    // Metamodel definitions
    implementation(project(":gearshift-kerml-model"))
}
