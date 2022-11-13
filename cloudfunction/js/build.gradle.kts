import com.github.gradle.node.npm.task.NpxTask

repositories {
    mavenCentral()
}


plugins {
    id("com.github.node-gradle.node") version "3.5.0"
}

val core = project.parent?.findProject(":core")!!
val coreDep = core.tasks.findByName("jsMainClasses")

val copyResources by tasks.creating(Copy::class) {
    from("../../core/src/commonMain/resources")
    into("build")
}

val copySources by tasks.creating(Copy::class) {
    from("./src/runScripts")
    into("build")
}


val build by tasks.creating(NpxTask::class) {
    dependsOn(coreDep)
    dependsOn(copyResources)
    dependsOn(copySources)
    inputs.dir("../../build/js/packages")
    outputs.files("./build/api.js")
    command.set("webpack")
    args.addAll("-c", "./src/buildScripts/package.js")
}


val run by tasks.creating(NpxTask::class) {
    dependsOn(build)
    workingDir.set(File("build"))
    command.set("@google-cloud/functions-framework")
    args.addAll("--target=api")
}
