package com.belsoft.routes

import com.belsoft.models.Product
import com.belsoft.models.Products
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.products(){
    get("/getProducts"){
        val products = transaction {
            Products.selectAll().map {
                Product(
                    it[Products.id],
                    it[Products.name],
                    it[Products.price],
                    it[Products.unit],
                    it[Products.imageName],
                    it[Products.isEnabled],
                    it[Products.details]
                )
            }
        }

        call.respond(products)
    }
}