package com.github.mejiomah17.konstantin.example.server

import com.github.mejiomah17.konstantin.backend.KonstantinServer
import io.ktor.application.call
import io.ktor.response.respondFile
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File
import org.github.mejiomah17.konstantin.example.configuration.TestConfig

fun main() {
    KonstantinServer(
        configuration = TestConfig().createConfiguration()
    ){
        it.routing{
            get("/konstantin-web.js"){
                call.respondFile(File("C:\\Users\\Mark\\IdeaProjects\\konstantin2\\example\\ui\\web\\build\\compileSync\\main\\developmentExecutable\\kotlin\\konstantin-web.js"))
            }
        }
    }.start(wait = true)
}