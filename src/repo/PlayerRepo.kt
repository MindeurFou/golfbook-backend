package com.mindeurfou.repo

import com.mindeurfou.model.Player

object PlayerRepo {

    fun get(id: Int) : Player? {

        // TODO

        return Player(id=id, name = "testName", lastName = "testLastName", drawableResourceId = 100)
    }
}