package com.belsoft.db

import org.jetbrains.exposed.sql.*

object StarWarsFilms: Table() {
    val id = integer("id").autoIncrement()
    val name: Column<String?> = varchar("name", 50).nullable()
    val director: Column<String?> = varchar("director", 50).nullable()

    override val primaryKey = PrimaryKey(id)
}

data class StarWarsFilm(
    val id: Int? = null,
    val name: String? = null,
    val director: String? = null
)

fun Transaction.initDSLStartWarsDb() {
//    SchemaUtils.drop(StarWarsFilms)
    SchemaUtils.create(StarWarsFilms)

//    //CREATE
//    val id = StarWarsFilms.insert {
//        it[name] = "The Last Jedi"
//        it[director] = "Rian Johnson"
//    } get(StarWarsFilms.id)
//
//    //READ
//    val allFilms = StarWarsFilms.
//        selectAll().map {
//            StarWarsFilm(
//                it[StarWarsFilms.id],
//                it[StarWarsFilms.name],
//                it[StarWarsFilms.director]
//            )
//        }
//    println("All films : $allFilms")
}