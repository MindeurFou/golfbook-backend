package com.mindeurfou.database.course

import com.mindeurfou.utils.DatabaseMapper
import com.mindeurfou.model.course.outgoing.Course
import org.jetbrains.exposed.sql.ResultRow

object CourseDbMapper : DatabaseMapper<ResultRow, Course> {

    override fun mapFromEntity(resultRow: ResultRow): Course = Course(
        id = resultRow[CourseTable.id].value,
        name = resultRow[CourseTable.name],
        numberOfHoles = resultRow[CourseTable.numberOfHoles],
        par = resultRow[CourseTable.par],
        gamesPlayed = resultRow[CourseTable.gamesPlayed],
        stars = 4, // TODO
        createdAt = resultRow[CourseTable.createdAt]
    )
}