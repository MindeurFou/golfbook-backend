package com.mindeurfou.model.game

import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.GameDetailsNetworkEntity

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
            gameDetails.scoreBook
        )
}