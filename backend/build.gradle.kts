import com.github.mejiomah17.konstantin.Library
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}


dependencies {
    api(project(":api"))
    api(project(":configuration"))
    api(Library.kotlinCoroutines)
    implementation(Library.ktorServerNetty)
    implementation(Library.ktorServerWebsockets)
    implementation(Library.kotlinSerialization)
    implementation(Library.logback)
    testImplementation(project(":client"))
    testImplementation(Library.junit)
    testImplementation(Library.ktorClientCio)
    testImplementation(Library.ktorClientWebsockets)
    testImplementation(Library.kotest)

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
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
