package com.mindeurfou.service

import com.mindeurfou.utils.GBException
import com.mindeurfou.database.game.GameDao
import com.mindeurfou.database.game.GameDaoImpl
import com.mindeurfou.database.tournament.TournamentDao
import com.mindeurfou.database.tournament.TournamentDaoImpl
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.PutScoreBook
import com.mindeurfou.model.game.outgoing.GameDetails
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.ScoreBook

class GameService : ServiceNotification() {

	private val gameDao: GameDao = GameDaoImpl()
	private val tournamentDao: TournamentDao = TournamentDaoImpl()

	// CRUD classic methods

	fun getGame(gameId: Int): GameDetails {
		return gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
	}

	fun addNewGame(postGame: PostGameBody) : GameDetails {

		postGame.tournamentId?.let {
			val tournamentDetails = tournamentDao.getTournamentById(it) ?: throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)
            if (tournamentDetails.state == GBState.DONE) throw GBException(GBException.TOURNAMENT_DONE_MESSAGE)
		}

		val gameId = gameDao.insertGame(postGame)
		return gameDao.getGameById(gameId)!!
	}

	fun updateGame(putGame: PutGameBody): GameDetails {
		val gameDetails = gameDao.getGameById(putGame.id) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)

		when (putGame.state) {
			GBState.PENDING -> {
				if (gameDetails.state == GBState.WAITING) {
					// process change to pending
					// (notify observers)
				} else if (gameDetails.state == GBState.DONE) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.WAITING -> {
				if (gameDetails.state == GBState.PENDING) {
					// rollback to waiting
				} else if (gameDetails.state == GBState.DONE) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.DONE -> {
				if (gameDetails.state == GBState.PENDING) {
					// check context
				} else if (gameDetails.state == GBState.WAITING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
		}

		return gameDao.updateGame(putGame)
	}

	fun deleteGame(gameId: Int): Boolean = gameDao.deleteGame(gameId)

	fun getGameByTournamentId(
		tournamentId: Int,
		limit : Int = GET_GAMES_DEFAULT_SIZE,
		offset : Int = GET_GAMES_DEFAULT_OFFSET
	) = gameDao.getGamesByTournamentId(tournamentId, limit, offset)

	// in-game specific operations

	fun addGamePlayer(gameId: Int, playerId: Int) {
		val gameDetails = gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
        if (gameDetails.state != GBState.WAITING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		val playerInGame = gameDetails.players.any { it.id == playerId }
		val gameIsFull = gameDetails.players.size >= 4

		if (!playerInGame && !gameIsFull)
			gameDao.addGamePlayer(gameId, playerId)
	}

	fun deleteGamePlayer(gameId: Int, playerId: Int) {
		val gameDetails = gameDao.getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
		if (gameDetails.state != GBState.WAITING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		val playerInGame = gameDetails.players.any { it.id == playerId }

		if (playerInGame)
			gameDao.deleteGamePlayer(gameId, playerId)
	}

	fun updateScoreBook(putScoreBook: PutScoreBook): ScoreBook {
		val gameDetails = gameDao.getGameById(putScoreBook.gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
		if (gameDetails.state != GBState.PENDING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)

		return gameDao.updateScoreBook(putScoreBook)
	}

	fun getScoreBookByGameId(gameId: Int): ScoreBook {
		return gameDao.getScoreBookByGameId(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)
	}

	companion object {
		const val GET_GAMES_DEFAULT_SIZE = 10
		const val GET_GAMES_DEFAULT_OFFSET = 0
	}
}