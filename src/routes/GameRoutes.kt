package com.mindeurfou.routes

import com.mindeurfou.model.game.PutScoreBook
import com.mindeurfou.model.game.incoming.PatchGameBody
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.service.GameService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Route.gameRouting() {

    val gameService: GameService by inject()

    route("/game") {

        route("{id}") {

            get {
                val gameId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val gameDetails = gameService.getGame(gameId)
                    call.respond(gameDetails)
                } catch (gBException: GBException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                try {
                    val putGameBody = call.receive<PutGameBody>()
                    val updatedGame = gameService.updateGame(putGameBody)
                    call.respond(updatedGame)
                } catch (e: SerializationException) {
                    return@put call.respond(HttpStatusCode.BadRequest)
                } catch (e: GBException) {
                    if (e.message == GBException.GAME_NOT_FIND_MESSAGE)
                        call.respond(HttpStatusCode.NotFound)
                    else // invalid operation message
                        call.respondText(e.message, status = GBHttpStatusCode.valueA)
                }
            }

            patch {
                val gameId = call.parameters["id"]?.toInt() ?: return@patch call.respond(HttpStatusCode.BadRequest)
                try {
                    val patchGameBody = call.receive<PatchGameBody>()
                    if (patchGameBody.playing)
                        gameService.addGamePlayer(gameId, patchGameBody.playerId)
                    else
                        gameService.deleteGamePlayer(gameId, patchGameBody.playerId)
                    call.respond(HttpStatusCode.OK)
                } catch (e: SerializationException) {
                    return@patch call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete {
                val gameId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                try {
                    val deleted = gameService.deleteGame(gameId)
                    call.respond(deleted)
                } catch (e: GBException) {
                    call.respondText(e.message, status = GBHttpStatusCode.valueA)
                }
            }

            scoreBookRouting(gameService)
        }

        post {
            try {
                val postGameBody = call.receive<PostGameBody>()
                val gameDetails = gameService.addNewGame(postGameBody)
                call.respond(gameDetails)
            } catch (e: SerializationException) {
                return@post call.respond(HttpStatusCode.BadRequest)
            } catch (e: GBException) {
                if (e.message == GBException.TOURNAMENT_NOT_FIND_MESSAGE)
                    call.respond(HttpStatusCode.NotFound)
                else
                    call.respond(GBHttpStatusCode.valueA) // tournament done
            }
        }

        get {
            val tournamentId = call.parameters["tournamentId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val games = gameService.getGameByTournamentId(tournamentId)
            games?.let {
                call.respond(it)
            } ?: return@get call.respond(HttpStatusCode.NoContent)
        }

    }
}


private fun Route.scoreBookRouting(gameService: GameService) {

    route("scorebook") {

        put {
            call.parameters["id"]?.toInt() ?: return@put call.respond(HttpStatusCode.BadRequest)
            try {
                val putScoreBook = call.receive<PutScoreBook>()
                val updatedScorebook = gameService.updateScoreBook(putScoreBook)
                call.respond(updatedScorebook)
            } catch (e: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (e: GBException) {
                if (e.message == GBException.GAME_NOT_FIND_MESSAGE)
                    call.respond(HttpStatusCode.NotFound)
                else // invalid operation
                    call.respondText(e.message, status = GBHttpStatusCode.valueA)
            }
        }

        get {
            val gameId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            try {
                val scoreBook = gameService.getScoreBookByGameId(gameId)
                call.respond(scoreBook)
            } catch (e: GBException) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}