package com.mindeurfou.model.game.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class PlayerScoreNetworkEntity(
    val name: String, // username
    val scores: List<ScoreDetailsNetworkEntity>,
    val scoreSum: String,
    val netSum: String
)
