package com.mindeurfou.database.game

import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.ScoreBook

interface GameDao {
    fun getGameById(gameId: Int): GameDetails?
    fun insertGame(postGame: PostGameBody): Int
    fun updateGame(putGame: PutGameBody): GameDetails
    fun deleteGame(gameId: Int): Boolean
    fun getGamesByPlayerId(playerId: Int, limit: Int = 20, offset: Int = 0): List<Game>
    fun getGamesByState(state: GBState): List<Game>

    // scoreBook specific operations
    fun addGamePlayer(gameId: Int, playerId: Int): GameDetails
    fun deleteGamePlayer(gameId: Int, playerId: Int): GameDetails?
    fun updateScoreBook(scoreBook: ScoreBook): ScoreBook
    fun getScoreBookByGameId(gameId: Int): ScoreBook?
}