package com.mindeurfou.model.game.outgoing

@kotlinx.serialization.Serializable
data class ScoreBookNetworkEntity(
    val playerScores: List<PlayerScoreNetworkEntity>
)
