package com.mindeurfou.database.game

import com.mindeurfou.model.game.Game
import com.mindeurfou.model.game.PostGameBody
import com.mindeurfou.model.game.PutGameBody

interface GameDao {
    fun getGameById(gameId: Int): Game?
    fun insertGame(postGame: PostGameBody): Int
    fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int)
    fun deleteGamePlayer(gameId: Int, playerId: Int): Boolean
    fun updateGame(game: Game): Game? // game access strategies ?
    fun deleteGame(gameId: Int): Boolean
    fun getGamesByTournamentId(tournamentId: Int, limit: Int = 20, offset: Int = 0): List<Game>
//    fun getGamesByTournamentName()
}