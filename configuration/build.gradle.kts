import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(project(":api"))
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
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
