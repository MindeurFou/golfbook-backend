package com.mindeurfou.model.tournament.outgoing

import com.mindeurfou.model.GBState
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TournamentDetails(
    val id : Int,
    val name : String,
    val state: GBState,
    val leaderBoard: Map<String, Int>? = null,
    @Serializable(with = DateAsLongSerializer::class)
    val createdAt: LocalDate,
)
