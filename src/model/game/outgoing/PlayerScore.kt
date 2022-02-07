package com.mindeurfou.model.game.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class PlayerScore(
    val name: String,
    val scores: List<ScoreDetails>,
    val scoreSum: String,
    val netSum: String
)
