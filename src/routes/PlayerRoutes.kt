package com.mindeurfou.routes

import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
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


fun Route.playerRouting() {

    val playerService: PlayerService by inject()

    route("/player") {

        route("{id}") {

            get {

                val playerId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val player = playerService.getPlayer(playerId)
                    call.respond(player)
                } catch (gBException: GBException) {
                    call.respondText(gBException.message, status = GBHttpStatusCode.value)
                }
            }

            put {
                val putPlayerBody = call.receive<PutPlayerBody>()
                try {
                    val updatePlayer = playerService.updatePlayer(putPlayerBody)
                    call.respond(updatePlayer)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (gBExcpetion: GBException) {
                    call.respondText(gBExcpetion.message, status = GBHttpStatusCode.value)
                }
            }

            delete {
                val playerId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleted = playerService.deletePlayer(playerId)
                call.respond(deleted)
            }

        }

        post {
            val postPlayerBody = call.receive<PostPlayerBody>()
            try {
                val player = playerService.addNewPlayer(postPlayerBody)
                call.respond(player)
            } catch(e: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (gBException: GBException) {
                call.respondText(gBException.message, status = GBHttpStatusCode.value)
            }
        }

        get {
            val username = call.parameters["username"] // return@get call.respond(HttpStatusCode.BadRequest)

            if (username != null) {
                playerService.getPlayerByUsername(username)?.let {
                    call.respond(it)
                } ?: return@get call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(playerService.getPlayers())
            }
        }


    }
}