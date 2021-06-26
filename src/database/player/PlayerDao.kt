package com.mindeurfou.database.player

import com.mindeurfou.model.player.Player
import com.mindeurfou.model.player.PostPlayerBody
import com.mindeurfou.model.player.PutPlayerBody

interface PlayerDao {
    fun getPlayerById(playerId: Int): Player?
    fun insertPlayer(postPlayer: PostPlayerBody): Int
    fun updatePlayer(playerId: Int, putPlayer: PutPlayerBody): Player?
    fun deletePlayer(playerId: Int): Boolean
    fun getPlayerByUsername(username: String): Player?
}