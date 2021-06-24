package com.mindeurfou.database.course

import com.mindeurfou.model.course.Course
import com.mindeurfou.model.course.CourseDetails
import org.jetbrains.exposed.sql.ResultRow

object CourseDbMapper {

    fun mapCourseFromEntity(resultRow: ResultRow): Course = Course(
        id = resultRow[CourseTable.id].value,
        name = resultRow[CourseTable.name],
        numberOfHOles = resultRow[CourseTable.numberOfHoles],
        par = resultRow[CourseTable.par],
        gamesPlayed = resultRow[CourseTable.gamesPlayed]
    )

    fun mapCourseDetailsFromEntity(resultRow: ResultRow): CourseDetails? {
        return null
    }
}