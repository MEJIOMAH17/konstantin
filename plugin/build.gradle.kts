import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
//    id ("com.github.johnrengelman.shadow") version "7.1.0"

}
dependencies {
    implementation(project(":api"))
    implementation(project(":configuration"))
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("greetingsPlugin") {
            id =  project.group.toString()
            displayName = "<short displayable name for plugin>"
            description = "<Good human-readable description of what your plugin is about>"
            implementationClass = "com.github.mejiomah17.konstantin.plugin.RegistryGeneratorPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

