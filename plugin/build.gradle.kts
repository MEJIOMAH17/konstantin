import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
    implementation(project(":configuration"))
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.github.mejiomah17.konstantin.Version.kotlin}")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("konstantinPlugin") {
            id =  project.group.toString()
            implementationClass = "com.github.mejiomah17.konstantin.plugin.RegistryGeneratorPlugin"
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

