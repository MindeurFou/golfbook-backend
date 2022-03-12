package com.mindeurfou.service

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.model.course.CourseNetworkMapper
import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.model.course.outgoing.Course
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.model.course.outgoing.CourseDetailsNetworkEntity
import com.mindeurfou.utils.GBException

class CourseService {

    private val courseDao: CourseDao = CourseDaoImpl()

    fun addNewCourse(postCourseBody: PostCourseBody) : CourseDetailsNetworkEntity {
        val courseId = courseDao.insertCourse(postCourseBody)
        return CourseNetworkMapper.toCourseDetailsNetworkMapper(courseDao.getCourseById(courseId)!!)
    }

    fun getCourse(courseId : Int) : CourseDetailsNetworkEntity {
        return courseDao.getCourseById(courseId)?.let { CourseNetworkMapper.toCourseDetailsNetworkMapper(it) } ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)
    }

    fun getCourseByName(name : String) : Course {
        return courseDao.getCourseByName(name) ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)
    }

    fun updateCourse(putCourseBody: PutCourseBody): CourseDetails {
        return courseDao.updateCourse(putCourseBody)
    }

    fun deleteCourse(courseId: Int): Boolean =
        courseDao.deleteCourse(courseId)

    fun getCourses(filters: Map<String, String>? = null, limit : Int?, offset: Int?): List<Course> {
        val actualLimit = limit ?: GET_COURSES_DEFAULT_LIMIT
        val actualOffset = offset ?: GET_COURSES_DEFAULT_OFFSET
        return courseDao.getCourses(filters, actualLimit, actualOffset.toLong())
    }

    companion object {
        const val GET_COURSES_DEFAULT_LIMIT = 20
        const val GET_COURSES_DEFAULT_OFFSET = 0
    }

}