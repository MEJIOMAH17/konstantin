
plugins {
    kotlin("jvm") version "1.5.21"
    id("org.github.mejiomah17.konstantin") version "1.0"
}
group = "org.github.mejiomah17.konstantin.example"

dependencies {
    implementation("org.github.mejiomah17.konstantin:api:1.0")
    implementation("org.github.mejiomah17.konstantin:configuration:1.0")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.create("adf"){
//    println(sourceSets.asMap.keys)
    println(sourceSets["main"].runtimeClasspath.asPath)
}

//tasks.withType<KotlinCompile>() {
//    kotlinOptions.jvmTarget = "13"
//}
