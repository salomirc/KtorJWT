package com.belsoft.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.math.BigDecimal

object Products : Table() {
    val id = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val price: Column<BigDecimal> = decimal("price", 7, 2)
    val unit: Column<String> = varchar("unit", 50)
    val imageName: Column<String> = varchar("image_name", 50)
    val isEnabled: Column<Boolean> = bool("is_enabled")
    val details: Column<String?> = varchar("details", 200).nullable()

    override val primaryKey = PrimaryKey(Users.id)
}

data class Product(
    val id: Int? = null,
    val name: String,
    val price: BigDecimal,
    val unit: String,
    val imageName: String,
    val isEnabled: Boolean,
    val details: String?
)