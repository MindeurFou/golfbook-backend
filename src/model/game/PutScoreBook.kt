package com.mindeurfou.model.game

data class PutScoreBook(
    val gameId: Int,
    val scoreBook: Map<Int, List<Int?>>
)
