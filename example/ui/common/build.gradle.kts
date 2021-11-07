import com.github.mejiomah17.konstantin.plugin.useKonstantinGeneratedSource
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat


plugins {
    kotlin("multiplatform")
    id("org.github.mejiomah17.konstantin")
    id("org.jetbrains.compose") version  "1.0.0-beta5"
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {
    jvm()
//    js {
//        browser()
//        binaries.executable()
//    }

    dependencies {
        commonMainImplementation("org.github.mejiomah17.konstantin:api:0.1.0")
        commonMainImplementation("org.github.mejiomah17.konstantin:client:0.1.0")
        commonMainImplementation("org.github.mejiomah17.konstantin:ui-common:0.1.0")
    }
    useKonstantinGeneratedSource(project)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
    }
}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.9.0"
    }
}