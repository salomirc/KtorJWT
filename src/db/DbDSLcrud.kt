package com.belsoft.db

import com.belsoft.db.Cities.autoIncrement
import org.jetbrains.exposed.sql.*

object StarWarsFilms: Table() {
    val id = integer("id").autoIncrement()
    val name: Column<String?> = varchar("name", 50).nullable()
    val director: Column<String?> = varchar("director", 50).nullable()

    override val primaryKey = PrimaryKey(Users.id)
}

data class StarWarsFilm(
    val name: String? = null,
    val director: String? = null
)

data class IdResponse(
    val id: Int
)

fun Transaction.initDSLStartWarsDb() {
//    SchemaUtils.drop(StarWarsFilms)
    SchemaUtils.create(StarWarsFilms)

    //CREATE
    val id = StarWarsFilms.insert {
        it[name] = "The Last Jedi"
        it[director] = "Rian Johnson"
    } get(StarWarsFilms.id)

    //READ
    val allFilms = StarWarsFilms.
        selectAll().map {
            StarWarsFilm(
                it[StarWarsFilms.name],
                it[StarWarsFilms.director]
            )
        }
    println("All films : $allFilms")
}