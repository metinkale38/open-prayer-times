val logback_version: String by project
val ktor_version: String by project
val sqldelight_version: String by project

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        create("TimesDatabase") {
            packageName.set("org.metinkale.prayertimes.db")
            srcDirs.setFrom("src/commonMain/kotlin")
        }
    }
}


kotlin {
    applyDefaultHierarchyTemplate()
    jvm { }
    if (project.findProperty("skipNative") != null) {
        linuxX64("linux") {}
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }


        commonMain.dependencies {
            api("io.ktor:ktor-server-core:$ktor_version")
            implementation(project(":providers"))
            api("io.ktor:ktor-server-cio:$ktor_version")
            implementation(project(":hijri"))
            implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
            implementation("io.ktor:ktor-server-cors:$ktor_version")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("ch.qos.logback:logback-classic:$logback_version")
            implementation("app.cash.sqldelight:runtime:$sqldelight_version")
        }

        linuxMain.dependencies {
            implementation("app.cash.sqldelight:native-driver:${sqldelight_version}")
        }
        jvmMain.dependencies {
            implementation("app.cash.sqldelight:sqlite-driver:${sqldelight_version}")
        }
    }
}