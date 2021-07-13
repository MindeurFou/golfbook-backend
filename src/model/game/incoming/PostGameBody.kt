package com.mindeurfou.model.game.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PostGameBody(
    val courseId: Int,
    val tournamentId: Int? = null,
)
