import com.github.mejiomah17.konstantin.plugin.useKonstantinGeneratedSource
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.github.mejiomah17.konstantin")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


dependencies {
    implementation(project(":configuration"))
    implementation("org.github.mejiomah17.konstantin:backend:0.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.6")
}

kotlin{
    useKonstantinGeneratedSource(project)
}

tasks.test {
    useJUnitPlatform()
}

tasks{
    shadowJar {
        mergeServiceFiles()
        manifest {
            attributes(Pair("Main-Class", "com.github.mejiomah17.konstantin.example.server.ServerKt"))
        }
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}