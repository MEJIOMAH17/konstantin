import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.mejiomah17.konstantin.Library

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

dependencies{
    commonMainImplementation(project(":api"))
    commonMainImplementation(Library.kotlinCoroutines)
    commonMainImplementation(Library.ktorClientCore)
    commonMainImplementation(Library.ktorClientWebsockets)
    commonMainImplementation(Library.kotlinSerialization)
    commonMainImplementation(Library.logger)
}

kotlin {
    jvm()
    js(IR){
        browser()
    }
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
