package com.mindeurfou.model.course.incoming

import com.mindeurfou.model.hole.incoming.PostHoleBody
import kotlinx.serialization.Serializable

@Serializable
data class PostCourseBody(
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val stars : Int,
    val holes : List<PostHoleBody>
)

