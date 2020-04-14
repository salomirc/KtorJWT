package com.belsoft.routes

import com.belsoft.db.StarWarsFilm
import com.belsoft.db.StarWarsFilms
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.startWarsFilms(){
    get("/getStartWarsFilms"){
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
        call.respond(allFilms)
    }

    post("/postStartWarsFilms"){
        val request = call.receive<StarWarsFilm>()
        val id = transaction {
            StarWarsFilms.insert {
                it[name] = request.name
                it[director] = request.director
            } get(StarWarsFilms.id)
        }
        call.respond(mapOf("id" to id))
    }
}