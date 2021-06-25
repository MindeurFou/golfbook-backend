package com.mindeurfou.database.tournament

import com.mindeurfou.model.tournament.Tournament
import org.jetbrains.exposed.sql.ResultRow

object TournamentDbMapper {
    fun mapFromEntity(resultRow: ResultRow, leaderBoard: Map<String, Int>) = Tournament(
        id = resultRow[TournamentTable.id].value,
        name = resultRow[TournamentTable.name],
        state = resultRow[TournamentTable.state],
        leaderBoard = leaderBoard
    )
}