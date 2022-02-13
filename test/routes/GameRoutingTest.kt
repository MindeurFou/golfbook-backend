package routes

import com.mindeurfou.model.game.incoming.PatchGameBody
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.outgoing.GameDetailsNetworkEntity
import com.mindeurfou.routes.*
import com.mindeurfou.service.GameService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.dsl.module
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameRoutingTest : BaseRoutingTest() {

    private val gameService: GameService = mockk()

    @BeforeAll
    fun setup() {
        koinModules = module {
            single { gameService }
        }
        moduleList = {
            install(Routing) {
                gameRouting()
            }
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(gameService)
    }

    @Test
    fun `POST Game = create new game`() = withBaseTestApplication {
        val gameId = 1
        val gameDetails = RoutingInstrumentation.initialGameDetails(gameId)
        every { gameService.addNewGame(any()) } returns gameDetails

        val body = toJsonBody(RoutingInstrumentation.postGameBody())
        handleRequest(HttpMethod.Post, "/game") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.parseBody(GameDetailsNetworkEntity::class.java)
            assertEquals(gameDetails, responseBody)
        }
    }

//    @Test
//    fun getGames() = withBaseTestApplication {
//        val games = RoutingInstrumentation.games()
//        every { gameService.getGameByTournamentId(1) } returns games
//
//        handleRequest(HttpMethod.Get, "/game?tournamentId=1") {
//            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//        }.apply {
//            assertEquals(HttpStatusCode.OK, response.status())
//            val responseBody = response.parseBodyList(Game::class.java)
//            assertEquals(games[0].name, responseBody[0].name)
//        }
//    }

    @Test
    fun `PUT game = modify game state or course`() = withBaseTestApplication {
        val gameId = 1
        val gameDetails = RoutingInstrumentation.initialGameDetails(gameId)
        every { gameService.updateGame(any()) } returns gameDetails

        val body = toJsonBody(RoutingInstrumentation.putGameBody(gameId))
        handleRequest(HttpMethod.Put, "/game/1") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.parseBody(GameDetailsNetworkEntity::class.java)
            assertEquals(gameDetails, responseBody)
        }
    }

    @Test
    fun `SerializationException is thrown as expected`() = withBaseTestApplication {
        val body = toJsonBody(Pair("test", "bad JSON"))
        handleRequest(HttpMethod.Post, "/game") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun `PATCH game = update scoreBook`() = withBaseTestApplication {

        val gameDetailsNetworkEntity = RoutingInstrumentation.initialGameDetails(1)
        every { gameService.addGamePlayer(any(), any()) } returns gameDetailsNetworkEntity
        val body = toJsonBody(PatchGameBody(1, true))
        handleRequest(HttpMethod.Patch, "/game/1") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }

    @Test
    fun `PUT scorebook`() = withBaseTestApplication {
//        val expectedScoreBook = ScoreBook(mapOf("Player" to listOf(1,3,null,null,null,null, null, null, null)))
//        every { gameService.getScoreBookByGameId(any()) } returns expectedScoreBook
//        handleRequest(HttpMethod.Get, "/game/1/scorebook") {
//            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//        }.apply {
//            val scorebook = response.parseBody(ScoreBook::class.java)
//            assertEquals(expectedScoreBook, scorebook)
//        }
    }
}