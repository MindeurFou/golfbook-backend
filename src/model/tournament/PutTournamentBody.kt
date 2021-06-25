package com.mindeurfou.model.tournament

import com.mindeurfou.model.GBState

data class PutTournamentBody(
    val id: Int,
    val name: String? = null,
    val state: GBState? = null
)
