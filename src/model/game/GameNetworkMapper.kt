package com.mindeurfou.model.game

import com.mindeurfou.model.game.GameNetworkMapper.toNetworkEntity
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.local.ScoreBook
import com.mindeurfou.model.game.local.ScoreDetails
import com.mindeurfou.model.game.outgoing.GameDetailsNetworkEntity
import com.mindeurfou.model.game.outgoing.PlayerScoreNetworkEntity
import com.mindeurfou.model.game.outgoing.ScoreBookNetworkEntity
import com.mindeurfou.model.game.outgoing.ScoreDetailsNetworkEntity

object GameNetworkMapper {

    fun toGameDetailsNetworkEntity(gameDetails: GameDetails) : GameDetailsNetworkEntity =
        GameDetailsNetworkEntity(
            gameDetails.id,
            gameDetails.name,
            gameDetails.state,
            gameDetails.date,
            gameDetails.scoringSystem,
            gameDetails.courseName,
            gameDetails.par,
            gameDetails.players,
            gameDetails.scoreBook.toNetworkEntity()
        )

    fun ScoreBook.toNetworkEntity() = ScoreBookNetworkEntity(
        playerScores = playerScores.map {
            PlayerScoreNetworkEntity(
                name = it.name,
                scores = it.scores.toNetworkEntity(),
                scoreSum = it.scoreSum,
                netSum = it.netSum
            )
        }
    )

    private fun List<ScoreDetails>.toNetworkEntity() =
        map { scoreDetails ->
            ScoreDetailsNetworkEntity(
                score = scoreDetails.score,
                net = scoreDetails.net
            )
        }
}