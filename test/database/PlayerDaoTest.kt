package com.mindeurfou.database

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.model.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerDaoTest : BaseDaoTest() {

    private val playerDao: PlayerDao = PlayerDaoImpl()

    @Test
    fun `insert and retrieve player`() {
        transaction {
            createSchema()
            val validPostPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)
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

    @Test
    fun `get non existing player`() {
        transaction {
            createSchema()
            val player = playerDao.getPlayerById(1)
            assertEquals(player, null)
        }
    }

    @Test
    fun `update player`() {
        transaction {
            createSchema()
            val unValidPutPlayer(4, "luffy91230", 34234253)
            var updatedPlayer = playerDao.updatePlayer(4, unValidPutPlayer)

            assertEquals(updatedPlayer, null)

            val validPostPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)
            updatedPlayer = playerDao.updatePlayer(playerId, validPutPlayer)

            assertThat(updatedPlayer).isEqualTo(
                Player(
                    playerId,
                    validPostPlayer.name,
                    validPostPlayer.lastName,
                    validPutPlayer.username,
                    validPutPlayer.drawableResourceId
                )
            )

            val player = playerDao.getPlayerById(playerId)
            assertEquals(player, updatedPlayer)
        }
    }

    @Test
    fun deletePlayer() {
        transaction {
            createSchema()
            var result = playerDao.deletePlayer(1)
            assertEquals(result, false)

            val validPostPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)
            result = playerDao.deletePlayer(playerId)
            assertEquals(result, true)
        }
    }

    @Test
    fun getPlayerByUsername() {
        transaction {
            createSchema()
            var player = playerDao.getPlayerByUsername("non present name")
            assertEquals(player, null)

            val validPlayer = DbInstrumentation.validPostPlayerBody()
            playerDao.insertPlayer(validPlayer)
            player = playerDao.getPlayerByUsername(validPlayer.username)

            player ?: throw IllegalStateException("Player shouldn't be null")

            assertEquals(player.name, validPlayer.name)
            assertEquals(player.lastName, validPlayer.lastName)
            assertEquals(player.username, validPlayer.username)
            assertEquals(player.drawableResourceId, validPlayer.drawableResourceId)
        }
    }

    override fun createSchema() {
        SchemaUtils.create(PlayerTable)
    }

    override fun dropSchema() {
        SchemaUtils.drop(PlayerTable)
    }
}