package com.mindeurfou.database.tournament.leaderboard

import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object LeaderBoardTable : Table() {
    val score = integer("score").default(0)
    val playerId = reference("playerId", PlayerTable)
    val tournamentId = reference("tournamentId", TournamentTable)
    override val primaryKey = PrimaryKey(PlayerTable.id, TournamentTable.id, name = "id")
}