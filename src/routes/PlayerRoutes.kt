package com.mindeurfou.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.PlayerRouting() {

    route("/player") {

        get("{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            // playerRepo.get(id)?.let {
            //     call.respond(it)
            // } ?: kotlin.run { call.respond(HttpStatusCode.NotFound) }
        }

    }
}