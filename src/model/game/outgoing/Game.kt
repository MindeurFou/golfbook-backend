package com.mindeurfou.model.game.outgoing

import com.mindeurfou.model.GBState
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Game(
    val id : Int,
    val name: String,
    val state: GBState,
    val scoringSystem: ScoringSystem,
    val players: List<Player>,
    val courseName: String,
    @Serializable(with = DateAsLongSerializer::class)
    val createdAt: LocalDate
)
