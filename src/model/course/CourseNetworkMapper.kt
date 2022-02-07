package com.mindeurfou.model.course

import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.model.course.outgoing.CourseDetailsNetworkEntity
import com.mindeurfou.model.hole.network.HoleNetworkEntity
import com.mindeurfou.model.hole.local.Hole

object CourseNetworkMapper {

    fun toCourseDetailsNetworkMapper(courseDetails: CourseDetails) : CourseDetailsNetworkEntity {
        return CourseDetailsNetworkEntity(
            id = courseDetails.id,
            name = courseDetails.name,
            numberOfHoles = courseDetails.numberOfHoles,
            par = courseDetails.par,
            gamesPlayed = courseDetails.gamesPlayed,
            stars = courseDetails.stars,
            createdAt = courseDetails.createdAt,
            holes = courseDetails.holes.map { toHoleNetworkEntity(it) }
        )
    }

    private fun toHoleNetworkEntity(hole: Hole) : HoleNetworkEntity =
        HoleNetworkEntity(id = hole.id, par = hole.par)
}