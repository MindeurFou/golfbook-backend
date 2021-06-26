package com.mindeurfou.model.course.incoming

import com.mindeurfou.model.hole.incoming.PostHoleBody

data class PostCourseBody(
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val holes : List<PostHoleBody>
)

