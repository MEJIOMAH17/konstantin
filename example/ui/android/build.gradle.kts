

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose") version "1.0.0-beta5"
}

group = "org.github.mejiomah17.konstantin"
version = "1.0"

repositories {
    google()
}

dependencies {
    api("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation(project(":ui:common")){
        exclude("org.jetbrains.compose")
    }
}
android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "org.github.mejiomah17.android"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    packagingOptions {
        exclude("META-INF/common.kotlin_module")
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}