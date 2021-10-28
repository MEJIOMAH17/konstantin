import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `maven-publish`
}

val ktorVersion = "1.6.4"
dependencies {
    implementation(project(":api"))
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "13"
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
