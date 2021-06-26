package com.mindeurfou.database.course

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

object CourseTable : IntIdTable() {
    val name = varchar("name", 255)
    val numberOfHoles = integer("numberOfHoles")
    val par = integer("par")
    val gamesPlayed = integer("gamesPlayed")
    val createdAt = date("createdAt").default(LocalDate.now())
}