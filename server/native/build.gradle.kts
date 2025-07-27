val sqldelight_version: String by project
val ktor_version: String by project
plugins {
    kotlin("multiplatform")
    id("io.ktor.plugin") version "3.2.2"
}

kotlin {
    applyDefaultHierarchyTemplate()
    if (project.findProperty("skipNative") != null) {
        linuxX64("linux") {
            compilations.configureEach {
                cinterops {
                    create("sqlite") {
                        definitionFile = File("${project.projectDir.absolutePath}/src/linuxMain/cinterop/sqlite3.def")
                    }
                }
            }


            binaries {
                executable {
                    entryPoint = "dev.metinkale.prayertimes.main"
                }
            }
        }
    }

    sourceSets {
        linuxMain.dependencies {
            implementation(project(":server"))
            implementation("app.cash.sqldelight:native-driver:${sqldelight_version}")
        }
    }
}
