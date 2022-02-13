package database

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.PasswordManager
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerDaoTest : BaseDaoTest() {

    private val playerDao: PlayerDao = PlayerDaoImpl()
    private val passwordManager: PasswordManager = mockk()

    @BeforeEach
    override fun setup() {
        super.setup()
        koinModules = module {
            single { passwordManager }
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(passwordManager)
    }

    @Test
    fun `insert and retrieve player`() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
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
                    validPostPlayer.avatarId,
                    true
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
    fun `update player`() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val unValidPutPlayer = PutPlayerBody(4, "tanguy", "Pouriel", "Toug", 1)
            assertThrows(GBException::class.java) {
                playerDao.updatePlayer(unValidPutPlayer)
            }

            val validPostPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)
            val validPutPlayer = PutPlayerBody(playerId, "Tanguy", "Pouriel", "Toug", 1)
            val updatedPlayer = playerDao.updatePlayer(validPutPlayer)

            assertThat(updatedPlayer).isEqualTo(
                Player(
                    playerId,
                    validPutPlayer.name,
                    validPutPlayer.lastName,
                    validPutPlayer.username,
                    validPutPlayer.avatarId,
                    true
                )
            )
        }
    }

    @Test
    fun deletePlayer() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
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
    fun getPlayerByUsername() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
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
            assertEquals(player.avatarId, validPlayer.avatarId)
            assertEquals(player.realUser, validPlayer.realUser)
        }
    }

    override fun createSchema() {
        SchemaUtils.create(PlayerTable)
    }

    override fun dropSchema() {
        SchemaUtils.drop(PlayerTable)
    }
}