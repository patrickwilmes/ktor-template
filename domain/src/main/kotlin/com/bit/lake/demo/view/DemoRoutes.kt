package com.bit.lake.demo.view

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.installDemoRoutes() {
    routing {
        route("/demo") {
            get {
                call.respondText("Hello from demo")
            }
        }
    }
}
