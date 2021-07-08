package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState
import com.mindeurfou.model.player.outgoing.Player

data class GameDetails(
    val id : Int,
    val state: GBState,
    val courseName: String,
    val courseId: Int,
    val currentHole: Int,
    val players: List<Player>,
    //                  name  , list of scores (null if not played yet)
    val scoreBook : Map<String, List<Int?>>?
)
