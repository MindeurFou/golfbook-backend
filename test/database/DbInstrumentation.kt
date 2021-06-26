package com.mindeurfou.database

import com.mindeurfou.model.player.PostPlayerBody
import com.mindeurfou.model.player.PutPlayerBody

object DbInstrumentation {

    fun validPostPlayerBody() = PostPlayerBody(
        "tanguy",
        "pouriel",
        "MindeurFou",
        247933224
    )

    fun validPutPlayerBody() = PutPlayerBody(
        "luffy91230",
        34234253
    )
}