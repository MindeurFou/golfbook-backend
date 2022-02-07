package com.mindeurfou.model.course.outgoing

import com.mindeurfou.model.hole.local.Hole
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CourseDetails(
    val id: Int,
    val name : String,
    val numberOfHoles : Int,
    val par : Int,
    val gamesPlayed : Int,
    val stars: Int,
    @Serializable(with = DateAsLongSerializer::class)
    val createdAt : LocalDate,
    val holes : List<Hole>
)

object CourseDetailsMapper {
    fun mapToCourseDetails(course: Course, holes: List<Hole>): CourseDetails = CourseDetails(
        id = course.id,
        name = course.name,
        numberOfHoles = course.numberOfHoles,
        par = course.par,
        gamesPlayed = course.gamesPlayed,
        createdAt = course.createdAt,
        stars = course.stars,
        holes = holes
    )
}