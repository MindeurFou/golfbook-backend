package com.mindeurfou.routes

import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.model.player.outgoing.Player

object RoutingInstrumentation {

    fun player(id: Int) = Player(
        id = id,
        name = "Tanguy",
        lastName = "Pouriel",
        username = "MindeurFou",
        drawableResourceId = 2423429
    )

    fun postPlayerBody() = PostPlayerBody(
        name = "Tanguy",
        lastName = "Pouriel",
        username = "MindeurFou",
        drawableResourceId = 2423429
    )

    fun putPlayerBody(id: Int) = PutPlayerBody(
        id = id,
        username = "LeBoss",
        drawableResourceId = 3429324
    )
}