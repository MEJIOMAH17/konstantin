import com.github.mejiomah17.konstantin.Library
import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha3"
    `maven-publish`
}
kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":api"))
                implementation(project(":client"))
                implementation(Library.ktorClientWebsockets)
                implementation(Library.kotlinSerialization)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
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
