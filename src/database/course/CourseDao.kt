package com.mindeurfou.database.course

import com.mindeurfou.model.course.Course
import com.mindeurfou.model.course.CourseDetails
import com.mindeurfou.model.course.PostCourseBody

interface CourseDao {
    fun getCourseById(courseId: Int): CourseDetails?
    fun insertCourse(postCourse: PostCourseBody): Int
    fun updateCourse(courseId: Int, postCourse: PostCourseBody): CourseDetails?
    fun deleteCourse(courseId: Int): Boolean
    fun getCourseByName(name: String): Course?
    fun getCourses(filters: Map<String, String?>?): List<Course>?
}