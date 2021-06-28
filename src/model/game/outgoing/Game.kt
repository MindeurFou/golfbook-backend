package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState

data class Game(
    val id : Int,
    val state: GBState,
    val currentHole: Int,
    val players: List<String>?
)
