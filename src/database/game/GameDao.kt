package com.mindeurfou.database.game

import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.PostGameBody
import com.mindeurfou.model.game.outgoing.GameDetails

interface GameDao {
    fun getGameById(gameId: Int): GameDetails?
    fun insertGame(postGame: PostGameBody): Int
    fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int)
    fun deleteGamePlayer(gameId: Int, playerId: Int): Boolean
    fun updateGame(gameDetails: GameDetails): GameDetails? // game access strategies ?
    fun deleteGame(gameId: Int): Boolean
    fun getGamesByTournamentId(tournamentId: Int, limit: Int = 20, offset: Int = 0): List<Game>
//    fun getGamesByTournamentName()
}