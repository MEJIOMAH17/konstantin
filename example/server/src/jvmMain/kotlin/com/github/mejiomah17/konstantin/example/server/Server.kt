package com.github.mejiomah17.konstantin.example.server

import com.github.mejiomah17.konstantin.backend.KonstantinServer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondFile
import io.ktor.response.respondOutputStream
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import org.github.mejiomah17.konstantin.example.configuration.TestConfig

fun main() {
    KonstantinServer(
        configuration = TestConfig().createConfiguration()
    ) {
        it.routing {
            get("/"){
                call.respondResource("index.html")
            }
            get("/styles.css"){
                call.respondResource("styles.css")
            }
            get("/server.js") {
                call.respondResource("server.js")
            }
            get("/server.js.map") {
                call.respondResource("server.js.map")
            }
        }
    }.start(wait = true)
}

private suspend fun ApplicationCall.respondResource(name:String){
    val resource = this.javaClass.classLoader.getResource(name)
    respondText(resource.readText(), contentType = ContentType("text",name.substringAfterLast(".")))
}