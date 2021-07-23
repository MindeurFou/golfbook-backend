package com.mindeurfou.service

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.utils.GBException

class CourseService {

    private val courseDao: CourseDao = CourseDaoImpl()

    fun addNewCourse(postCourseBody: PostCourseBody) : CourseDetails {
        val courseId = courseDao.insertCourse(postCourseBody)
        return courseDao.getCourseById(courseId)!!
    }

    fun getCourse(courseId : Int) : CourseDetails {
        return courseDao.getCourseById(courseId) ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)
    }

    fun updateCourse(putCourseBody: PutCourseBody): CourseDetails {
        return courseDao.updateCourse(putCourseBody)
    }

    fun deleteCourse(courseId: Int): Boolean =
        courseDao.deleteCourse(courseId)

    fun getCourses(
        filters: Map<String, String>? = null,
        limit : Int = GET_COURSES_DEFAULT_LIMIT,
        offset: Int = GET_COURSES_DEFAULT_OFFSET
    ) = courseDao.getCourses(filters, limit, offset)

    companion object {
        const val GET_COURSES_DEFAULT_LIMIT = 20
        const val GET_COURSES_DEFAULT_OFFSET = 0
    }

}