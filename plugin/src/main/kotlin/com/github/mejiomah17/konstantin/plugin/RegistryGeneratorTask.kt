package com.github.mejiomah17.konstantin.plugin

import com.github.mejiomah17.konstantin.configuration.ConfigurationProvider
import java.io.File
import java.net.URLClassLoader
import org.github.mejiomah17.konstantin.api.Thing
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import kotlin.reflect.full.primaryConstructor


abstract class RegistryGeneratorTask : DefaultTask() {
    @get:Input
    abstract val configurationClass: Property<String>

    @TaskAction
    open fun invoke() {
        val runtimeClasspath = (project.extensions.getByName("sourceSets") as org.gradle.api.tasks.SourceSetContainer)
            .getByName("main").runtimeClasspath
        val urls = runtimeClasspath.files.map {
            it.toURI().toURL()
        }.toTypedArray()
        val cl: ClassLoader = URLClassLoader(urls, this::class.java.classLoader)
        val providerClass = cl.loadClass(configurationClass.get()).kotlin
        val provider = providerClass.primaryConstructor!!.call() as ConfigurationProvider
        val configuration = provider.createConfiguration()
        val source = buildString {
            appendLine("import org.github.mejiomah17.konstantin.api.Thing")
            appendLine()
            appendLine("object Registry{")
            configuration.things.forEach {
                val defaultState = it.defaultState
                val thingDeclaration = when (defaultState) {
                    is Thing.RGBSwitch.RGBSwitchState -> "Thing.RGBSwitch(id=\"${it.id}\", state = Thing.RGBSwitch.RGBSwitchState(red=${defaultState.red},green=${defaultState.green},blue=${defaultState.blue}))"
                    is Thing.Switch.SwitchState -> {
                        val value = when(defaultState){
                            Thing.Switch.SwitchState.Off -> "Thing.Switch.SwitchState.Off"
                            Thing.Switch.SwitchState.On -> "Thing.Switch.SwitchState.On"
                        }
                        "Thing.Switch(id=\"${it.id}\", state = $value)"
                    }
                }
                appendLine("    val ${it.id} = $thingDeclaration")
            }
            appendLine("}")
        }
        val packageName = providerClass.java.packageName.replace(".", "/")
        val generatedDir = File(project.buildDir, "generated/konstantin/src/main/kotlin/$packageName").also {
            it.mkdirs()
        }
        val sourceFile = File(generatedDir, "KonstantinBinding.kt").also {
            it.createNewFile()
        }
        sourceFile.writeText(source)
    }
}