package com.mindeurfou.model.game.local


data class ScoreBook(
    val playerScores: List<PlayerScore>
) {

    companion object {
        fun emptyScoreBook() = ScoreBook(listOf())
    }
}
