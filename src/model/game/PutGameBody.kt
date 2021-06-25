package com.mindeurfou.model.game

data class PutGameBody(
    val authorOfUpdateId: Int,
    val currentHole: Int,
    val courseId: Int,
    val scoreBook: Map<String, List<Int?>>
)
