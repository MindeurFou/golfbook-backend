package com.mindeurfou.model.player.outgoing

import io.ktor.auth.Principal
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val lastName : String,
    val username : String,
    val avatarId: Int,
    val realUser: Boolean
) : Principal
