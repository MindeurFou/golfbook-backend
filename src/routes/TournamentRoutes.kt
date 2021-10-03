package com.mindeurfou.routes

import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.model.tournament.incoming.PutTournamentBody
import com.mindeurfou.service.TournamentService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
import com.mindeurfou.utils.addCacheHeader
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Route.tournamentRouting() {

    val tournamentService: TournamentService by inject()

    route("/tournament") {

        route("{id}") {

            get {
                val tournamentId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val tournamentDetails = tournamentService.getTournament(tournamentId)
                    with(call) {
                        addCacheHeader()
                        respond(tournamentDetails)
                    }
                } catch (e: GBException) {
                    return@get call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                try {
                    val putTournamentBody = call.receive<PutTournamentBody>()
                    val updatedTournament = tournamentService.updateTournament(putTournamentBody)
                    call.respond(updatedTournament)
                } catch (e: SerializationException) {
                    return@put call.respond(HttpStatusCode.BadRequest)
                } catch (e: GBException) {
                    if (e.message == GBException.TOURNAMENT_NOT_FIND_MESSAGE)
                        call.respond(HttpStatusCode.NotFound)
                    else // invalid operation
                        call.respondText(e.message, status = GBHttpStatusCode.valueA)
                }
            }

            delete {
                val tournamentId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleted = tournamentService.deleteTournament(tournamentId)
                call.respond(deleted)
            }

            leaderBoardRouting(tournamentService)
        }

        get {
            val limit = call.parameters["limit"]?.toInt()
            val offset = call.parameters["offset"]?.toInt()
            tournamentService.getTournaments(null, limit, offset)?.let {
                with(call) {
                    addCacheHeader()
                    respond(it)
                }
            } ?: return@get call.respond(HttpStatusCode.NoContent)
        }

        post {
            try {
                val postTournamentBody = call.receive<PostTournamentBody>()
                val tournamentDetails = tournamentService.addNewTournament(postTournamentBody)
                call.respond(tournamentDetails)
            } catch (e: SerializationException) {
                return@post call.respond(HttpStatusCode.BadRequest)
            } catch (e: GBException) {
                call.respondText(e.message, status = GBHttpStatusCode.valueA)
            }

        }
    }
}

fun Route.leaderBoardRouting(tournamentService: TournamentService) {

}