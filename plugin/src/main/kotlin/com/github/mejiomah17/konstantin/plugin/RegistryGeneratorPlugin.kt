package com.github.mejiomah17.konstantin.plugin


import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class RegistryGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val task: RegistryGeneratorTask = target.tasks.create<RegistryGeneratorTask>("konstantinGenerateSource") {
            group = "konstantin"
//            dependsOn(target.tasks.getByName("compileKotlin"))
        }
    }

}