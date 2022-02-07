package com.mindeurfou.model.player.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PutPlayerBody(
    val id: Int,
    val name: String,
    val lastName: String,
    val username: String,
    val avatarId: Int
)
