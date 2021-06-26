package com.mindeurfou.model.course.outgoing

import com.mindeurfou.model.hole.outgoing.Hole
import java.time.LocalDate

data class CourseDetails(
    val id: Int,
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val gamesPlayed : Int,
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