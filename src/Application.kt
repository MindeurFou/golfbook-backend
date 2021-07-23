package com.mindeurfou

import com.mindeurfou.database.DatabaseProvider
import com.mindeurfou.database.DatabaseProviderContract
import com.mindeurfou.routes.*
import io.ktor.application.*
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
            RoutesInjection.koinRoutesModule
        )
    }

    val dbProvider : DatabaseProviderContract by inject()
    dbProvider.init()

    install(Routing) {
        playerRouting()
        courseRouting()
        gameRouting()
        tournamentRouting()
    }

}

