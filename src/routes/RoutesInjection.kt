package com.mindeurfou.routes

import com.mindeurfou.service.PlayerService
import org.koin.dsl.module

object RoutesInjection {
    val koinRoutesModule = module {
        single { PlayerService() }
    }
}