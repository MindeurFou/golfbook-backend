package com.mindeurfou.model.game.local

data class PlayerScore(
    val id: Int,
    val name: String, // username
    val scores: List<ScoreDetails>,
    val scoreSum: String,
    val netSum: String
)
