import com.github.mejiomah17.konstantin.Library
import org.jetbrains.compose.compose

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        maven("https://jetbrains.bintray.com/trove4j")

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.github.mejiomah17.konstantin.Version.kotlin}")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("com.github.DevSrSouza:svg-to-compose:0.8.1")
        classpath("com.google.guava:guava:23.0")
        classpath("com.android.tools:sdk-common:27.2.0-alpha16")
        classpath("com.android.tools:common:27.2.0-alpha16")
        classpath("com.squareup:kotlinpoet:1.7.2")
        classpath("org.ogce:xpp3:1.1.6")
    }
}
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version  "1.0.0-beta5"
    `maven-publish`
}
kotlin {
    jvm()
    js(IR){
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":api"))
                implementation(project(":client"))
                implementation(Library.ktorClientWebsockets)
                implementation(Library.kotlinSerialization)
                api(compose.runtime)
                api(compose.web.core)
//                api(compose.foundation)
//                api(compose.material)
            }
            kotlin.srcDir(File(project.projectDir, "src/generated"))
        }
    }
}

kotlin {
    jvm()
    val publicationsFromMainHost =
        listOf(jvm()).map { it.name } + "kotlinMultiplatform"
    publishing {
        publications {
            matching { it.name in publicationsFromMainHost }.all {
                val targetPublication = this@all
                tasks.withType<AbstractPublishToMaven>()
                    .matching {
                        it.publication == targetPublication
                    }.all {
                        publication.artifactId = "ui-${publication.artifactId}"
                    }
            }
        }
    }
}

tasks.create("generateIcons") {
    val assetsDir = File(project.projectDir, "icons")
    val srcDir = File(project.projectDir, "src/generated")
    doLast {
        br.com.devsrsouza.svg2compose.Svg2Compose.parse(
            applicationIconPackage = "com.github.mejiomah17.konstantin.icons",
            accessorName = "KonstantinIcons",
            outputSourceDirectory = srcDir,
            vectorsDirectory = assetsDir,
            type = br.com.devsrsouza.svg2compose.VectorType.SVG,
            allAssetsPropertyName = "AllAssets"
        )
        print("adsf")
    }

}
