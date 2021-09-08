package com.mindeurfou.routes

import com.mindeurfou.auth.Credentials
import com.mindeurfou.auth.JWTConfig
import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.service.PlayerService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Route.registrationRouting() {

    val playerService by inject<PlayerService>()

    post("/login") {
        val credentials = call.receive<Credentials>()
        playerService.getPlayerByUsername(credentials.username)?.let { player ->

            if (credentials.password.length >= 5) {
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
            call.respond(player)
        } catch(e: SerializationException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (gBException: GBException) {
            call.respondText(gBException.message, status = GBHttpStatusCode.valueA)
        }
    }
}