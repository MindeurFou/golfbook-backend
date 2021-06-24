package com.mindeurfou.database.player

import com.mindeurfou.model.player.Player
import com.mindeurfou.model.player.PostPlayerBody
import com.mindeurfou.model.player.PutPlayerBody
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerDaoImpl : PlayerDao, KoinComponent {

    private val playerTable: PlayerTable by inject()
    private val playerDbMapper: PlayerDbMapper by inject()

    override fun getPlayerById(playerId: Int): Player? = transaction {
        playerTable.select {
            (playerTable.id eq playerId)
        }.mapNotNull {
            playerDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }

    override fun insertPlayer(postPlayer: PostPlayerBody): Int = transaction {
        playerTable.insertAndGetId {
            it[name] = postPlayer.name
            it[lastName] = postPlayer.lastName
            it[username] = postPlayer.username
            it[drawableResourceId] = postPlayer.drawableResourceId
        }.value
    }

    override fun updateUser(playerId: Int, putPlayer: PutPlayerBody): Player? {
        transaction {
            playerTable.update( {playerTable.id eq playerId} ) {
                it[username] = putPlayer.username
                it[drawableResourceId] = putPlayer.drawableResourceId
            }
        }
        return getPlayerById(playerId)
    }

    override fun deleteUser(playerId: Int) = transaction {
        playerTable.deleteWhere { (playerTable.id eq playerId) } > 0
    }

    override fun getPlayerByUsername(username: String): Player? = transaction {
        playerTable.select {
            PlayerTable.username eq username
        }.mapNotNull {
            playerDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }
}