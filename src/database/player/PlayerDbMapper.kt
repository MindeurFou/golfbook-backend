package com.mindeurfou.database.player

import com.mindeurfou.utils.DatabaseMapper
import com.mindeurfou.model.player.outgoing.Player
import org.jetbrains.exposed.sql.ResultRow

object PlayerDbMapper : DatabaseMapper<ResultRow, Player> {

    override fun mapFromEntity(resultRow: ResultRow): Player =
        Player(
            id = resultRow[PlayerTable.id].value,
            name = resultRow[PlayerTable.name],
            lastName = resultRow[PlayerTable.lastName],
            username = resultRow[PlayerTable.username],
            avatarId = resultRow[PlayerTable.avatarId],
            realUser = resultRow[PlayerTable.realUser]
        )
}