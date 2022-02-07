package com.mindeurfou.model.game.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class ScoreDetails(
    val score: Int,
    val net: String
)
