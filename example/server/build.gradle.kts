import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
}


dependencies {
    implementation(project(":configuration"))
    implementation("org.github.mejiomah17.konstantin:api:0.1.0")
    implementation("org.github.mejiomah17.konstantin:backend:0.1.0")
    implementation("org.github.mejiomah17.konstantin:configuration:0.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}