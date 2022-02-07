package com.mindeurfou.model.game.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class ScoreBook(
    val playerScores: List<PlayerScore>
)
