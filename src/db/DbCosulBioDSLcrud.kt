package com.belsoft.db

import org.jetbrains.exposed.sql.*

object Users: Table() {
    val id = integer("id").autoIncrement()
    val username: Column<String> = varchar("username", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 50).uniqueIndex()
    val firstName: Column<String> = varchar("first_name", 50)
    val lastName: Column<String> = varchar("last_name", 50)
    val email: Column<String> = varchar("email", 50)
    val isAdmin: Column<Boolean> = bool("is_admin")
    val token: Column<String?> = varchar("token", 200).nullable()

    override val primaryKey = PrimaryKey(id)
}

data class User(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isAdmin: Boolean,
    var token: String?
)

fun Transaction.initDSLCosulBioDb() {
//    SchemaUtils.drop(Users)
    SchemaUtils.create(Users)

    //CREATE
//    val id = Users.insert {
//        it[username] = "salox"
//        it[password] = "redhatslx"
//        it[firstName] = "Ciprian"
//        it[lastName] = "Salomir"
//        it[email] = "ciprian.salomir@gmail.com"
//        it[isAdmin] = false
//        it[token] = null
//    } get(Users.id)

    //READ
    val allUsers = Users.
        selectAll().map {
        User(
            it[Users.username],
            it[Users.password],
            it[Users.firstName],
            it[Users.lastName],
            it[Users.email],
            it[Users.isAdmin],
            it[Users.token]
        )
        }
    println("All films : $allUsers")
}