package com.mindeurfou.repo

import com.mindeurfou.model.player.Player

object PlayerRepo {

    fun get(id: Int) : Player? {

        // TODO

        return Player(id=id, name = "testName", lastName = "testLastName", username = "MindeurFou", drawableResourceId = 100)
    }
}