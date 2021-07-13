package com.mindeurfou.model.player.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PutPlayerBody(
    val id: Int,
    val username: String,
    val drawableResourceId : Int
)
