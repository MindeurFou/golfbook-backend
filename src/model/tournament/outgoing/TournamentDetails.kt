package com.mindeurfou.model.tournament.outgoing

import com.mindeurfou.model.GBState
import java.time.LocalDate

data class TournamentDetails(
    val id : Int,
    val name : String,
    val state: GBState,
    val leaderBoard: Map<String, Int>? = null,
    val createdAt: LocalDate,
)
