package com.github.mejiomah17.konstantin.plugin

import java.io.File
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


fun KotlinMultiplatformExtension.useKonstantinGeneratedSource(
    project: Project,
    configurationProjectName:String = "configuration"
) {
    sourceSets {
        val commonMain by getting {
            val configurationProject = project.rootProject.childProjects[configurationProjectName]!!
            kotlin.srcDir(File(configurationProject.buildDir, "generated"))
        }
    }

    kotlin.runCatching { project.tasks.getByName("jvmMainClasses") }.onSuccess {
        it.dependsOn(":configuration:konstantinGenerateSource")
    }

}

fun KotlinMultiplatformExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)
}