package com.mindeurfou.model.game.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class ScoreDetailsNetworkEntity(
    val score: Int,
    val net: String
)
