package com.mindeurfou.model.game.outgoing

enum class ScoringSystem {
    STROKE_PLAY,
    STABLEFORD,
    MATCH_PLAY;

    override fun toString(): String {
        return when(name) {
            STROKE_PLAY.name -> "Stroke Play"
            STABLEFORD.name -> "Stableford"
            MATCH_PLAY.name -> "Match Play"
            else -> ""
        }
    }
}