package com.mindeurfou.database.player

import com.mindeurfou.utils.GBException
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.utils.PasswordManager
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerDaoImpl : PlayerDao, KoinComponent {

    private val passwordManager by inject<PasswordManager>()

    override fun getPlayerById(playerId: Int): Player? = transaction {
        PlayerTable.select {
            (PlayerTable.id eq playerId)
        }.mapNotNull {
            PlayerDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }

    override fun getPlayerPassword(playerId: Int): String? = transaction {
        PlayerTable.select {
            (PlayerTable.id eq playerId)
        }.mapNotNull {
            it[PlayerTable.password]
        }.singleOrNull()
    }

    override fun getPlayers(filters: Map<String, String>?, limit: Int, offset: Int) = transaction {
        PlayerTable.selectAll()
            .limit(limit, offset.toLong())
            .orderBy(PlayerTable.id to SortOrder.DESC)
            .mapNotNull { PlayerDbMapper.mapFromEntity(it) }
    }


    override fun insertPlayer(postPlayer: PostPlayerBody): Int = transaction {
        PlayerTable.insertAndGetId {
            it[name] = postPlayer.name
            it[lastName] = postPlayer.lastName
            it[username] = postPlayer.username
            it[password] = passwordManager.encryptPassword(postPlayer.password)
            it[avatarId] = postPlayer.avatarId
            it[realUser] = postPlayer.realUser
        }.value
    }

    override fun updatePlayer(putPlayer: PutPlayerBody): Player {
        transaction {
            val updatedColumns = PlayerTable.update( {PlayerTable.id eq putPlayer.id} ) {
                it[name] = putPlayer.name
                it[lastName] = putPlayer.lastName
                it[username] = putPlayer.username
                it[avatarId] = putPlayer.avatarId
            }
            if (updatedColumns == 0) throw GBException(GBException.PLAYER_NOT_FIND_MESSAGE)
        }
        return getPlayerById(putPlayer.id)!!
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