package com.github.mejiomah17.konstantin.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class RegistryGeneratorPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val kotlinCompile = kotlin.runCatching { target.tasks.getByName("compileKotlin")}.getOrNull()
        if(kotlinCompile!=null){
            target.tasks.create<RegistryGeneratorTask>("konstantinGenerateSource") {
                group = "konstantin"
                dependsOn(kotlinCompile)
            }
        }

    }

}