apply {
    from("$rootDir/base-module.gradle")
}

// when the module is not the app module or an android&tablet module the
// implementation keyword needs to be written with " " and not raw
dependencies {
    "implementation"(project(Modules.core))
}