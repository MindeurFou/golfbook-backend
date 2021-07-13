package com.mindeurfou.model.hole.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PostHoleBody(
    val holeNumber : Int,
    val par : Int
)
