pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "open-prayer-times"

include(":client")
include(":calctimes")
include(":hijri")
include(":server")
include(":diyanet-client")
