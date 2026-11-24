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
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.time.ExperimentalTime"
        )
    }
}

java {
    withSourcesJar()
}

tasks.withType<Jar>().configureEach {
    from("LICENSE") { into("META-INF") }
    from("LICENSE.GPL-3.0") { into("META-INF") }
    from("NOTICE.txt") { into("META-INF") }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "calctimes"
            pom {
                name.set("calctimes")
                description.set("Offline prayer time calculation engine based on PrayTimes.org")
                url.set("https://github.com/metinkale38/open-prayer-times")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
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
