package com.mindeurfou

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class PlayerTests {

    @Test
    fun testGetPlayerById() {
        withTestApplication({ module(testing = true) }) {

            handleRequest(HttpMethod.Get, "/player/1").apply {
                assertEquals(HttpStatusCode.OK, response.status())

                assertEquals("""{"id":1,"name":"testName","lastName":"testLastName","drawableResourceId":100}""", response.content)
            }
        }
    }
}
