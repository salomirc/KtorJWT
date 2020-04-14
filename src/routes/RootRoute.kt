package com.belsoft.routes

import com.belsoft.db.StarWarsFilm
import com.belsoft.db.StarWarsFilms
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
    get("/html"){
        val allFilms = transaction {
            StarWarsFilms.
            selectAll().
            map {
                StarWarsFilm(
                    it[StarWarsFilms.id],
                    it[StarWarsFilms.name],
                    it[StarWarsFilms.director]
                )
            }
        }
        call.respondHtml(){
            head {
                title { +"StarWars films list from Db" }
                styleLink("/static/style.css")
            }
            body {
                h1() { id="page_title"
                    +"Title"
                }
                for (film in allFilms){
                    p("subtitle") {
                        +"#${film.id} ${film.name} ${film.director}"
                    }
                }
            }
        }
    }
}