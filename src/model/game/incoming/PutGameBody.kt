package com.mindeurfou.model.game.incoming

import com.mindeurfou.model.GBState
import kotlinx.serialization.Serializable

@Serializable
data class PutGameBody(
    val id: Int,
    val state: GBState,
    val courseId: Int
)
