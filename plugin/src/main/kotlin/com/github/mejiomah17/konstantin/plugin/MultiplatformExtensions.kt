package com.github.mejiomah17.konstantin.plugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


fun KotlinMultiplatformExtension.konstantinGeneratedSource(){
    jvm("konstantin"){

    }
    sourceSets {
        val konstantinMain by getting {
            kotlin.srcDir("generated/konstantin/src/main/kotlin")
        }
    }
}

fun KotlinMultiplatformExtension.sourceSets(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)
}