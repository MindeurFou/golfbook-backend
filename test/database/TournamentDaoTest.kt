package database

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentDao
import com.mindeurfou.database.tournament.TournamentDaoImpl
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.database.tournament.leaderboard.LeaderBoardTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.tournament.PutLeaderBoardBody
import com.mindeurfou.model.tournament.incoming.PutTournamentBody
import com.mindeurfou.model.tournament.outgoing.TournamentDetails
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.PasswordManager
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.dsl.module
import java.time.LocalDate
import kotlin.test.assertEquals

class TournamentDaoTest : BaseDaoTest(){

    private val tournamentDao: TournamentDao = TournamentDaoImpl()
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
        io.mockk.clearMocks(passwordManager)
    }

    @Test
    fun insertTournament() {
        transaction {
            createSchema()
            val postTournament = PostTournamentBody("tournoi du sale")
            val tournamentId = tournamentDao.insertTournament(postTournament)

            var tournament = tournamentDao.getTournamentById(2)
            assertEquals(null, tournament)

            tournament = tournamentDao.getTournamentById(tournamentId)
            assertThat(tournament).isEqualTo(
                TournamentDetails(
                    tournamentId,
                    "tournoi du sale",
                    GBState.WAITING,
                    createdAt = LocalDate.now()
                )
            )
        }
    }

    @Test
    fun updateTournament() {
        transaction {
            createSchema()
            assertThrows(GBException::class.java) {
                tournamentDao.updateTournament(
                    PutTournamentBody(
                        1,
                        "testName",
                        GBState.WAITING
                    )
                )
            }

            val validPostTournamentBody = PostTournamentBody("tournoi du sale")
            val tournamentId = tournamentDao.insertTournament(validPostTournamentBody)

            val validPutTournamentBody = PutTournamentBody(
                1,
                "testName",
                GBState.WAITING
            )
            val tournamentDetails = tournamentDao.updateTournament(validPutTournamentBody)
            assertThat(tournamentDetails).isEqualTo(
                TournamentDetails(
                    tournamentId,
                    validPutTournamentBody.name!!,
                    validPutTournamentBody.state!!,
                    null,
                    LocalDate.now()
                )
            )
        }
    }

    @Test
    fun deleteTournament() {
        transaction {
            createSchema()
            val postTournament = PostTournamentBody("tournoi du sale")
            val tournamentId = tournamentDao.insertTournament(postTournament)

            var result = tournamentDao.deleteTournament(3)
            assertEquals(false, result)

            result = tournamentDao.deleteTournament(tournamentId)
            assertEquals(true, result)
            val tournament = tournamentDao.getTournamentById(tournamentId)
            assertEquals(null, tournament)
        }
    }

    //test getTournaments ?

    @Test
    fun addTournamentPlayer() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val validPostTournamentBody = PostTournamentBody("tournoi du sale")
            val tournamentId = tournamentDao.insertTournament(validPostTournamentBody)

            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()

            assertThrows(GBException::class.java) {
                tournamentDao.addTournamentPlayer(tournamentId, 1)
            }

            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            val leaderBoard = tournamentDao.addTournamentPlayer(tournamentId, playerId)
            assertThat(leaderBoard).isEqualTo(mapOf(validPostPlayerBody.username to 0))

            assertThrows(GBException::class.java) {
                tournamentDao.addTournamentPlayer(tournamentId, playerId)
            }
        }
    }

    @Test
    fun deleteTournamentPlayer() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            
            val tournamentId = tournamentDao.insertTournament(PostTournamentBody("tournoi du sale"))

            assertThrows(GBException::class.java) {
                tournamentDao.deleteTournamentPlayer(tournamentId, 1)
            }    

            val validPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPlayer)

            tournamentDao.addTournamentPlayer(tournamentId, playerId)
            val leaderBoard = tournamentDao.deleteTournamentPlayer(tournamentId, playerId)
            assertEquals(null, leaderBoard)
        }
    }


    @Test
    fun updateTournamentLeaderBoard() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val unValidPutLeaderBoardBody = PutLeaderBoardBody(1, mapOf("name" to 3))
            assertThrows (GBException::class.java) {
                tournamentDao.updateTournamentLeaderBoard(unValidPutLeaderBoardBody)
            }

            val validPostTournamentBody = PostTournamentBody("tournoi du sale")
            val tournamentId = tournamentDao.insertTournament(validPostTournamentBody)

            assertThrows(GBException::class.java) {
                val validPutLeaderBoardBody = PutLeaderBoardBody(tournamentId, mapOf("user" to 4))
                tournamentDao.updateTournamentLeaderBoard(validPutLeaderBoardBody)
            }

            val validPostPlayer = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayer)

            assertThrows(GBException::class.java) {
                tournamentDao.updateTournamentLeaderBoard(PutLeaderBoardBody(tournamentId, mapOf(validPostPlayer.username to 4)))
            }

            tournamentDao.addTournamentPlayer(tournamentId, playerId)
            val leaderBoard = tournamentDao.updateTournamentLeaderBoard(PutLeaderBoardBody(tournamentId, mapOf(validPostPlayer.username to 3)))

            assertThat(leaderBoard).isEqualTo(
                mapOf(validPostPlayer.username to 3)
            )
        }

    }

    override fun createSchema() {
        SchemaUtils.create(TournamentTable)
        SchemaUtils.create(LeaderBoardTable)
        SchemaUtils.create(PlayerTable)
    }

    override fun dropSchema() {
        SchemaUtils.drop(TournamentTable)
        SchemaUtils.drop(LeaderBoardTable)
        SchemaUtils.drop(PlayerTable)
    }
}