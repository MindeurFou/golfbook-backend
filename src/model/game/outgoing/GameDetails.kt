package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState

data class GameDetails(
    val id : Int,
    val state: GBState,
    val courseName: String,
    val currentHole: Int,
    //                  name  , list of scores (null if not played yet)
    val scoreBook : Map<String, List<Int?>>
)
