package com.mindeurfou.database.game

import com.mindeurfou.model.game.PutScoreBook
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.GameDetails

interface GameDao {
    fun getGameById(gameId: Int): GameDetails?
    fun insertGame(postGame: PostGameBody): Int?
    fun updateGame(putGame: PutGameBody): GameDetails?
    fun deleteGame(gameId: Int): Boolean
    fun getGamesByTournamentId(tournamentId: Int, limit: Int = 20, offset: Int = 0): List<Game>
//    fun getGamesByTournamentName()

    // scoreBook specific operations
    fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int): GameDetails?
    fun deleteGamePlayer(gameId: Int, playerId: Int): GameDetails?
    fun updateScoreBook(putScoreBook : PutScoreBook): Map<String, List<Int?>>?
}