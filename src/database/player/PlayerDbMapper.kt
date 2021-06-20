package com.mindeurfou.database.player

import com.mindeurfou.DatabaseMapper
import com.mindeurfou.model.player.Player
import org.jetbrains.exposed.sql.ResultRow

object PlayerDbMapper : DatabaseMapper<ResultRow, Player> {

    override fun mapFromEntity(resultRow: ResultRow): Player =
        Player(
            id = resultRow[PlayerTable.id].value,
            name = resultRow[PlayerTable.name],
            lastName = resultRow[PlayerTable.lastName],
            username = resultRow[PlayerTable.username],
            drawableResourceId = resultRow[PlayerTable.drawableResourceId]
        )
}