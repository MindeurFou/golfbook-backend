package com.mindeurfou.service

import com.mindeurfou.utils.GBException
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl

class PlayerService {

	private val playerDao : PlayerDao = PlayerDaoImpl()

	fun addNewPlayer(postPlayer: PostPlayerBody) : Player {
		val playerByName = playerDao.getPlayerByUsername(postPlayer.username)  
		playerByName?.let { throw GBException(GBException.USERNAME_ALREADY_TAKEN_MESSAGE) }

		val playerId = playerDao.insertPlayer(postPlayer)

		return playerDao.getPlayerById(playerId)!!
	}

	fun getPlayer(playerId: Int): Player {
		return playerDao.getPlayerById(playerId) ?: throw GBException(GBException.PLAYER_NOT_FIND_MESSAGE)
	}

	fun updatePlayer(putPlayer: PutPlayerBody): Player {
		return playerDao.updatePlayer(putPlayer)
	} 

	fun deletePlayer(playerId: Int): Boolean = playerDao.deletePlayer(playerId)

	fun getPlayerByUsername(username: String): Player? = playerDao.getPlayerByUsername(username)

	fun getPlayerPassword(playerId: Int): String? = playerDao.getPlayerPassword(playerId)

	fun getPlayers(filters: Map<String, String>? = null, limit: Int?, offset: Int?): List<Player> {
        val actualLimit = limit ?: GET_PLAYER_LIST_DEFAULT_LIMIT
		val actualOffset = offset ?: GET_PLAYER_LIST_DEFAULT_OFFSET
		return playerDao.getPlayers(filters, actualLimit, actualOffset)
	}


	companion object {
		const val GET_PLAYER_LIST_DEFAULT_LIMIT = 20
		const val GET_PLAYER_LIST_DEFAULT_OFFSET = 0
	}
}