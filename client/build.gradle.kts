plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("maven-publish")
}

dependencies {
    implementation("io.ktor:ktor-client-cio:3.5.2")
    implementation("io.ktor:ktor-client-core:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")
    implementation(project(":calctimes"))

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.js.ExperimentalJsExport",
            "-opt-in=kotlin.time.ExperimentalTime",
            "-opt-in=kotlinx.coroutines.DelicateCoroutinesApi"
        )
    }
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) { into("META-INF") }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "open-prayer-times-client"
            pom {
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
            }
        }
    }
}