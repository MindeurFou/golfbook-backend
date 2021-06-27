package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.GBState
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object GameTable : IntIdTable() {
    val state = enumeration("state", GBState::class)
    val currentHole = integer("currentHole").default(1)
    val courseId = reference("courseId", CourseTable)
    val tournamentId = optReference("tournamentId", TournamentTable, onDelete = ReferenceOption.NO_ACTION)
}