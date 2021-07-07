package com.mindeurfou.routes

import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.service.PlayerService
import com.mindeurfou.utils.GBException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.playerRouting() {

    val playerService = PlayerService()

    route("/player") {

        route("{id}") {

            get {

                val playerId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val player = playerService.getPlayer(playerId)
                    call.respond(player)
                } catch (gBException: GBException) {
                    call.respondText(gBException.message, status = HttpStatusCode.NotFound)
                }
            }

            put {
                val putPlayerBody = call.receive<PutPlayerBody>()
                try {
                    val updatePlayer = playerService.updatePlayer(putPlayerBody)
                    call.respond(updatePlayer)
                } catch (gBExcpetion: GBException) {
                    call.respondText(gBExcpetion.message, status = HttpStatusCode.NotFound)
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
            } catch (gBException: GBException) {
                call.respondText(gBException.message, status = HttpStatusCode.Conflict)
            }
        }

        get {
            val username = call.parameters["username"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            playerService.getPlayerByUsername(username)?.let {
                call.respond(it)
            } ?: return@get call.respond(HttpStatusCode.NotFound)
        }


    }
}