rootProject.name = "open-prayer-times"

include("core")
//include("cloudfunction-jvm")
include("cloudfunction-js")
include("praytimes")

//project(":cloudfunction-jvm").projectDir = File(settingsDir, "cloudfunction/jvm")
project(":cloudfunction-js").projectDir = File(settingsDir, "cloudfunction/js")