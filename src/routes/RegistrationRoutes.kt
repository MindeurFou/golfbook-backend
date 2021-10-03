package com.mindeurfou.routes

import com.mindeurfou.auth.Credentials
import com.mindeurfou.auth.JWTConfig
import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.service.PlayerService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
import com.mindeurfou.utils.PasswordManager
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Route.registrationRouting() {

    val playerService by inject<PlayerService>()
    val passwordManager by inject<PasswordManager>()

    post("/login") {
        val credentials = call.receive<Credentials>()
        playerService.getPlayerByUsername(credentials.username)?.let { player ->
            val password = playerService.getPlayerPassword(player.id)!!
            if (passwordManager.validatePassword(credentials.password, password)) {
                val token = JWTConfig.createToken(player.id)
                call.respond(mapOf("token" to token))
            } else
                call.respond(HttpStatusCode.Unauthorized)

        } ?: call.respond(HttpStatusCode.Unauthorized)
    }

    post("/player") {
        val postPlayerBody = call.receive<PostPlayerBody>()
        try {
            val player = playerService.addNewPlayer(postPlayerBody)
            val token = JWTConfig.createToken(player.id)
            call.respond(mapOf("token" to token))
        } catch(e: SerializationException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (gBException: GBException) {
            call.respondText(gBException.message, status = GBHttpStatusCode.valueA)
        }
    }
}