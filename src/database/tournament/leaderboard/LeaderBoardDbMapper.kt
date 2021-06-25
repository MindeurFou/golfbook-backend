package com.mindeurfou.database.tournament.leaderboard

import com.mindeurfou.database.player.PlayerTable
import org.jetbrains.exposed.sql.ResultRow

object LeaderBoardDbMapper {
    fun mapFromEntity(resultRow: ResultRow): Pair<String, Int> = Pair(resultRow[PlayerTable.name], resultRow[LeaderBoardTable.score])
}
