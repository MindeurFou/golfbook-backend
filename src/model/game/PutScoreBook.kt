package com.mindeurfou.model.game

import kotlinx.serialization.Serializable

@Serializable
data class PutScoreBook(
    val gameId: Int,
    val scoreBook: Map<Int, List<Int?>>
)
