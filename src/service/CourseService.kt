package com.mindeurfou.service

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.utils.GBException

object CourseService {

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

//    TODO virer le ? sur le String
    fun getCourses(filters: Map<String, String?>? = null, limit : Int? = null, offset: Int? = null) =
        courseDao.getCourses(filters, limit, offset)
}