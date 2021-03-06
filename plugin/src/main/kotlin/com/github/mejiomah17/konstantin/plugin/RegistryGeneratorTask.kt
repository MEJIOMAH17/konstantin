package com.github.mejiomah17.konstantin.plugin

import com.github.mejiomah17.konstantin.configuration.ConfigurationProvider
import com.github.mejiomah17.konstantin.configuration.ThingAdapter
import org.github.mejiomah17.konstantin.api.Thing
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.URLClassLoader
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
            appendLine("package ${providerClass.java.packageName}")
            appendLine("import org.github.mejiomah17.konstantin.api.Thing")
            appendLine()
            appendLine("object Registry{")
            configuration.things.forEach {
                appendLine("    val ${it.id} = ${it.toDeclaration()}")
            }
            appendLine("}")
        }

        writeGeneratedCode(providerClass = providerClass, dirPrefix = Constants.generatedUi, source = source)
        writeGeneratedCode(providerClass = providerClass, dirPrefix = Constants.generatedBackend, source = source)
    }

    private fun writeGeneratedCode(providerClass: KClass<*>, dirPrefix: String, source: String) {
        val packageName = providerClass.java.packageName.replace(".", "/")
        val generatedDir = File(project.buildDir, "$dirPrefix/konstantin/src/main/kotlin/$packageName").also {
            it.mkdirs()
        }
        val sourceFile = File(generatedDir, "Registry.kt").also {
            it.createNewFile()
        }
        sourceFile.writeText(source)
    }

    private fun ThingAdapter<*>.toDeclaration(): String {
        return when (val state = defaultState) {
            is Thing.RGBSwitch.RGBSwitchState -> "Thing.RGBSwitch(id=\"${id}\", state = Thing.RGBSwitch.RGBSwitchState(red=${state.red},green=${state.green},blue=${state.blue}))"
            is Thing.Switch.SwitchState -> {
                val value = when (state) {
                    Thing.Switch.SwitchState.Off -> "Thing.Switch.SwitchState.Off"
                    Thing.Switch.SwitchState.On -> "Thing.Switch.SwitchState.On"
                }
                "Thing.Switch(id=\"${id}\", state = $value)"
            }
            is Thing.BooleanSensor.BooleanSensorState -> {
                val value = when (state) {
                    Thing.BooleanSensor.BooleanSensorState.False -> "Thing.BooleanSensor.BooleanSensorState.False"
                    Thing.BooleanSensor.BooleanSensorState.True -> "Thing.BooleanSensor.BooleanSensorState.True"
                }
                "Thing.BooleanSensor(id=\"${id}\",state=$value)"
            }
            is Thing.NumericSensor.NumericState -> "Thing.NumericSensor(id=\"${id}\",Thing.NumericSensor.NumericState(${state.value}))"
        }
    }
}
