plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.time.ExperimentalTime"
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
            artifactId = "hijri"
            pom {
                name.set("hijri")
                description.set("Hijri date conversion and events for open-prayer-times")
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
