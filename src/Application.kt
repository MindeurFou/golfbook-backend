package com.mindeurfou

import com.auth0.jwt.interfaces.JWTVerifier
import com.mindeurfou.auth.AuthInjection
import com.mindeurfou.database.DatabaseProvider
import com.mindeurfou.database.DatabaseProviderContract
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.routes.*
import com.mindeurfou.service.PlayerService
import com.mindeurfou.utils.PasswordManager
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    install(Koin) {
        modules(
            module { single<DatabaseProviderContract> { DatabaseProvider }},
            module { single { PasswordManager } },
            RoutesInjection.koinRoutesModule,
            AuthInjection.koinAuthModule
        )
    }

    val dbProvider : DatabaseProviderContract by inject()
    dbProvider.init()

    val playerService: PlayerService by inject()
    val myVerifier: JWTVerifier by inject()

    install (Authentication) {
        jwt("auth-jwt") {
            verifier(myVerifier)
            realm = "Golfbook API"
            validate { credentials ->
                credentials.payload.getClaim("playerId").asInt()?.let { playerId ->
                    playerService.getPlayer(playerId)
                }
            }
        }
    }

    install(Routing) {
        registrationRouting()
        authenticate("auth-jwt") {
            playerRouting()
            courseRouting()
            gameRouting()

//            webSocket("test")
        }
    }

}

