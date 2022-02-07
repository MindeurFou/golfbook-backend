package com.mindeurfou.model.course.outgoing

import com.mindeurfou.model.hole.network.HoleNetworkEntity
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CourseDetailsNetworkEntity(
    val id: Int,
    val name : String,
    val numberOfHoles : Int,
    val par : Int,
    val gamesPlayed : Int,
    val stars: Int,
    @Serializable(with = DateAsLongSerializer::class)
    val createdAt : LocalDate,
    val holes : List<HoleNetworkEntity>
)
