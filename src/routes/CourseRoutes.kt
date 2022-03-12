package com.mindeurfou.routes

import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.service.CourseService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
import com.mindeurfou.utils.addCacheHeader
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.koin.ktor.ext.inject

fun Route.courseRouting() {

    val courseService: CourseService by inject()

    route("/course") {

        route("{id}") {

            get {
                val courseId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val courseDetails = courseService.getCourse(courseId)
                    call.respond(courseDetails)
                } catch (e : GBException) {
                    if (e.message == GBException.COURSE_NOT_FIND_MESSAGE)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(HttpStatusCode.InternalServerError)
                }
            }

            put {
                try {
                    val putCourseBody = call.receive<PutCourseBody>()
                    val updatedCourse = courseService.updateCourse(putCourseBody)
                    call.respond(updatedCourse)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (gBException: GBException) {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete {
                val courseId = call.parameters["id"]?.toInt() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleted = courseService.deleteCourse(courseId)
                call.respond(deleted)
            }
        }

        post {
            try {
                val postCourseBody = call.receive<PostCourseBody>()
                val courseDetails = courseService.addNewCourse(postCourseBody)
                call.respond(courseDetails)
            } catch (e: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (e: GBException) {
                call.respond(GBHttpStatusCode.valueA, e.message)
            }
        }

        get {
            val limit = call.parameters["limit"]?.toInt()
            val offset = call.parameters["offset"]?.toInt()
            val courseName = call.parameters["name"]

            courseName?.let {
                try {
                    val course = courseService.getCourseByName(courseName)
                    call.respond(course)
                } catch (e: GBException) {
                    call.respond(HttpStatusCode.NotFound)
                }
                return@get
            }

            val courses = courseService.getCourses(limit = limit, offset = offset)
            if (courses.isEmpty())
                call.respond(HttpStatusCode.NoContent)
            else {
                with(call) {
                    addCacheHeader()
                    respond(courses)
                }
            }
        }
    }

    route("/courseNames") {
        get {
            val courses = courseService.getCourses(limit = -1, offset = null)
            if (courses.isEmpty())
                call.respond(HttpStatusCode.NoContent)
            else {
                with(call) {
                    addCacheHeader()
                    respond(courses.map { it.name })
                }
            }
        }
    }

}