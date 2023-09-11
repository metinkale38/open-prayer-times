plugins {
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("maven-publish")
}



kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    sourceSets {

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.coroutines.DelicateCoroutinesApi")
        }

        val ktor_version = "2.3.4"

        val commonMain by getting {
            dependencies {
                api("io.ktor:ktor-client-core:$ktor_version")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api(project(":praytimes"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktor_version")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktor_version")
            }
        }


    }
}

