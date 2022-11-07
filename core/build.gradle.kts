plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}


repositories {
    mavenCentral()
}


val generatedResourcePath = File(project.buildDir, "/generated/resources").also { it.mkdirs() }

kotlin {
    js {
        nodejs { }
    }
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                api(project(":praytimes"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }

        val jsMain by getting {
            dependencies {
                //implementation(npm("Resources", generatedResourcePath))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("com.google.maps:google-maps-services:2.1.0")
                implementation("org.slf4j:slf4j-simple:1.7.25")
            }
        }

    }
}


val task = tasks.register("jsGenerateResources") {
    doLast {
        kotlin.sourceSets["commonMain"].resources.srcDirs.forEach { file ->
            File(generatedResourcePath, "${file.name}.js").bufferedWriter().use {
                fun processFile(tab: Int, file: File) {
                    val name = file.name.let { if ("." in it) "\"$it\"" else it }
                    if (tab == 0) {
                        it.write("    ".repeat(tab) + "module.exports = {\n")
                        file.listFiles()?.forEach { processFile(tab + 1, it) }
                        it.write("    ".repeat(tab) + "}\n")
                    } else if (file.isDirectory) {
                        it.write("    ".repeat(tab) + "${name}: {\n")
                        file.listFiles()?.forEach { processFile(tab + 1, it) }
                        it.write("    ".repeat(tab) + "},\n")
                    } else {
                        it.write("    ".repeat(tab) + "$name: [")
                        file.readLines().forEach { line ->
                            it.write("\"$line\", ")
                        }
                        it.write("],\n")
                    }
                }

                processFile(0, file)
            }

            File(generatedResourcePath, "package.json").writeText(
                """{
  "name": "Resources",
  "description": "",
  "version": "0.1.0",
  "dependencies": {},
  "devDependencies": {}
}"""
            )
        }
    }
}

tasks.findByName("jsPackageJson")?.dependsOn(task)