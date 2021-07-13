package com.mindeurfou.model.course.outgoing

import com.mindeurfou.model.hole.outgoing.Hole
import com.mindeurfou.utils.DateAsLongSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CourseDetails(
    val id: Int,
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val gamesPlayed : Int,
    @Serializable(with = DateAsLongSerializer::class)
    val createdAt : LocalDate,
    val holes : List<Hole>
)

object CourseDetailsMapper {
    fun mapToCourseDetails(course: Course, holes: List<Hole>): CourseDetails = CourseDetails(
        id = course.id,
        name = course.name,
        numberOfHOles = course.numberOfHOles,
        par = course.par,
        gamesPlayed = course.gamesPlayed,
        createdAt = course.createdAt,
        holes = holes
    )
}