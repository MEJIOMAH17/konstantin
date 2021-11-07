rootProject.name = "konstantin"
pluginManagement {
    plugins {
        kotlin("multiplatform") version "1.5.31"
        kotlin("jvm") version "1.5.31"
        kotlin("plugin.serialization") version "1.5.31"

        id("com.android.library") version "7.0.3"
        id("kotlin-android-extensions") version "1.5.31"
    }
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
include(
    "api",
    "backend",
    "client",
    "configuration",
    "plugin",
    "ui:common",
    "ui:desktop",
)

