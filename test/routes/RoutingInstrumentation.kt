package routes

import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.local.GameDetails
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
        drawableResourceId = 2423429
    )

    fun getPlayerResponse() = GetPlayersResponse(
        Player(
            id = 1,
            name = "Tanguy",
            lastName = "Pouriel",
            username = "MindeurFou",
            drawableResourceId = 2423429
        ),
        listOf(Player(2, "name", "lastname", "username", 243232))
    )

    fun postPlayerBody() = PostPlayerBody(
        name = "Tanguy",
        lastName = "Pouriel",
        username = "MindeurFou",
        password = "mypassword",
        drawableResourceId = 2423429,
    )

    fun putPlayerBody(id: Int) = PutPlayerBody(
        id = id,
        username = "LeBoss",
        drawableResourceId = 3429324
    )

    fun initialGameDetails(gameId: Int) = GameDetails(
        gameId,
        GBState.WAITING,
        "Parcours du test",
        1,
        emptyList(),
        null
    )

    fun postGameBody() = PostGameBody(
        1,
        null,
        "myGame"
    )

    fun putGameBody(gameId: Int) = PutGameBody(
        gameId,
        GBState.PENDING,
        1
    )

    fun games() = listOf(
        Game(
            1, "gameName",
            GBState.PENDING,
            "courseName",
            null,
            LocalDate.now()
        ),
        Game(
            2, "gameName2",
            GBState.PENDING,
            "courseName2",
            null,
            LocalDate.now()
        )
    )
}