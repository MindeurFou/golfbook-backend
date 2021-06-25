package com.mindeurfou.database.tournament

import com.mindeurfou.model.tournament.Tournament
import org.jetbrains.exposed.sql.ResultRow

object TournamentDbMapper {
    fun mapFromEntity(resultRow: ResultRow) = Tournament(
        id = resultRow[TournamentTable.id].value,
        name = resultRow[TournamentTable.name],
        state = resultRow[TournamentTable.state]
    )
}