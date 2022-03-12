package com.mindeurfou.database.game.scorebook

import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.player.PlayerTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

// 0 for a non played yet hole and null for a non present hole
object PlayerScoreTable : IntIdTable() {
    val netSum = varchar("netSum", length = 255)
    val gameId = reference(
        name = "gameId",
        foreign = GameTable,
        onDelete = ReferenceOption.CASCADE
    )
    val playerId = reference(
        name = "playerId",
        foreign = PlayerTable,
        onDelete = ReferenceOption.CASCADE
    )
}