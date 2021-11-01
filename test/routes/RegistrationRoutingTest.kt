package routes

import com.mindeurfou.auth.Credentials
import com.mindeurfou.auth.JWTConfig
import com.mindeurfou.routes.*
import com.mindeurfou.service.PlayerService
import com.mindeurfou.utils.PasswordManager
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
class RegistrationRoutingTest : BaseRoutingTest() {

    private val playerService: PlayerService = mockk()
    private val passwordManager: PasswordManager = mockk()

    @BeforeAll
    fun setup() {
        koinModules = module {
            single { playerService }
            single { passwordManager }
        }
        moduleList = {
            install(Routing) {
                registrationRouting()
            }
        }
    }

    @BeforeEach
    fun clearMocks() {
        clearMocks(playerService)
        clearMocks(passwordManager)
    }

    @Test
    fun login() = withBaseTestApplication {
        val playerId = 1
        val player = RoutingInstrumentation.player(playerId)
        every { playerService.getPlayerByUsername("testUsername") } returns player
        every { playerService.getPlayerPassword(any()) } returns "myPassword"
        every { passwordManager.validatePassword(any(), any()) } returns true

        val body = toJsonBody(Credentials("testUsername", "myPassword"))

        handleRequest(HttpMethod.Post, "/login") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(body)
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            val responseBody = response.content
            println("body : $responseBody")
            val tokenMap = "{\"token\":\"${JWTConfig.createToken(playerId)}\",\"playerId\":\"$playerId\"}"
            assertEquals(tokenMap, responseBody)
        }
    }

}