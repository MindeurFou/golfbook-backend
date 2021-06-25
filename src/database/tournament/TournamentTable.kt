package com.mindeurfou.database.tournament

import com.mindeurfou.model.GBState
import org.jetbrains.exposed.dao.id.IntIdTable

object TournamentTable : IntIdTable() {
    val name = varchar("name", 255)
    val state = enumeration("state", GBState::class)
    val createdAt = integer("createdAt")
}