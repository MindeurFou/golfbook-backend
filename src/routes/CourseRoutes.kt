package com.mindeurfou.routes

import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.service.CourseService
import com.mindeurfou.utils.GBException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.courseRouting() {

    route("/course") {

        route("{id}") {

            get {
                val courseId = call.parameters["id"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
                try {
                    val courseDetails = CourseService.getCourse(courseId)
                    call.respond(courseDetails)
                } catch (gBException : GBException) {
                    call.respondText(gBException.message, status = HttpStatusCode.NotFound)
                }
            }

            put {
                val putCourseBody = call.receive<PutCourseBody>()
                try {
                    val updatedCourse = CourseService.updateCourse(putCourseBody)
                    call.respond(updatedCourse)
                } catch (gBException: GBException) {
                    call.respondText(gBException.message, status = HttpStatusCode.NotFound)
                }
            }

        }

        post {
            val postCourseBody = call.receive<PostCourseBody>()
            try {
                val courseDetails = CourseService.addNewCourse(postCourseBody)
                call.respond(courseDetails)
            } catch (gBException : GBException) {
                call.respondText(gBException.message, status = HttpStatusCode.Conflict)
            }
        }

        get {
            val courses = CourseService.getCourses()?.let {
                call.respond(it)
            } ?: return@get call.respond(HttpStatusCode.NoContent)
        }
    }
}