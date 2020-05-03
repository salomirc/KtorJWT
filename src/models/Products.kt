package com.belsoft.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.math.BigDecimal

object Products : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
    val unit: Column<String> = varchar("unit", 50)
    val price: Column<BigDecimal> = decimal("price", 7, 2)
    val imageName: Column<String> = varchar("image_name", 50)
    val isEnabled: Column<Boolean> = bool("is_enabled")
}