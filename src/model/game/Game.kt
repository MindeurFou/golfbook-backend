package com.mindeurfou.model.game

import com.mindeurfou.model.GBState

data class Game(
    val id : Int,
    val state: GBState,
    val currentHole: Int,
    //                  name  , list of scores (null if not played yet)
    val scoreBook : Map<String, List<Int?>>
)
