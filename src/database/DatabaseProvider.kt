package com.mindeurfou.database.player

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DatabaseProvider : KoinComponent {

    val playerDaoImpl by inject<PlayerDaoImpl>()
}