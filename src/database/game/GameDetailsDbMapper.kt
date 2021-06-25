package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.model.game.outgoing.GameDetails
import org.jetbrains.exposed.sql.ResultRow

object GameDetailsDbMapper {
    fun mapFromEntity(resultRow: ResultRow, scoreBook : Map<String, List<Int?>>) = GameDetails(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        courseName = resultRow[CourseTable.name],
        currentHole = resultRow[GameTable.currentHole],
        scoreBook = scoreBook,
    )
}