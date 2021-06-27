package com.mindeurfou.database.tournament

import com.mindeurfou.model.GBState
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

object TournamentTable : IntIdTable() {
    val name = varchar("name", 255)
    val state = enumeration("state", GBState::class)
    val createdAt = date("createdAt").default(LocalDate.now())
}