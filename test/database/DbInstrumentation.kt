package database

import com.mindeurfou.model.course.incoming.PostCourseBody
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.outgoing.ScoringSystem
import com.mindeurfou.model.hole.local.Hole
import com.mindeurfou.model.player.incoming.PostPlayerBody

object DbInstrumentation {

    fun validPostPlayerBody() = PostPlayerBody(
        "tanguy",
        "pouriel",
        "MindeurFou",
        "testPassword",
        247933224,
        true
    )

    fun otherValidPostPlayerBody() = PostPlayerBody(
        "jay",
        "adams",
        "LeKing",
        "testtestpwd",
        272939308,
        true
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
        4,
        listOfHoles().map { it.par }
    )

    fun initialScoreBook(name: String) = mapOf(
        name to listOf<Int?>(null, null, null, null, null, null, null, null, null)
    )

    fun validPostGame(courseName : String? = null) = PostGameBody(
        name = "Game of test",
        courseName = courseName ?: "Course of test",
        scoringSystem = ScoringSystem.STABLEFORD)

}