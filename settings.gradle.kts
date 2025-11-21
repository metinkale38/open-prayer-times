pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "open-prayer-times"

include(":core")
include(":calctimes")
include(":hijri")
include(":server")
include(":diyanet-client")