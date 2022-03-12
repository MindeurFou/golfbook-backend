package com.mindeurfou.database.game

import com.mindeurfou.database.PlayerGameAssociation
import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.game.scorebook.PlayerScoreTable
import com.mindeurfou.database.game.scorebook.ScoreBookDbMapper
import com.mindeurfou.database.game.scorebook.ScoreDetailsTable
import com.mindeurfou.database.game.scorebook.ScoreDetailsTable.holeNumber
import com.mindeurfou.database.game.scorebook.ScoreDetailsTable.net
import com.mindeurfou.database.game.scorebook.ScoreDetailsTable.playerScoreId
import com.mindeurfou.database.game.scorebook.ScoreDetailsTable.score
import com.mindeurfou.database.hole.HoleDbMapper
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerDbMapper
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.local.GameDetails
import com.mindeurfou.model.game.local.PlayerScore
import com.mindeurfou.model.game.local.ScoreBook
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GameDaoImpl : GameDao {

    private val courseDao: CourseDao = CourseDaoImpl()
    private val playerDao: PlayerDao = PlayerDaoImpl()

    private val scoreBookDao: ScoreBookDao = ScoreBookDaoImpl(
        courseDao = courseDao,
        playerDao = playerDao
    )

    override fun getGameById(gameId: Int): GameDetails? = transaction {
        val scoreBook = scoreBookDao.getScoreBookByGameId(gameId) ?: ScoreBook.emptyScoreBook()

        val players = (GameTable innerJoin PlayerGameAssociation innerJoin PlayerTable).select {
            GameTable.id eq gameId
        }.mapNotNull {
            PlayerDbMapper.mapFromEntity(it)
        }

        val gameDetails = (GameTable innerJoin CourseTable).select {
            GameTable.id eq gameId
        }.mapNotNull {
            GameDetailsDbMapper.mapFromEntity(it, scoreBook, players, par = listOf())
        }.singleOrNull()


        gameDetails?.let {
            val holes = HoleTable.select {
                HoleTable.courseId eq gameDetails.courseId
            }.mapNotNull { resultRow ->
                HoleDbMapper.mapFromEntity(resultRow)
            }.sortedBy {
                it.holeNumber
            }.map { it.par }

            gameDetails.copy(par = holes)
        }


    }

    override fun insertGame(postGame: PostGameBody): Int = transaction {

        val courseId = courseDao.getCourseByName(postGame.courseName)?.id ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)

        val gameId = GameTable.insertAndGetId {
            it[state] = GBState.INIT
            it[this.courseId] = courseId
            it[name] = postGame.name
            it[scoringSystem] = postGame.scoringSystem

        }.value

        gameId
    }

    // validity of gameId and playerId is checked in gameService
    override fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int): GameDetails = transaction {

        scoreBookDao.insertScoreBookPlayer(gameId, playerId, courseId)

        PlayerGameAssociation.insert {
            it[PlayerGameAssociation.playerId] = playerId
            it[PlayerGameAssociation.gameId] = gameId
        }

        getGameById(gameId)!!
    }

    // validity of gameId and playerId is checked in gameService
    override fun deleteGamePlayer(gameId: Int, playerId: Int): GameDetails = transaction {
        val deleted = true // scoreBookDao.deleteScoreBookPlayer(gameId, playerId)
        if (!deleted) throw GBException(GBException.SCOREBOOK_NOT_FIND_MESSAGE)

        PlayerGameAssociation.deleteWhere {
            PlayerGameAssociation.gameId eq gameId and (PlayerGameAssociation.playerId eq playerId)
        }

        getGameById(gameId)!!
    }

    override fun updateScoreBook(gameId: Int,scoreBook: ScoreBook): ScoreBook =
        scoreBookDao.updateScoreBook(gameId, scoreBook)

    override fun getScoreBookByGameId(gameId: Int): ScoreBook? =
        scoreBookDao.getScoreBookByGameId(gameId)

    override fun updateGame(putGame: PutGameBody): GameDetails = transaction {

        courseDao.getCourseById(putGame.courseId) ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)

        val updatedGame = GameTable.update( {GameTable.id eq putGame.id} ) {
            it[state] = putGame.state
            it[courseId] = putGame.courseId
        }

        if (updatedGame == 0) throw GBException(GBException.GAME_NOT_FIND_MESSAGE)

        getGameById(putGame.id)!!
    }

    override fun deleteGame(gameId: Int): Boolean = transaction {
//        scoreBookDao.deleteScoreBook(gameId)
        GameTable.deleteWhere { GameTable.id eq gameId } > 0
    }

    override fun getGamesByPlayerId(playerId: Int, limit: Int, offset: Int): List<Game> = transaction {

        (GameTable innerJoin PlayerGameAssociation).select {
            PlayerGameAssociation.playerId eq playerId and (GameTable.id eq PlayerGameAssociation.gameId)
        }.mapNotNull {

            val players = scoreBookDao.getScoreBookByGameId(it[GameTable.id].value)?.playerScores?.map { playerScore ->
                playerScore.name
            } ?: listOf()

            val course = courseDao.getCourseById(it[GameTable.courseId].value)
            GameDbMapper.mapFromEntity(it, players, course!!.name)
        }
    }

    override fun getGamesByState(state: GBState): List<Game> = transaction {
        GameTable.select {
            GameTable.state eq state
        }.mapNotNull {

            val players = scoreBookDao.getScoreBookByGameId(it[GameTable.id].value)?.playerScores?.map { playerScore ->
                playerScore.name
            } ?: listOf()
            val course = courseDao.getCourseById(it[GameTable.courseId].value)

            GameDbMapper.mapFromEntity(it, players, course!!.name)
        }
    }

    interface ScoreBookDao {
        fun getScoreBookByGameId(gameId: Int): ScoreBook?
        fun insertScoreBookPlayer(gameId: Int, authorId: Int, courseId: Int)
        fun deleteScoreBookPlayer(gameId: Int, playerId: Int): Boolean
        fun updateScoreBook(gameId: Int, scoreBook: ScoreBook): ScoreBook
        fun deleteScoreBook(gameId: Int): Boolean
    }


    private inner class ScoreBookDaoImpl(
        private val courseDao : CourseDao,
        private val playerDao : PlayerDao
    ) : ScoreBookDao {

        override fun getScoreBookByGameId(gameId: Int): ScoreBook? = transaction {
            val playerScores = (PlayerScoreTable innerJoin PlayerTable).select {
                PlayerScoreTable.gameId eq gameId
            }.mapNotNull {
                print(it.toString())
                ScoreBookDbMapper.mapFromEntityToPlayerScore(it, listOf())
            }

            if (playerScores.isEmpty()) return@transaction null

            val newPlayerScores: MutableList<PlayerScore> = mutableListOf()

            playerScores.forEach { playerScore ->

                val scoreDetails = ScoreDetailsTable.select {
                    playerScoreId eq playerScore.id
                }.mapNotNull {
                    ScoreBookDbMapper.mapFromEntityToScoreDetails(it)
                }.sortedBy {
                    it.holeNumber
                }

                newPlayerScores.add(playerScore.copy(scores = scoreDetails))
            }

            ScoreBook(newPlayerScores)
        }

        override fun insertScoreBookPlayer(gameId: Int, addedPlayerId: Int, courseId: Int): Unit = transaction {

            val query = PlayerScoreTable.select { PlayerScoreTable.gameId eq gameId and (PlayerScoreTable.playerId eq addedPlayerId) }
            if (!query.empty()) return@transaction

            val numberOfHoles = courseDao.getCourseById(courseId)?.numberOfHoles ?: throw GBException(GBException.COURSE_NOT_FIND_MESSAGE)

            val entityId = PlayerScoreTable.insertAndGetId {
                it[this.playerId] = addedPlayerId
                it[PlayerScoreTable.gameId] = gameId
                it[netSum] = ""
            }

            ScoreDetailsTable.batchInsert(List(numberOfHoles) { index -> index + 1 }) { index ->
                this[score] = 0
                this[net] = ""
                this[holeNumber] = index
                this[playerScoreId] = entityId
            }
        }

        override fun deleteScoreBookPlayer(gameId: Int, playerId: Int) = transaction {
            PlayerScoreTable.deleteWhere { PlayerScoreTable.gameId eq gameId  and (PlayerScoreTable.playerId eq playerId)} > 0
        }

        override fun updateScoreBook(gameId: Int, scoreBook: ScoreBook): ScoreBook = transaction {
            getGameById(gameId) ?: throw GBException(GBException.GAME_NOT_FIND_MESSAGE)

            scoreBook.playerScores.forEach { playerScore ->
                playerDao.getPlayerByUsername(playerScore.name) ?: throw GBException(GBException.PLAYER_NOT_FIND_MESSAGE)

                PlayerScoreTable.update({ PlayerScoreTable.id eq playerScore.id }) {
                    it[netSum] = playerScore.netSum
                }

                var index = 1 // TODO check insert of exising values and then test everything
                ScoreDetailsTable.batchInsert(playerScore.scores) { scoreDetails ->
                    this[score] = scoreDetails.score
                    this[net] = scoreDetails.net
                    this[holeNumber] = index
                    this[playerScoreId] = playerScore.id

                    index++
                }
            }
            getScoreBookByGameId(gameId)!!
        }

        override fun deleteScoreBook(gameId: Int): Boolean = transaction {
            PlayerScoreTable.deleteWhere { PlayerScoreTable.gameId eq gameId } > 0
        }

    }

}