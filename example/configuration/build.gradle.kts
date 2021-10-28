import com.github.mejiomah17.konstantin.plugin.konstantinGeneratedSource
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty
import kotlin.reflect.full.declaredMemberProperties

plugins {
    kotlin("multiplatform") version "1.5.21"
//    kotlin("jvm") version "1.5.21"
    id("org.github.mejiomah17.konstantin") version "1.0"
}
group = "org.github.mejiomah17.konstantin.example"

kotlin {
    jvm()

    sourceSets{
        val jvmMain: KotlinSourceSet by getting{
            dependencies {
                implementation("org.github.mejiomah17.konstantin:api:1.0")
                implementation("org.github.mejiomah17.konstantin:configuration:1.0")
            }
        }
    }
    konstantinGeneratedSource()
}
tasks.create("sfdsa"){
    doLast{
        //[ext, kotlin, kotlinTestRegistry, base, defaultArtifacts, sourceSets, reporting, java, javaToolchains, kotlinScripting]
//        val x = project.extensions.extensionsSchema.elements.map{it.name}
        val x = project.extensions.getByName("kotlin") as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
        x.targets.getByName("jvm").compilations
//        val x = project.extensions.getByName("jvmMainClasses") as org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
//        val y: KotlinSourceSet = x.sourceSets.getByName("jvmMain")

        print(x)
    }
}
//dependencies {

//    implementation("ch.qos.logback:logback-classic:1.2.6")
//    testImplementation(kotlin("test"))
//}

//tasks.test {
//    useJUnitPlatform()
//}


//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "13"
//}
