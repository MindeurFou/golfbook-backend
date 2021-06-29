package com.mindeurfou.model.player.incoming

data class PutPlayerBody(
    val id: Int,
    val username: String,
    val drawableResourceId : Int
)
