package com.mindeurfou.database.player.injection

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerDbMapper
import com.mindeurfou.database.player.PlayerTable
import org.koin.dsl.module

object PlayerDaoInjection {
    val playerDaoModule = module {
        single <PlayerDao>{ PlayerDaoImpl() }
    }
}