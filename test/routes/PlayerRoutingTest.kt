package routes

import com.mindeurfou.auth.JWTConfig
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.routes.playerRouting
import com.mindeurfou.routes.registrationRouting
import com.mindeurfou.service.PlayerService
import io.ktor.application.install
import io.ktor.http.*
import io.ktor.routing.Routing
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
class PlayerRoutingTest : BaseRoutingTest() {

    private val playerService: PlayerService = mockk()

    @BeforeAll
    fun setup() {
        koinModules = module {
            single { playerService }
        }
        moduleList = {
            install(Routing) {
                playerRouting()
                registrationRouting()
            }
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(playerService)
    }

    @Test
    fun `POST player`() = withBaseTestApplication {
        val playerId = 1
        val player = RoutingInstrumentation.player(playerId)
        every { playerService.addNewPlayer(any()) } returns player

        val body = toJsonBody(RoutingInstrumentation.postPlayerBody())
        handleRequest(HttpMethod.Post, "/player") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.content
            val tokenMap = "{\"token\":\"${JWTConfig.createToken(playerId)}\",\"playerId\":\"$playerId\"}"
            assertEquals(tokenMap, responseBody)
        }
    }

    @Test
    fun `PUT a player`() = withBaseTestApplication {
        val playerId = 1
        val putPlayerBody = RoutingInstrumentation.putPlayerBody(playerId)
        val player = Player(putPlayerBody.id, "Tanguy", "Pouriel", putPlayerBody.username, putPlayerBody.drawableResourceId)
        every { playerService.updatePlayer(any()) } returns player

        val body = toJsonBody(putPlayerBody)
        handleRequest(HttpMethod.Put, "/player/$playerId") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.parseBody(Player::class.java)
            assertEquals(player, responseBody)
        }
    }

    @Test
    fun `GET player`() = withBaseTestApplication {
        val playerId = 1
        val player = RoutingInstrumentation.player(playerId)
        every { playerService.getPlayer(any()) } returns player

        handleRequest(HttpMethod.Get, "/player/$playerId") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.parseBody(Player::class.java)
            assertEquals(player, responseBody)
        }
    }

    @Test
    fun `DELETE player`() = withBaseTestApplication {
        every { playerService.deletePlayer(any()) } returns true

        handleRequest(HttpMethod.Delete, "/player/1").apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("true", response.content)
        }
    }
}