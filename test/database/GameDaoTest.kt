package com.mindeurfou.database

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.game.GameDao
import com.mindeurfou.database.game.GameDaoImpl
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.game.ScoreBookTable
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.tournament.TournamentDao
import com.mindeurfou.database.tournament.TournamentDaoImpl
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.outgoing.GameDetails
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.utils.GBException
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class GameDaoTest : BaseDaoTest(){

    private val gameDao: GameDao = GameDaoImpl()
    private val courseDao : CourseDao = CourseDaoImpl()
    private val playerDao: PlayerDao = PlayerDaoImpl()

    @Test
    fun `insert and retrieve game`() {
        transaction {
            createSchema()
            SchemaUtils.create(TournamentTable)
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)

            val validPostGameBody = PostGameBody(courseId = courseId)
            val gameId = gameDao.insertGame(validPostGameBody)

            val game = gameDao.getGameById(gameId)

            assertThat(game).isEqualTo(
                GameDetails(
                    gameId,
                    GBState.WAITING,
                    validCourseBody.name,
                    courseId,
                    1,
                    null
                )
            )

            assertThrows(GBException::class.java) {
                val otherValidPostGameBody = PostGameBody(courseId = courseId, tournamentId = 1)
                gameDao.insertGame(otherValidPostGameBody)
            }
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
    fun updateGame() {
        transaction {
            createSchema()

            assertThrows(GBException::class.java) {
                gameDao.updateGame(PutGameBody(1, GBState.WAITING, 1))
            }

            val validPostCourse = DbInstrumentation.validPostCourseBody()
            val courseId = courseDao.insertCourse(validPostCourse)
            assertThrows(GBException::class.java) {
                gameDao.updateGame(PutGameBody(1, GBState.WAITING, courseId))
            }

            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            val validPostGameBody = PostGameBody(courseId = courseId)
            val gameId = gameDao.insertGame(validPostGameBody)

            val gameDetails = gameDao.updateGame(PutGameBody(gameId, GBState.PENDING, courseId))
            assertThat(gameDetails).isEqualTo(
                GameDetails(
                    gameId,
                    GBState.PENDING,
                    validPostCourse.name,
                    courseId,
                    1,
                    null
                )
            )
        }
    }

    @Test
    fun deleteGame() {
        transaction {
            createSchema()
            var result = gameDao.deleteGame(1)
            assertEquals(result, false)

            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val otherValidPostPlayer = DbInstrumentation.otherValidPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            val otherPlayerId = playerDao.insertPlayer(otherValidPostPlayer)

            val validPostGameBody = PostGameBody(courseId = courseId)
            val gameId = gameDao.insertGame(validPostGameBody)

            result = gameDao.deleteGame(gameId)
            assertEquals(result, true)

            val course = courseDao.getCourseById(courseId)
            assertThat(course).isEqualTo(
                CourseDetails(
                    courseId,
                    validCourseBody.name,
                    validCourseBody.numberOfHOles,
                    validCourseBody.par,
                    0,
                    LocalDate.now(),
                    DbInstrumentation.listOfHoles()
                )
            )
            val player = playerDao.getPlayerById(playerId)
            assertThat(player).isEqualTo(
                Player(
                    playerId,
                    validPostPlayerBody.name,
                    validPostPlayerBody.lastName,
                    validPostPlayerBody.username,
                    validPostPlayerBody.drawableResourceId
                )
            )

            val otherPlayer = playerDao.getPlayerById(otherPlayerId)
            assertThat(otherPlayer).isEqualTo(
                Player(
                    otherPlayerId,
                    otherValidPostPlayer.name,
                    otherValidPostPlayer.lastName,
                    otherValidPostPlayer.username,
                    otherValidPostPlayer.drawableResourceId
                )
            )

        }
    }

    @Test
    fun getGameByTournamentId() {
        transaction {
            createSchema()
            SchemaUtils.create(TournamentTable)

            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val otherValidPostPlayer = DbInstrumentation.otherValidPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            playerDao.insertPlayer(otherValidPostPlayer)

            val tournamentId = TournamentDaoImpl().insertTournament(PostTournamentBody("Tournoi de ouf"))

            val validPostGameBody = PostGameBody(courseId = courseId, tournamentId = tournamentId)
            val unValidPostGameBody = PostGameBody(courseId = courseId, tournamentId = 2)

            val gameId = gameDao.insertGame(validPostGameBody)

            assertThrows(GBException::class.java) {
                gameDao.insertGame(unValidPostGameBody)
            }


            val games = gameDao.getGamesByTournamentId(tournamentId)
            assertEquals(1, games.size)
            assertThat(games[0]).isEqualTo(
                Game(
                    gameId,
                    GBState.WAITING,
                    1,
                    null
                )
            )
            SchemaUtils.drop(TournamentTable)
        }
    }

    @Test
    fun addGamePlayer() {
        transaction {
            createSchema()
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()
            val otherValidPostPlayer = DbInstrumentation.otherValidPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)
            val otherPlayerId = playerDao.insertPlayer(otherValidPostPlayer)

            val validPostGameBody = PostGameBody(courseId = courseId)
            val gameId = gameDao.insertGame(validPostGameBody)

            assertThrows(GBException::class.java) {
                gameDao.addGamePlayer(gameId, 4, courseId)
            }
            assertThrows(GBException::class.java) {
                gameDao.addGamePlayer(gameId, otherPlayerId, 5)
            }

            gameDao.addGamePlayer(gameId, playerId, courseId)

            val gameDetails = gameDao.addGamePlayer(gameId, otherPlayerId, courseId)

            val scoreBook = DbInstrumentation.initialScoreBook(validPostPlayerBody.username) + DbInstrumentation.initialScoreBook(otherValidPostPlayer.username)

            assertThat(gameDetails).isEqualTo(
                GameDetails(
                    gameId,
                    GBState.WAITING,
                    validCourseBody.name,
                    courseId,
                    1,
                    scoreBook
                )
            )
        }
    }

    @Test
    fun deleteGamePlayer() {
        transaction {
            createSchema()
            val validCourseBody = DbInstrumentation.validPostCourseBody()
            val validPostPlayerBody = DbInstrumentation.validPostPlayerBody()

            val courseId = courseDao.insertCourse(validCourseBody)
            val playerId = playerDao.insertPlayer(validPostPlayerBody)

            val validPostGameBody = PostGameBody(courseId = courseId)
            val gameId = gameDao.insertGame(validPostGameBody)

            assertThrows(GBException::class.java) {
                gameDao.deleteGamePlayer(gameId, 3)
            }

            gameDao.addGamePlayer(gameId, playerId, courseId)

            val gameDetails = gameDao.deleteGamePlayer(gameId, playerId)
            assertThat(gameDetails).isEqualTo( 
               GameDetails(
                   gameId,
                   GBState.WAITING,
                   validCourseBody.name,
                   courseId,
                   1,
                   null
               )
            )
        }
    }


    override fun dropSchema() {
        SchemaUtils.drop(GameTable)
        SchemaUtils.drop(CourseTable)
        SchemaUtils.drop(HoleTable)
        SchemaUtils.drop(ScoreBookTable)
    }

    override fun createSchema() {
        SchemaUtils.create(GameTable)
        SchemaUtils.create(CourseTable)
        SchemaUtils.create(HoleTable)
        SchemaUtils.create(ScoreBookTable)
    }
}