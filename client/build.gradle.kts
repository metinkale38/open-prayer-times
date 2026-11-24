plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("maven-publish")
}

dependencies {
    api("io.ktor:ktor-client-core:3.5.2")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    api(project(":calctimes"))

    implementation("io.ktor:ktor-client-cio:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("io.ktor:ktor-client-mock:3.5.2")
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

java {
    withSourcesJar()
}

tasks.withType<Jar>().configureEach {
    from(rootProject.file("LICENSE")) { into("META-INF") }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "client"
            pom {
                name.set("client")
                description.set("JVM client for the open-prayer-times API")
                url.set("https://github.com/metinkale38/open-prayer-times")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("metinkale38")
                        name.set("Metin Kale")
                        url.set("https://github.com/metinkale38")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/metinkale38/open-prayer-times.git")
                    developerConnection.set("scm:git:ssh://github.com/metinkale38/open-prayer-times.git")
                    url.set("https://github.com/metinkale38/open-prayer-times")
                }
            }
        }
    }
}
