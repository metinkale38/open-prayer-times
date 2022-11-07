plugins {
    kotlin("js")
}

kotlin {
    js {
        nodejs {}
    }.binaries.executable()
}

dependencies {
    implementation(project(":core"))
    implementation(npm("@google-cloud/functions-framework", "3.1.2"))
    implementation("dev.chriskrueger:kotlin-express:1.1.1")
}