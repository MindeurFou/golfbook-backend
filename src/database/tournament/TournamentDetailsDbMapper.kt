package com.mindeurfou.database.tournament

import com.mindeurfou.model.tournament.TournamentDetails
import org.jetbrains.exposed.sql.ResultRow

object TournamentDetailsDbMapper {
    fun mapFromEntity(resultRow: ResultRow, leaderBoard: Map<String, Int>) = TournamentDetails(
        id = resultRow[TournamentTable.id].value,
        name = resultRow[TournamentTable.name],
        state = resultRow[TournamentTable.state],
        leaderBoard = leaderBoard
    )
}