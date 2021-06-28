package com.mindeurfou.database.game

import com.mindeurfou.model.game.outgoing.Game
import org.jetbrains.exposed.sql.ResultRow

object GameDbMapper {

    fun mapFromEntity(resultRow: ResultRow, players: List<String>?) = Game(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        currentHole = resultRow[GameTable.currentHole],
        players = players
    )

}