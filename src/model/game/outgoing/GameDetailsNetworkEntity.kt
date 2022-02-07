package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class GameDetailsNetworkEntity(
    val id : Int,
    val name: String,
    val state: GBState,
    @Serializable(with = DateAsLongSerializer::class)
    val date: LocalDate,
    val scoringSystem: ScoringSystem,
    val courseName: String,
    val par: List<Int>,
    val players: List<Player>,
    val scoreBook : ScoreBook
)
