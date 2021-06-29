package com.mindeurfou.model.player.incoming

data class PostPlayerBody(
    val name: String,
    val lastName : String,
    val username: String,
    val drawableResourceId : Int
)
