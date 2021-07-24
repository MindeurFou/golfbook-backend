package com.mindeurfou.routes

import com.mindeurfou.service.CourseService
import com.mindeurfou.service.GameService
import com.mindeurfou.service.PlayerService
import com.mindeurfou.service.TournamentService
import org.koin.dsl.module

object RoutesInjection {
    val koinRoutesModule = module {
        single { PlayerService() }
        single { CourseService() }
        single { GameService() }
        single { TournamentService() }
    }
}