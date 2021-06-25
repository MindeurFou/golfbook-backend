package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.game.GameState
import org.jetbrains.exposed.dao.id.IntIdTable

object GameTable : IntIdTable() {
    val state = enumeration("state", GameState::class)
    val currentHole = integer("currentHole").default(1)
    val courseId = reference("courseId", CourseTable)
    val tournamentId = reference("tournamentId", TournamentTable)
}