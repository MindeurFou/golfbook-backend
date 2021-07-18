package com.mindeurfou.routes

import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.course.incoming.PutCourseBody
import com.mindeurfou.service.CourseService
import com.mindeurfou.utils.GBException
import com.mindeurfou.utils.GBHttpStatusCode
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
                } catch (gBException : GBException) {
                    call.respondText(gBException.message, status = GBHttpStatusCode.value)
                }
            }

            put {
                val putCourseBody = call.receive<PutCourseBody>()
                try {
                    val updatedCourse = courseService.updateCourse(putCourseBody)
                    call.respond(updatedCourse)
                } catch (e: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (gBException: GBException) {
                    call.respondText(gBException.message, status = GBHttpStatusCode.value)
                }
            }
        }

    }

    post {
        val postCourseBody = call.receive<PostCourseBody>()
        try {
            val courseDetails = courseService.addNewCourse(postCourseBody)
            call.respond(courseDetails)
        } catch (e: SerializationException) {
            call.respond(HttpStatusCode.BadRequest)
        } catch (gBException : GBException) {
            call.respondText(gBException.message, status = GBHttpStatusCode.value)
        }
    }

    get {
        courseService.getCourses()?.let {
            call.respond(it)
        } ?: return@get call.respond(HttpStatusCode.NoContent)
    }
}