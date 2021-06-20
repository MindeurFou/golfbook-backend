package com.mindeurfou

import com.mindeurfou.repo.PlayerRepo
import com.mindeurfou.routes.PlayerRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        gson()
    }

    install(Koin) {

    }

    install(Routing) {
        PlayerRouting(PlayerRepo)
    }

}

