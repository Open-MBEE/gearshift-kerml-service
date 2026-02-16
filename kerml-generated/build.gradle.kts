plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Framework for MDMEngine, MDMObject, etc.
    implementation(project(":mdm-runtime"))

    // Metamodel definitions
    implementation(project(":gearshift-kerml-model"))
}
