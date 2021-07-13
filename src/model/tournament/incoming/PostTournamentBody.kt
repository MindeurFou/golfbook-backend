package com.mindeurfou.model.tournament.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PostTournamentBody(
    val name: String
)
