package database

import com.mindeurfou.database.PlayerGameAssociation
import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.game.GameDao
import com.mindeurfou.database.game.GameDaoImpl
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.game.scorebook.ScoreBookTable
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.model.GBState
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.PlayerScore
import com.mindeurfou.model.game.outgoing.ScoreBook
import com.mindeurfou.model.game.outgoing.ScoringSystem
import com.mindeurfou.model.player.outgoing.Player
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

class GameDaoTest : BaseDaoTest(){

    private val gameDao: GameDao = GameDaoImpl()
    private val courseDao : CourseDao = CourseDaoImpl()
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
    fun `insert and retrieve game`() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)

            val validPostGameBody = PostGameBody(courseName = validCourseBody.name, name = "myGame", scoringSystem = ScoringSystem.STABLEFORD)
            val gameId = gameDao.insertGame(validPostGameBody)

            val game = gameDao.getGameById(gameId)

            assertThat(game).isEqualTo(
                GameDetails(
                    gameId,
                    name = validPostGameBody.name,
                    state = GBState.INIT,
                    date = LocalDate.now(),
                    scoringSystem = validPostGameBody.scoringSystem,
                    courseName = validCourseBody.name,
                    par = validCourseBody.holes,
                    players = listOf(),
                    courseId = courseId,
                    scoreBook = ScoreBook(listOf())
                )
            )

        }
    }


    @Test
    fun `try to get non existing game`() {
        transaction {
            createSchema()
            val gameDetails = gameDao.getGameById(1)
            assertEquals(gameDetails, null)
        }
    }


    @Test
    fun updateGame() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()

            assertThrows(GBException::class.java) {
                gameDao.updateGame(PutGameBody(1, GBState.INIT, 1))
            }

            val validPostCourse = DbInstrumentation.validPostCourseBody()
            val courseId = courseDao.insertCourse(validPostCourse)
            assertThrows(GBException::class.java) {
                gameDao.updateGame(PutGameBody(1, GBState.INIT, courseId))
            }

            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            playerDao.insertPlayer(validPostPlayerBody)
            val validPostGameBody = PostGameBody("test course", validPostCourse.name, ScoringSystem.STABLEFORD)
            val gameId = gameDao.insertGame(validPostGameBody)

            val gameDetails = gameDao.updateGame(PutGameBody(gameId, GBState.PENDING, courseId))
            assertThat(gameDetails).isEqualTo(
                GameDetails(
                    gameId,
                    validPostGameBody.name,
                    GBState.PENDING,
                    LocalDate.now(),
                    validPostGameBody.scoringSystem,
                    validPostCourse.name,
                    validPostCourse.holes,
                    listOf(),
                    courseId,
                    ScoreBook(listOf())
                )
            )
        }
    }

    @Test
    fun deleteGame() = withBaseTestApplication{
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            var result = gameDao.deleteGame(1)
            assertEquals(result, false)

            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val otherValidPostPlayer = DbInstrumentation.otherValidPostPlayerBody()

            courseDao.insertCourse(validCourseBody)
            playerDao.insertPlayer(validPostPlayerBody)
            playerDao.insertPlayer(otherValidPostPlayer)

            val validPostGameBody = DbInstrumentation.validPostGame(courseName = validCourseBody.name)
            val gameId = gameDao.insertGame(validPostGameBody)

            result = gameDao.deleteGame(gameId)
            assertEquals(result, true)

        }
    }

    @Test
    fun addGamePlayer() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val otherValidPostPlayer = DbInstrumentation.otherValidPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            val otherPlayerId = playerDao.insertPlayer(otherValidPostPlayer)

            val players = listOf(
                Player(
                    playerId,
                    validPostPlayerBody.name,
                    validPostPlayerBody.lastName,
                    validPostPlayerBody.username,
                    validPostPlayerBody.avatarId,
                    true
                ),
                Player(
                    otherPlayerId,
                    otherValidPostPlayer.name,
                    otherValidPostPlayer.lastName,
                    otherValidPostPlayer.username,
                    otherValidPostPlayer.avatarId,
                    true
                )
            )

            val validPostGameBody = DbInstrumentation.validPostGame(courseName = validCourseBody.name)
            val gameId = gameDao.insertGame(validPostGameBody)

            gameDao.addGamePlayer(gameId, playerId)

            val gameDetails = gameDao.addGamePlayer(gameId, otherPlayerId)

            val scoreBook = ScoreBook(
                listOf(
                    PlayerScore(
                        players[0].name,
                        listOf(),
                        "",
                        ""
                    ),
                    PlayerScore(
                        players[0].name,
                        listOf(),
                        "",
                        ""
                    ),
                )
            )

            assertThat(gameDetails).isEqualTo(
                GameDetails(
                    gameId,
                    validPostGameBody.name,
                    GBState.INIT,
                    LocalDate.now(),
                    validPostGameBody.scoringSystem,
                    validCourseBody.name,
                    validCourseBody.holes,
                    players,
                    courseId,
                    ScoreBook(listOf())
                )
            )
        }
    }

    @Test
fun deleteGamePlayer() = withBaseTestApplication {
        every { passwordManager.encryptPassword(any()) } returns "testPassword"
        transaction {
            createSchema()
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)

            val validPostGameBody = DbInstrumentation.validPostGame(courseName = validCourseBody.name)
            val gameId = gameDao.insertGame(validPostGameBody)

            gameDao.addGamePlayer(gameId, playerId)

            val gameDetails = gameDao.deleteGamePlayer(gameId, playerId)
            assertThat(gameDetails).isEqualTo( 
               GameDetails(
                   gameId,
                   validPostGameBody.name,
                   GBState.INIT,
                   LocalDate.now(),
                   validPostGameBody.scoringSystem,
                   validCourseBody.name,
                   validCourseBody.holes,
                   listOf(),
                   courseId,
                   ScoreBook(listOf())
               )
            )
        }
    }


    override fun dropSchema() {
        SchemaUtils.drop(GameTable)
        SchemaUtils.drop(CourseTable)
        SchemaUtils.drop(HoleTable)
        SchemaUtils.drop(ScoreBookTable)
        SchemaUtils.drop(PlayerGameAssociation)
    }

    override fun createSchema() {
        SchemaUtils.create(GameTable)
        SchemaUtils.create(CourseTable)
        SchemaUtils.create(HoleTable)
        SchemaUtils.create(ScoreBookTable)
        SchemaUtils.create(PlayerGameAssociation)
    }
}