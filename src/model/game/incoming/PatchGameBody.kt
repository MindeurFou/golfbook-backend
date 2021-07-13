package com.mindeurfou.model.game.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PatchGameBody(
    val playerId: Int,
    val playing: Boolean
)
