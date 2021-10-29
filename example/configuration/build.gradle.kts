plugins {
    kotlin("jvm") version "1.5.21"
    id("org.github.mejiomah17.konstantin") version "1.0"
}
group = "org.github.mejiomah17.konstantin.example"

konstantin {
    configurationClass.set("org.github.mejiomah17.konstantin.example.configuration.TestConfig")
}

dependencies {
    implementation("org.github.mejiomah17.konstantin:api:1.0")
    implementation("org.github.mejiomah17.konstantin:configuration:1.0")
    implementation("ch.qos.logback:logback-classic:1.2.6")
}
