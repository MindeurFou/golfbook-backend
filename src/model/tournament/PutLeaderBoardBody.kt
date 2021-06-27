package com.mindeurfou.model.tournament

data class PutLeaderBoardBody(
    val tournamentId : Int,
    val leaderBoard : Map<String, Int>
)
