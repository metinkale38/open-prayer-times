plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("maven-publish")
}



kotlin {
    jvm()

    sourceSets {

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        }

        val ktor_version = "3.0.1"

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktor_version")
                api("io.ktor:ktor-client-core:$ktor_version")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                api(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                api(project(":praytimes"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-annotations-common"))
            }
        }


    }
}

