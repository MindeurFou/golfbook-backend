package com.mindeurfou.model.hole.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class Hole(
    val id : Int,
    val holeNumber : Int,
    val par : Int
)
