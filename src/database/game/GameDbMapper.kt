package com.mindeurfou.database.game

import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.player.outgoing.Player
import org.jetbrains.exposed.sql.ResultRow

object GameDbMapper {

    fun mapFromEntity(resultRow: ResultRow, players: List<Player>, courseName: String) = Game(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        name = resultRow[GameTable.name],
        scoringSystem = resultRow[GameTable.scoringSystem],
        courseName = courseName,
        players = players,
        createdAt = resultRow[GameTable.createdAt]
    )

}