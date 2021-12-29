package com.github.mejiomah17.konstantin.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

open class KonstantinPluginExtension(project: Project) {
    private val objects = project.objects

    val configurationClass: Property<String> = objects.property<String>().convention("null")
}
