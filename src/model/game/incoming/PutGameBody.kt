package com.mindeurfou.model.game.incoming

import com.mindeurfou.model.GBState

data class PutGameBody(
    val id: Int,
    val state: GBState,
    val courseId: Int,
)
