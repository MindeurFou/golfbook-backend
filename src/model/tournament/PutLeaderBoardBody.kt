package com.mindeurfou.model.tournament

import kotlinx.serialization.Serializable

@Serializable
data class PutLeaderBoardBody(
    val tournamentId : Int,
    val leaderBoard : Map<String, Int>
)
