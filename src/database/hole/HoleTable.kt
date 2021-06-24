package com.mindeurfou.database.hole

import com.mindeurfou.database.course.CourseTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object HoleTable : IntIdTable() {
    val holeNumber = integer("holeNumber")
    val par = integer("par")
    val courseId = reference("courseId", CourseTable,
        onDelete = ReferenceOption.CASCADE
    )
}