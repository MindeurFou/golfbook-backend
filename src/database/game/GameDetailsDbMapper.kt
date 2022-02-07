package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.ScoreBook
import com.mindeurfou.model.player.outgoing.Player
import org.jetbrains.exposed.sql.ResultRow

object GameDetailsDbMapper {
    fun mapFromEntity(resultRow: ResultRow, scoreBook: ScoreBook?, players: List<Player>, par: List<Int>) = GameDetails(
        id = resultRow[GameTable.id].value,
        state = resultRow[GameTable.state],
        courseName = resultRow[CourseTable.name],
        courseId = resultRow[CourseTable.id].value,
        players =  players,
        scoreBook = scoreBook!!,
        scoringSystem = resultRow[GameTable.scoringSystem],
        name = resultRow[GameTable.name],
        date = resultRow[GameTable.createdAt],
        par = par
    )
}