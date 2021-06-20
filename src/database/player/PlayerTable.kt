package com.mindeurfou.database.player

import org.jetbrains.exposed.dao.id.IntIdTable

object PlayerTable : IntIdTable() {
    val name = varchar("name", 255)
    val lastName = varchar("lastName", 255)
    val username = varchar("username", 255)
    val drawableResourceId = integer("drawableResourceId")
}