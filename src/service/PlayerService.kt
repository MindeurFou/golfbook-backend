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
		playerByName?.let { throw GBException("This username is already taken") }

		val playerId = playerDao.insertPlayer(postPlayer)

		return playerDao.getPlayerById(playerId)!!
	}

	fun getPlayer(playerId: Int): Player {
		return playerDao.getPlayerById(playerId) ?: throw GBException("This player doesn't exist")
	}

	fun updatePlayer(putPlayer: PutPlayerBody): Player {
		return playerDao.updatePlayer(putPlayer) ?: throw GBException("This player doesn't exist")
	} 

	fun deletePlayer(playerId: Int): Boolean = playerDao.deletePlayer(playerId)

	fun getPlayerByUsername(username: String): Player? = playerDao.getPlayerByUsername(username)
}