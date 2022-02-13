package com.mindeurfou.service

import com.mindeurfou.utils.GBException
import com.mindeurfou.database.game.GameDao
import com.mindeurfou.database.game.GameDaoImpl
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.GameNetworkMapper
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.outgoing.GameDetailsNetworkEntity
import com.mindeurfou.model.game.outgoing.ScoreBook

class GameService : ServiceNotification() {

	private val gameDao: GameDao = GameDaoImpl()

	// CRUD classic methods

	fun getGame(gameId: Int): GameDetailsNetworkEntity {
		return gameDao.getGameById(gameId)?.let { GameNetworkMapper.toGameDetailsNetworkEntity(it) } ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
	}

	fun addNewGame(postGame: PostGameBody) : GameDetailsNetworkEntity {
		val gameId = gameDao.insertGame(postGame)
		return GameNetworkMapper.toGameDetailsNetworkEntity(gameDao.getGameById(gameId)!!)
	}

	fun updateGame(putGame: PutGameBody): GameDetailsNetworkEntity {
		val gameDetails = gameDao.getGameById(putGame.id) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)

		when (putGame.state) {
			GBState.PENDING -> {
				if (gameDetails.state == GBState.INIT) {
					// process change to pending
					// (notify observers)
				} else if (gameDetails.state == GBState.DONE) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.INIT -> {
				if (gameDetails.state == GBState.PENDING) {
					// rollback to waiting
				} else if (gameDetails.state == GBState.DONE) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.DONE -> {
				if (gameDetails.state == GBState.PENDING) {
					// check context
					// TODO update course.gamesPlayed
				} else if (gameDetails.state == GBState.INIT) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
		}

		return GameNetworkMapper.toGameDetailsNetworkEntity(gameDao.updateGame(putGame))
	}

	fun deleteGame(gameId: Int): Boolean = gameDao.deleteGame(gameId)

	// in-game specific operations

	fun addGamePlayer(gameId: Int, playerId: Int) : GameDetailsNetworkEntity {
		val gameDetails = gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
        if (gameDetails.state != GBState.INIT) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		val playerInGame = gameDetails.players.any { it.id == playerId }
		val gameIsFull = gameDetails.players.size >= 4

		return if (!playerInGame && !gameIsFull)
			GameNetworkMapper.toGameDetailsNetworkEntity(gameDao.addGamePlayer(gameId, playerId))
		else
			GameNetworkMapper.toGameDetailsNetworkEntity(gameDetails)
	}

	fun deleteGamePlayer(gameId: Int, playerId: Int) {
		val gameDetails = gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
		if (gameDetails.state != GBState.INIT) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		val playerInGame = gameDetails.players.any { it.id == playerId }

		if (playerInGame)
			gameDao.deleteGamePlayer(gameId, playerId)
	}

	fun updateScoreBook(gameId: Int, scoreBook: ScoreBook): ScoreBook {
		val gameDetails = gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
		if (gameDetails.state != GBState.PENDING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		return gameDao.updateScoreBook(scoreBook)
	}

	fun getScoreBookByGameId(gameId: Int): ScoreBook {
		return gameDao.getScoreBookByGameId(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
	}

	fun getGamesByPlayerId(playerId: Int): List<Game>? =
		gameDao.getGamesByPlayerId(playerId).ifEmpty { null }

	fun getGamesByState(state: String): List<Game>? {
		return GBState.toState(state)?.let {
			gameDao.getGamesByState(it).ifEmpty { null }
		}
	}


    companion object {
		const val GET_GAMES_DEFAULT_SIZE = 10
		const val GET_GAMES_DEFAULT_OFFSET = 0
	}
}