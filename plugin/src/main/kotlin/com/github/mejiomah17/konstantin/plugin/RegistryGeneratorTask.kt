package com.github.mejiomah17.konstantin.plugin

import org.github.mejiomah17.konstantin.configuration.ConfigurationProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URLClassLoader
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import kotlin.reflect.KClass
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
                appendLine("    val ${it.id} = Thing.Switch(id=\"${it.id}\", state = Thing.Switch.SwitchState.On)")
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