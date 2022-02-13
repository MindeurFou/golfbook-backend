package com.mindeurfou.model.game.incoming

import com.mindeurfou.model.game.outgoing.ScoringSystem
import kotlinx.serialization.Serializable

@Serializable
data class PostGameBody(
    val name: String,
    val courseName: String,
    val scoringSystem: ScoringSystem
)
