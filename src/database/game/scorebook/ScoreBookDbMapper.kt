package com.mindeurfou.database.game.scorebook

import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.model.game.local.PlayerScore
import com.mindeurfou.model.game.local.ScoreDetails
import org.jetbrains.exposed.sql.ResultRow

object ScoreBookDbMapper {

    fun mapFromEntityToPlayerScore(resultRow: ResultRow, scoreDetails: List<ScoreDetails>) = PlayerScore(
        id = resultRow[PlayerScoreTable.id].value,
        name = resultRow[PlayerTable.username],
        scores = scoreDetails,
        scoreSum = "0",
        netSum = resultRow[PlayerScoreTable.netSum]
    )

    fun mapFromEntityToScoreDetails(resultRow: ResultRow) = ScoreDetails(
        score = resultRow[ScoreDetailsTable.score],
        net = resultRow[ScoreDetailsTable.net],
        holeNumber = resultRow[ScoreDetailsTable.holeNumber]
    )

}