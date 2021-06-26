package com.mindeurfou.database

import com.mindeurfou.model.player.PostPlayerBody

object DbInstrumentation {

    fun givenAValidPostPlayerBody() = PostPlayerBody(
        "tanguy",
        "pouriel",
        "MindeurFou",
        247933224
    )
}