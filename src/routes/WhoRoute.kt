package com.belsoft.routes

import io.ktor.application.call
import io.ktor.auth.authentication
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.who(){

    route("/who") {
        handle {
            val principal = call.authentication.principal<JWTPrincipal>()
            val subjectString = principal!!.payload.subject.removePrefix("auth0|")
            call.respondText("Success, $subjectString")
        }
    }
}