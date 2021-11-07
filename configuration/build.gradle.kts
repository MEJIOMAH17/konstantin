import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.mejiomah17.konstantin.Library

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
    kotlinOptions.jvmTarget = "1.8"
}

kotlin{
    publishing {
        repositories {
            mavenLocal()
        }
        publications {
            create<MavenPublication>("lib") {
                from(components["kotlin"])
            }
        }
    }
}
val compileKotlin: KotlinCompile by tasks
