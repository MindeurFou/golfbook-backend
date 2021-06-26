package com.mindeurfou.database

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerDbMapper
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.model.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerDaoTest : BaseDaoTest() {

    @Test
    fun `when creating user with correct information and user not taken, database is storing user`() {
        val playerDao: PlayerDao = PlayerDaoImpl(PlayerDbMapper)
        transaction {
            createSchema()
            val validPostPlayer = DbInstrumentation.givenAValidPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)
            print(playerId)
            val player = playerDao.getPlayerById(playerId)
            assertThat(player).isEqualTo(
                Player(
                    playerId,
                    validPostPlayer.name,
                    validPostPlayer.lastName,
                    validPostPlayer.username,
                    validPostPlayer.drawableResourceId
                )
            )

        }
    }



    override fun createSchema() {
        SchemaUtils.create(PlayerTable)
    }

    override fun dropSchema() {
        SchemaUtils.drop(PlayerTable)
    }
}