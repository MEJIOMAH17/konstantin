

group = "org.github.mejiomah17.example"
version = "1.0"

allprojects{
    repositories {
        mavenCentral()
        google()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.github.mejiomah17.konstantin:org.github.mejiomah17.konstantin.gradle.plugin:0.1.0")
    }
}

