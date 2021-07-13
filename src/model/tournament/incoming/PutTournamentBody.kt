package com.mindeurfou.model.tournament.incoming

import com.mindeurfou.model.GBState
import kotlinx.serialization.Serializable

@Serializable
data class PutTournamentBody(
    val id: Int,
    val name: String? = null,
    val state: GBState? = null
)
