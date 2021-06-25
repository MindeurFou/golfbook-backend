package com.mindeurfou.model.game

data class PostGameBody(
    val courseId: Int,
    val tournamentId: Int? = null,
    val authorId: Int
)
