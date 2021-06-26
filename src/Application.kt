package com.mindeurfou

import com.mindeurfou.database.player.DatabaseProvider
import com.mindeurfou.database.player.DatabaseProviderContract
import com.mindeurfou.repo.PlayerRepo
import com.mindeurfou.routes.PlayerRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        gson()
    }

    install(Koin) {
        modules(
            module { single<DatabaseProviderContract> { DatabaseProvider }}
        )
    }

    val dbProvider : DatabaseProviderContract by inject()
    dbProvider.init()

    install(Routing) {
        PlayerRouting(PlayerRepo)
    }

}

