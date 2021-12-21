import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version  "1.0.0-beta5"
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":ui:common"))
                implementation(com.github.mejiomah17.konstantin.Library.ktorClientCio)
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}