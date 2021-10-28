rootProject.name = "konstantin"
pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
include(
    "backend",
    "configuration",
    "api",
    "plugin",
    "ui:common",
    "ui:desktop",
)

