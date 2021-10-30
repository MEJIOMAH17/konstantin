package com.github.mejiomah17.konstantin

object Version {
    val kotlin = "1.5.21"
    val ktor = "1.6.4"

}

object Library {
    val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
    val ktorServerNetty = "io.ktor:ktor-server-netty:${Version.ktor}"
    val ktorServerWebsockets = "io.ktor:ktor-websockets:${Version.ktor}"
    val ktorClientWebsockets = "io.ktor:ktor-client-websockets:${Version.ktor}"
    val ktorClientCio = "io.ktor:ktor-client-cio:${Version.ktor}"
    val logback = "ch.qos.logback:logback-classic:1.2.6"


    val kotest = "io.kotest:kotest-assertions-core-jvm:4.6.3"
}