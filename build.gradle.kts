import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.testing.Test

plugins {
    kotlin("multiplatform") version "2.4.10" apply false
    kotlin("plugin.serialization") version "2.4.10" apply true
    kotlin("jvm") version "2.4.10" apply false
    id("io.ktor.plugin") version "3.5.2" apply false
    id("maven-publish")
}

// Release workflows pass the semantic version via the VERSION environment variable.
// Local builds can use -PreleaseVersion=1.2.3, otherwise a snapshot version is used.
val releaseVersion = providers.environmentVariable("VERSION")
    .orElse(providers.gradleProperty("releaseVersion"))
    .orElse("1.0.0-SNAPSHOT")

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

subprojects {
    group = "dev.metinkale"
    version = releaseVersion.get()

    tasks.withType<Test>().configureEach {
        failOnNoDiscoveredTests = false
    }

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            repositories {
                maven {
                    name = "GitHubPackages"
                    // GitHub Packages needs the repository in lowercase.
                    val repository = System.getenv("GITHUB_REPOSITORY")
                        ?: "metinkale38/open-prayer-times"
                    url = uri("https://maven.pkg.github.com/${repository.lowercase()}")

                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                            ?: System.getenv("GITHUB_REPOSITORY_OWNER")
                            ?: ""
                        password = System.getenv("GITHUB_TOKEN") ?: ""
                    }
                }
            }
        }
    }
}
