package com.mindeurfou.database.course

import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.model.course.Course
import com.mindeurfou.model.course.PostCourseBody
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CourseDaoImpl : CourseDao, KoinComponent {

    private val courseTable: CourseTable by inject()
    private val holeTable: HoleTable by inject()
    private val courseDbMapper: CourseDbMapper by inject()

    override fun getCourseById(courseId: Int): Course? = transaction {
        courseTable.select {
            courseTable.id eq courseId
        }.mapNotNull {
            courseDbMapper.mapCourseFromEntity(it)
        }.singleOrNull()
    }

    override fun insertCourse(postCourse: PostCourseBody): Int {
        return transaction {
            val courseId = courseTable.insertAndGetId {
                it[name] = postCourse.name
                it[numberOfHoles] = postCourse.numberOfHOles
                it[par] = postCourse.numberOfHOles
                it[gamesPlayed] = 0
            }.value

            holeTable.batchInsert(postCourse.holes) { hole ->
                this[holeTable.courseId] = courseId
                this[holeTable.holeNumber] = hole.holeNumber
                this[holeTable.par] = hole.par
            }
            courseId
        }
    }

    override fun updateCourse(courseId: Int, postCourse: PostCourseBody): Course? {
        transaction {
            courseTable.update({courseTable.id eq courseId}) {
                it[name] = postCourse.name
                it[numberOfHoles] = postCourse.numberOfHOles
                it[par] = postCourse.numberOfHOles
            }

            holeTable.batchInsert(postCourse.holes) { hole ->
                this[holeTable.par] = hole.par
                this[holeTable.holeNumber] = hole.holeNumber
            }
        }
        return getCourseById(courseId)
    }

    override fun deleteCourse(courseId: Int) = transaction {
        courseTable.deleteWhere { courseTable.id eq courseId } > 0
    }

    override fun getCourseByName(name: String): Course? = transaction {
        courseTable.select {
            courseTable.name eq name
        }.mapNotNull {
            courseDbMapper.mapCourseFromEntity(it)
        }.singleOrNull()
    }

    override fun getCourses(filters: Map<String, String?>?): List<Course>? {
        return null
    }
    
    private fun getAllCourses(limit: Int = 20, offset: Long = 0) : List<Course> {
        return courseTable.selectAll()
            .limit(limit, offset)
            .orderBy(CourseTable.createdAt to SortOrder.DESC)
            .map { courseDbMapper.mapCourseFromEntity(it) }
    }

}