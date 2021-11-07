import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

kotlin{
    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets{
        val jvmMain by getting{
            dependencies {
                implementation(project(":configuration"))
                implementation("org.github.mejiomah17.konstantin:api:0.1.0")
                implementation("org.github.mejiomah17.konstantin:backend:0.1.0")
                implementation("org.github.mejiomah17.konstantin:configuration:0.1.0")
                implementation("io.ktor:ktor-server-netty:1.6.4")
                implementation("ch.qos.logback:logback-classic:1.2.6")
                //I do not need it, but gradle ask it. (TODO fix)
                implementation(compose.runtime)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("highlight.js", "10.7.2"))
//                implementation(project(":ui:common"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}
afterEvaluate {
    val jsBrowserDistribution by tasks.getting

    tasks.getByName<ProcessResources>("jvmProcessResources") {
        dependsOn(jsBrowserDistribution)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(jsBrowserDistribution)
    }
}
//tasks.test {
//    useJUnitPlatform()
//}
//
//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "1.8"
//}