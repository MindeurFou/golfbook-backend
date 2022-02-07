package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.course.CourseTable.default
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.outgoing.ScoringSystem
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.LocalDate

object GameTable : IntIdTable() {
    val state = enumeration("state", GBState::class)
    val name = varchar("name", 255)
    val courseId = reference("courseId", CourseTable)
    val tournamentId = optReference("tournamentId", TournamentTable, onDelete = ReferenceOption.NO_ACTION)
    val createdAt = date("createdAt").default(LocalDate.now())
    val scoringSystem = enumeration("scoringSystem", ScoringSystem::class)
}