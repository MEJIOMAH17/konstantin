
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${com.github.mejiomah17.konstantin.Version.kotlin}")
        classpath("com.android.tools.build:gradle:4.1.2")
    }
}


allprojects{
    group = "org.github.mejiomah17.konstantin"
    version = "0.1.0"
    repositories {
        mavenCentral()
        google()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
