import com.github.mejiomah17.konstantin.Library
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
    implementation(Library.kotlinCoroutines)
    implementation(Library.logger)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

val compileKotlin: KotlinCompile by tasks
