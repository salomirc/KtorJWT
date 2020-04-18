package com.belsoft.routes

import com.belsoft.db.StarWarsFilm
import com.belsoft.db.StarWarsFilms
import com.belsoft.templates.crudCreateTableTemplate
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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

fun Routing.rootHTML(){
    get("/admin/StarWarsFilms"){
        val allFilms = transaction {
            StarWarsFilms.
            selectAll().
            map {
                mapOf(
                    "id" to it[StarWarsFilms.id].toString(),
                    "name" to it[StarWarsFilms.name].toString(),
                    "director" to it[StarWarsFilms.director].toString()
                )
            }
        }
        call.respondHtml(){
            crudCreateTableTemplate(
                "StarWars films list",
                "StarWarsFilms",
                allFilms
            )
        }
    }
}

