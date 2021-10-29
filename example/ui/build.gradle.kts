import com.github.mejiomah17.konstantin.plugin.useKonstantinGeneratedSource

plugins {
    kotlin("multiplatform") version "1.5.21"
    id("org.github.mejiomah17.konstantin") version "1.0"
    `maven-publish`
}


kotlin {
    jvm()
    dependencies {
        commonMainImplementation("org.github.mejiomah17.konstantin:api:1.0")
    }
    useKonstantinGeneratedSource(project)
}

