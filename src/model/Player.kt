package com.mindeurfou.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val lastName: String,
    val drawableResourceId: Int
)
