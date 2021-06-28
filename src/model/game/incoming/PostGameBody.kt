package com.mindeurfou.model.game.incoming

data class PostGameBody(
    val courseId: Int,
    val tournamentId: Int? = null,
)
