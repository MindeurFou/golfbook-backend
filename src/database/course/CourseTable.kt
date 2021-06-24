package com.mindeurfou.database.course

import org.jetbrains.exposed.dao.id.IntIdTable

object CourseTable : IntIdTable() {
    val name = varchar("name", 255)
    val numberOfHoles = integer("numberOfHoles")
    val par = integer("par")
    val gamesPlayed = integer("gamesPlayed") // TODO
    val createdAt = integer("createdAt") // TODO
}