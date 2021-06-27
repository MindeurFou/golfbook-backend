package com.mindeurfou.model.tournament.outgoing

import com.mindeurfou.model.GBState
import java.time.LocalDate

data class Tournament(
    val id : Int,
    val name : String,
    val state: GBState,
    val  createdAt: LocalDate
)
