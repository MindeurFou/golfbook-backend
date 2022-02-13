package routes

import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.outgoing.GameDetailsNetworkEntity
import com.mindeurfou.model.game.outgoing.ScoreBook
import com.mindeurfou.model.game.outgoing.ScoringSystem
import com.mindeurfou.model.player.incoming.PostPlayerBody
import com.mindeurfou.model.player.incoming.PutPlayerBody
import com.mindeurfou.model.player.outgoing.GetPlayersResponse
import com.mindeurfou.model.player.outgoing.Player
import java.time.LocalDate

object RoutingInstrumentation {

    fun player(id: Int) = Player(
        id = id,
        name = "Tanguy",
        lastName = "Pouriel",
        username = "MindeurFou",
        avatarId = 1,
        realUser = true
    )

    fun getPlayerResponse() = GetPlayersResponse(
        Player(
            id = 1,
            name = "Tanguy",
            lastName = "Pouriel",
            username = "MindeurFou",
            avatarId = 1,
            realUser = true
        ),
        listOf(Player(2, "name", "lastname", "username", 1, true))
    )

    fun postPlayerBody() = PostPlayerBody(
        name = "Tanguy",
        lastName = "Pouriel",
        username = "MindeurFou",
        password = "mypassword",
        avatarId = 1,
        realUser = true
    )

    fun putPlayerBody(id: Int) = PutPlayerBody(
        id = id,
        username = "LeBoss",
        avatarId = 3429324,
        name = "test name",
        lastName = "test lastname"
    )

    fun initialGameDetails(gameId: Int) = GameDetailsNetworkEntity(
        gameId,
        "Game of test",
        GBState.INIT,
        LocalDate.now(),
        ScoringSystem.STABLEFORD,
        "Parcours du test",
        listOf(3, 3, 4, 4, 4, 4, 4, 4, 4),
        listOf(),
        ScoreBook(listOf())
    )

    fun postGameBody() = PostGameBody(
        "myGame",
        "Parcours du test",
        ScoringSystem.STABLEFORD
    )

    fun putGameBody(gameId: Int) = PutGameBody(
        gameId,
        GBState.PENDING,
        1
    )

    fun games() = listOf(
        Game(
            1,
            "gameName",
            GBState.PENDING,
            ScoringSystem.STABLEFORD,
            listOf(),
            "courseName",
            LocalDate.now(),
        ),
        Game(
            2,
            "gameName2",
            GBState.PENDING,
            ScoringSystem.STABLEFORD,
            listOf(),
            "courseName2",
            LocalDate.now()
        )
    )
}