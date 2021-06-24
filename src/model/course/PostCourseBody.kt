package com.mindeurfou.model.course

import com.mindeurfou.model.player.Hole

data class PostCourseBody(
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val holes : List<Hole>
)

