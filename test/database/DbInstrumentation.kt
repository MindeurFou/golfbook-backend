package com.mindeurfou.database

import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.hole.incoming.PostHoleBody
import com.mindeurfou.model.hole.outgoing.Hole
import com.mindeurfou.model.player.PostPlayerBody
import com.mindeurfou.model.player.PutPlayerBody

object DbInstrumentation {

    fun validPostPlayerBody() = PostPlayerBody(
        "tanguy",
        "pouriel",
        "MindeurFou",
        247933224
    )

    fun otherValidPostPlayerBody() = PostPlayerBody(
        "jay",
        "adams",
        "LeKing",
        272939308
    )

    fun validPutPlayerBody() = PutPlayerBody(
        "luffy91230",
        34234253
    )

    fun validPostHoleBodies() = listOf(
            PostHoleBody(1,3),
            PostHoleBody(2,4),
            PostHoleBody(3,5),
            PostHoleBody(4,2),
            PostHoleBody(5,5),
            PostHoleBody(6,7),
            PostHoleBody(7,3),
            PostHoleBody(8,4),
            PostHoleBody(9,3)
    )

    fun listOfHoles() = listOf(
        Hole(1,1,3),
        Hole(2,2,4),
        Hole(3,3,5),
        Hole(4,4,2),
        Hole(5,5,5),
        Hole(6,6,7),
        Hole(7,7,3),
        Hole(8,8,4),
        Hole(9,9,3)
    )

    fun validPostCourseBody() = PostCourseBody(
        "Parcours du test",
        9,
        36,
        validPostHoleBodies()
    )

    fun initialScoreBook(name: String) = mapOf(
        name to listOf<Int?>(null, null, null, null, null, null, null, null, null)
    )

}