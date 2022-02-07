package com.mindeurfou.model.player.incoming

import kotlinx.serialization.Serializable

@Serializable
data class PostPlayerBody(
    val name: String,
    val lastName : String,
    val username: String,
    val password: String,
    val avatarId: Int,
    val realUser: Boolean
)
