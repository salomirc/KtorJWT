package com.belsoft.routes

import com.belsoft.db.User
import com.belsoft.db.Users
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.principal
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.login(token: String){
    get("/login") {
        val principal = call.principal<UserIdPrincipal>()!!
        val users = transaction {
            Users.
            select { Users.username eq principal.name }.
            map {
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
        }
        call.respond(users[0].apply {
            this.token = token
        })
    }
}