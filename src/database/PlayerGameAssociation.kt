package com.mindeurfou.database

import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.player.PlayerTable
import org.jetbrains.exposed.sql.Table

object PlayerGameAssociation : Table(){
    val playerId = reference("playerId", PlayerTable)
    val gameId = reference("gameId", GameTable)
    override val primaryKey = PrimaryKey(playerId, gameId, name = "id", )
}