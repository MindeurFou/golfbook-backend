package com.mindeurfou.model.hole.network

import kotlinx.serialization.Serializable

@Serializable
data class HoleNetworkEntity(
    val id : Int,
    val par : Int
)
