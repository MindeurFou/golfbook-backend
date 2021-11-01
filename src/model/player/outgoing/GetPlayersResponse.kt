package com.mindeurfou.model.player.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class GetPlayersResponse(
    val selfPlayer: Player,
    val players: List<Player>
)
