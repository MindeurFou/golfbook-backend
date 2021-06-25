package com.mindeurfou.model.course

import com.mindeurfou.model.player.Hole

data class CourseDetails(
    val id: Int,
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val gamesPlayed : Int,
    val holes : List<Hole>
)

object CourseDetailsMapper {
    fun mapToCourseDetails(course: Course, holes: List<Hole>): CourseDetails = CourseDetails(
        id = course.id,
        name = course.name,
        numberOfHOles = course.numberOfHOles,
        par = course.par,
        gamesPlayed = course.gamesPlayed,
        holes = holes
    )
}