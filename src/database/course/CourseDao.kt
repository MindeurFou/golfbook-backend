package com.mindeurfou.database.course

import com.mindeurfou.model.course.Course
import com.mindeurfou.model.course.PostCourseBody

interface CourseDao {
    fun getCourseById(courseId: Int): Course?
    fun insertCourse(postCourse: PostCourseBody): Int
    fun updateCourse(courseId: Int, postCourse: PostCourseBody): Course?
    fun deleteCourse(courseId: Int): Boolean
    fun getCourseByName(name: String): Course?
    fun getCourses(filters: Map<String, String?>?): List<Course>?
}