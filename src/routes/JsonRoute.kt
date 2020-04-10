package com.belsoft.routes

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post

data class Response(val status: String)

data class Request (val id : String,
                    val quantity: Int,
                    val isTrue: Boolean)

fun Route.json(){

    get("/json") {
        call.respond(Response(status = "OK"))
    }

    post("/json"){
        val request = call.receive<Request>()
        call.respond(request)
    }
}