

plugins {
    kotlin("multiplatform")
    `maven-publish`
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
