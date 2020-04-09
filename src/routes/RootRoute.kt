package com.belsoft.routes

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.root(){
    get("/"){
        call.respondText("Hello from Ktor and Kotlin with Jetty and AppEngine!", ContentType.Text.Plain)
    }
}

fun Routing.rootPost(){
    post("/"){
        val post = call.receive<String>()
        call.respondText(
            "Received \"$post\" from the post body (lenght = ${post.length})",
            ContentType.Text.Plain, status = HttpStatusCode.OK
        )
    }
}