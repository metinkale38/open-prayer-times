pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "open-prayer-times"

include("praytimes")
include("providers")
include("hijri")
include("server")
include("server:jvm")
include("server:native")