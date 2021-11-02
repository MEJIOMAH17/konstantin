import com.github.mejiomah17.konstantin.plugin.useKonstantinGeneratedSource
import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform") version "1.5.21"
    id("org.github.mejiomah17.konstantin") version "0.1.0"
    id("org.jetbrains.compose") version "1.0.0-alpha3"
    `maven-publish`
}


kotlin {
    jvm()

    dependencies {
        commonMainImplementation("org.github.mejiomah17.konstantin:api:0.1.0")
        commonMainImplementation("org.github.mejiomah17.konstantin:client:0.1.0")
        commonMainImplementation("org.github.mejiomah17.konstantin:ui-common:0.1.0")
    }
    useKonstantinGeneratedSource(project)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
            }
        }
    }
}

