package com.mindeurfou.model.player.outgoing

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val lastName : String,
    val username : String,
    val drawableResourceId: Int
)
