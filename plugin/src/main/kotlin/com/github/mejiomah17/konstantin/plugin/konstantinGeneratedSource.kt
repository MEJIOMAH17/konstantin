package com.github.mejiomah17.konstantin.plugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import java.io.File

fun KotlinMultiplatformExtension.useKonstantinGeneratedSource(
    project: Project,
    configurationProjectName: String = "configuration"
) {
    sourceSets {
        val commonMain by getting {
            val configurationProject = project.rootProject.childProjects[configurationProjectName]!!
            kotlin.srcDir(File(configurationProject.buildDir, Constants.generatedUi))
        }
    }

    kotlin.runCatching { project.tasks.getByName("compileKotlinJvm") }.onSuccess {
        it.dependsOn(":configuration:konstantinGenerateSource")
    }
}

fun KotlinJvmProjectExtension.useKonstantinGeneratedSource(
    project: Project,
    configurationProjectName: String = "configuration"
) {

    val configurationProject = project.rootProject.childProjects[configurationProjectName]!!
    val konstantinBindingCompile = this.sourceSets.create("konstantinBindingCompile")
    konstantinBindingCompile.kotlin.srcDir(File(configurationProject.buildDir, Constants.generatedBackend))
    this.sourceSets.getByName("main").dependsOn(konstantinBindingCompile)

    kotlin.runCatching { project.tasks.getByName("compileKotlin") }.onSuccess {
        it.dependsOn(":configuration:konstantinGenerateSource")
    }
}

internal fun KotlinMultiplatformExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>) {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)
}
