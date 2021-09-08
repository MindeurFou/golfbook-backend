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
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                val putPlayerBody = call.receive<PutPlayerBody>()
                try {
                    val updatePlayer = playerService.updatePlayer(putPlayerBody)
                    call.respond(updatePlayer)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (e: GBException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete {
                val playerId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleted = playerService.deletePlayer(playerId)
                call.respond(deleted)
            }

        }

        get {
            val username = call.parameters["username"]

            if (username != null) {
                playerService.getPlayerByUsername(username)?.let {
                    call.respond(it)
                } ?: return@get call.respond(HttpStatusCode.NotFound)
            } else {
                val limit = call.parameters["limit"]?.toInt()
                val offset = call.parameters["offset"]?.toInt()
                val players = playerService.getPlayers(limit = limit, offset = offset)
                if (players.isEmpty())
                    call.respond(HttpStatusCode.NoContent)
                else
                    call.respond(players)
            }
        }


    }
}