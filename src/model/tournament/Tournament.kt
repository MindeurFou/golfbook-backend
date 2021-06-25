package com.mindeurfou.model.tournament

import com.mindeurfou.model.GBState

data class Tournament(
    val id : Int,
    val name : String,
    val state: GBState,
    val leaderBoard: Map<String, Int>
)
