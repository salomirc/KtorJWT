package com.belsoft.routes

import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.principal
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.login(token: String){
    get("/login") {
        val principal = call.principal<UserIdPrincipal>()!!
        call.respondText("Hello ${principal.name} the token is : $token")
    }
}