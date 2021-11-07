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
    "configuration",
    "server",
    "ui:android",
    "ui:common",
    "ui:desktop",
    "ui:web"
)

