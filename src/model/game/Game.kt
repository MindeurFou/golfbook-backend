package com.mindeurfou.model.game

data class Game(
    val id : Int,
    val state: GameState,
    val currentHole: Int,
    //                  name  , list of scores (null if not played yet)
    val scoreBook : Map<String, List<Int?>>
)
