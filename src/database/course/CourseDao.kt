package com.mindeurfou.database.course

import com.mindeurfou.model.course.outgoing.Course
import com.mindeurfou.model.course.outgoing.CourseDetails
import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody

interface CourseDao {
    fun getCourseById(courseId: Int): CourseDetails?
    fun insertCourse(postCourse: PostCourseBody): Int
    fun updateCourse(putCourse: PutCourseBody): CourseDetails?
    fun deleteCourse(courseId: Int): Boolean
    fun getCourseByName(name: String): Course?
    fun getCourses(filters: Map<String, String?>? = null, limit : Int? = null, offset: Int? = null): List<Course>?
}