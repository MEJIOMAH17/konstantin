plugins {
    kotlin("jvm")
    id("org.github.mejiomah17.konstantin")
}
group = "org.github.mejiomah17.konstantin.example"

konstantin {
    configurationClass.set("org.github.mejiomah17.konstantin.example.configuration.TestConfig")
}

dependencies {
    implementation("org.github.mejiomah17.konstantin:api:0.1.0")
    implementation("org.github.mejiomah17.konstantin:configuration:0.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.6")
}
