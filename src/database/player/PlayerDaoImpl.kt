package com.mindeurfou.database.player

import com.mindeurfou.model.player.Player
import com.mindeurfou.model.player.PostPlayerBody
import com.mindeurfou.model.player.PutPlayerBody
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerDaoImpl : PlayerDao {

    override fun getPlayerById(playerId: Int): Player? = transaction {
        PlayerTable.select {
            (PlayerTable.id eq playerId)
        }.mapNotNull {
            PlayerDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }

    override fun insertPlayer(postPlayer: PostPlayerBody): Int = transaction {
        PlayerTable.insertAndGetId {
            it[name] = postPlayer.name
            it[lastName] = postPlayer.lastName
            it[username] = postPlayer.username
            it[drawableResourceId] = postPlayer.drawableResourceId
        }.value
    }

    override fun updatePlayer(playerId: Int, putPlayer: PutPlayerBody): Player? {
        transaction {
            PlayerTable.update( {PlayerTable.id eq playerId} ) {
                it[username] = putPlayer.username
                it[drawableResourceId] = putPlayer.drawableResourceId
            }
        }
        return getPlayerById(playerId)
    }

    override fun deletePlayer(playerId: Int) = transaction {
        PlayerTable.deleteWhere { (PlayerTable.id eq playerId) } > 0
    }

    override fun getPlayerByUsername(username: String): Player? = transaction {
        PlayerTable.select {
            PlayerTable.username eq username
        }.mapNotNull {
            PlayerDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }
}