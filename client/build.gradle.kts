plugins {
    id("maven-publish")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
}



kotlin {
    jvm ()
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
                api(project(":core"))
            }
        }

    }
}

