package com.mindeurfou.model.course.incoming

import com.mindeurfou.model.hole.outgoing.Hole

data class PutCourseBody(
    val id: Int,
    val name : String,
    val numberOfHOles : Int,
    val par : Int,
    val gamesPlayed : Int,
    val holes : List<Hole>
)
