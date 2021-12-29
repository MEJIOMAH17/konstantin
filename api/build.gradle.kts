

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

dependencies {
    commonMainImplementation(com.github.mejiomah17.konstantin.Library.kotlinSerialization)
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
                    .matching { it.publication == targetPublication }
            }
        }
    }
}
