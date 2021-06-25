package com.mindeurfou.database.game

import com.mindeurfou.DatabaseMapper
import com.mindeurfou.model.game.Game
import org.jetbrains.exposed.sql.ResultRow

object GameDbMapper {

    fun mapFromEntity(resultRow: ResultRow, scoreBook : Map<String, List<Int?>>) = Game(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        currentHole = resultRow[GameTable.currentHole],
        scoreBook = scoreBook,
    )

}