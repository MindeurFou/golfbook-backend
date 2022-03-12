package com.mindeurfou.model.game.local

import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.outgoing.ScoringSystem
import com.mindeurfou.model.player.outgoing.Player
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

data class GameDetails(
    val id : Int,
    val name: String,
    val state: GBState,
    val date: LocalDate,
    val scoringSystem: ScoringSystem,
    val courseName: String,
    val par: List<Int>,
    val players: List<Player>,
    val courseId: Int,
    val scoreBook : ScoreBook
)
