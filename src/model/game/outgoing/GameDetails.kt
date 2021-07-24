package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState
import com.mindeurfou.model.player.outgoing.Player
import kotlinx.serialization.Serializable

@Serializable
data class GameDetails(
    val id : Int,
    val state: GBState,
    val courseName: String,
    val courseId: Int,
    val currentHole: Int,
    val players: List<Player>,
    //                  name  , list of scores (null if not played yet)
    val scoreBook : ScoreBook?
)
