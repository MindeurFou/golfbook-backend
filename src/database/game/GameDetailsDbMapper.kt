package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.model.game.outgoing.GameDetails
import com.mindeurfou.model.player.outgoing.Player
import org.jetbrains.exposed.sql.ResultRow

object GameDetailsDbMapper {
    fun mapFromEntity(resultRow: ResultRow, scoreBook : Map<String, List<Int?>>?, players: List<Player>) = GameDetails(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        courseName = resultRow[CourseTable.name],
        courseId = resultRow[CourseTable.id].value,
        currentHole = resultRow[GameTable.currentHole],
        players =  players,
        scoreBook = scoreBook,
    )
}