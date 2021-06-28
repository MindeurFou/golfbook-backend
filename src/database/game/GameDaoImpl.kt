package com.mindeurfou.database.game

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.course.CourseDaoImpl
import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.PutScoreBook
import com.mindeurfou.model.game.outgoing.Game
import com.mindeurfou.model.game.incoming.PostGameBody
import com.mindeurfou.model.game.incoming.PutGameBody
import com.mindeurfou.model.game.outgoing.GameDetails
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class GameDaoImpl : GameDao{

    private val scoreBookDao: ScoreBookDao = ScoreBookDaoImpl(
        courseDao = CourseDaoImpl(),
        playerDao = PlayerDaoImpl()
    )

    // automatically delete game if no scoreBook is associate
    override fun getGameById(gameId: Int): GameDetails? = transaction {
        val scoreBook = scoreBookDao.getScoreBookByGameId(gameId)

        (GameTable innerJoin CourseTable).select {
            GameTable.id eq gameId
        }.mapNotNull {
            GameDetailsDbMapper.mapFromEntity(it, scoreBook)
        }.singleOrNull()
    }

    override fun insertGame(postGame: PostGameBody): Int = transaction {

        postGame.tournamentId?.let { id ->
            val query = TournamentTable.select { TournamentTable.id eq id }
            if (query.empty()) throw GBException("Given tournamentId doesn't exist in db")
        }

        val gameId = GameTable.insertAndGetId {
            it[state] = GBState.WAITING
            it[currentHole] = 1
            it[courseId] = postGame.courseId

            postGame.tournamentId?.let { id ->
                it[tournamentId] = EntityID(id, TournamentTable)
            }

        }.value

        gameId
    }

    override fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int): GameDetails? {
        scoreBookDao.insertScoreBookPlayer(gameId, playerId, courseId)
        return getGameById(gameId)
    }

    override fun deleteGamePlayer(gameId: Int, playerId: Int): GameDetails? {
        val deleted = scoreBookDao.deleteScoreBookPlayer(gameId, playerId)
        if (!deleted) throw GBException("This player wasn't in the scorebook")

        return getGameById(gameId)
    }

    override fun updateScoreBook(putScoreBook: PutScoreBook): Map<String, List<Int?>>? =
        scoreBookDao.updateScoreBook(putScoreBook)

    override fun updateGame(putGame: PutGameBody): GameDetails? = transaction {

        val updatedGame = GameTable.update( {GameTable.id eq putGame.id} ) {
            it[state] = putGame.state
            it[courseId] = putGame.courseId
        }

        if (updatedGame == 0) throw GBException("Given gameId doesn't exist in db")

        getGameById(putGame.id)
    }

    override fun deleteGame(gameId: Int): Boolean = transaction {
        scoreBookDao.deleteScoreBook(gameId)
        GameTable.deleteWhere { GameTable.id eq gameId } > 0
    }

    override fun getGamesByTournamentId(tournamentId: Int, limit: Int, offset: Int): List<Game> = transaction {
        GameTable.select {
            GameTable.tournamentId eq tournamentId
        }.mapNotNull {
            val scoreBook = scoreBookDao.getScoreBookByGameId(it[GameTable.id].value)
            val players = scoreBook?.keys?.toList()
            GameDbMapper.mapFromEntity(it, players)
        }
    }

    interface ScoreBookDao {
        fun getScoreBookByGameId(gameId: Int): Map<String, List<Int?>>?
        fun insertScoreBookPlayer(gameId: Int, authorId: Int, courseId: Int) 
        fun deleteScoreBookPlayer(gameId: Int, playerId: Int): Boolean
        fun updateScoreBook(scoreBook: PutScoreBook): Map<String, List<Int?>>?
        fun deleteScoreBook(gameId: Int): Boolean
    }


    private inner class ScoreBookDaoImpl(
        private val courseDao : CourseDao,
        private val playerDao : PlayerDao
    ) : ScoreBookDao{

        override fun getScoreBookByGameId(gameId: Int): Map<String, List<Int?>>? = transaction {
            val query = (ScoreBookTable innerJoin PlayerTable).select {
                ScoreBookTable.gameId eq gameId
            }

            if (query.empty()) return@transaction null

            query.associate {
                ScoreBookDbMapper.mapFromEntity(it)
            }
        }

        override fun insertScoreBookPlayer(gameId: Int, authorId: Int, courseId: Int): Unit = transaction {
            val numberOfHole = courseDao.getCourseById(courseId)?.numberOfHOles ?: throw GBException("Couldn't find this course in db")
            playerDao.getPlayerById(authorId) ?: throw GBException("Couldn't find this player in db")

            val query = ScoreBookTable.select { ScoreBookTable.gameId eq gameId and (ScoreBookTable.playerId eq authorId) }
            if (!query.empty()) throw GBException("Player is already in this scorebook")

            ScoreBookTable.insert {
                it[playerId] = authorId
                it[ScoreBookTable.gameId] = gameId

                it[hole1] = 0
                it[hole2] = 0
                it[hole3] = 0
                it[hole4] = 0
                it[hole5] = 0
                it[hole6] = 0
                it[hole7] = 0
                it[hole8] = 0
                it[hole9] = 0

                val initValue = if (numberOfHole == 18) 0 else null

                it[hole10] = initValue
                it[hole11] = initValue
                it[hole12] = initValue
                it[hole13] = initValue
                it[hole14] = initValue
                it[hole15] = initValue
                it[hole16] = initValue
                it[hole17] = initValue
                it[hole18] = initValue
            }
        }

        override fun deleteScoreBookPlayer(gameId: Int, playerId: Int) = transaction {
            ScoreBookTable.deleteWhere { ScoreBookTable.gameId eq gameId  and (ScoreBookTable.playerId eq playerId)} > 0
        }

        override fun updateScoreBook(scoreBook: PutScoreBook): Map<String, List<Int?>>? = transaction {

            getGameById(scoreBook.gameId) ?: throw GBException("Given gameId wasn't found in db")
            scoreBook.scoreBook.forEach { (playerId, scoreBookEntry) ->
                playerDao.getPlayerById(playerId) ?: throw GBException("Given playerId wasn't found in db")

                ScoreBookTable.update( {ScoreBookTable.gameId eq scoreBook.gameId and (ScoreBookTable.playerId eq playerId) } ) {
                    it[hole1] = scoreBookEntry[0]!!
                    it[hole2] = scoreBookEntry[1]!!
                    it[hole3] = scoreBookEntry[2]!!
                    it[hole4] = scoreBookEntry[3]!!
                    it[hole5] = scoreBookEntry[4]!!
                    it[hole6] = scoreBookEntry[5]!!
                    it[hole7] = scoreBookEntry[6]!!
                    it[hole8] = scoreBookEntry[7]!!
                    it[hole9] = scoreBookEntry[8]!!
                    it[hole10] = scoreBookEntry[9]
                    it[hole11] = scoreBookEntry[10]
                    it[hole12] = scoreBookEntry[11]
                    it[hole13] = scoreBookEntry[12]
                    it[hole14] = scoreBookEntry[13]
                    it[hole15] = scoreBookEntry[14]
                    it[hole16] = scoreBookEntry[15]
                    it[hole17] = scoreBookEntry[16]
                    it[hole18] = scoreBookEntry[17]
                }
            }
            getScoreBookByGameId(scoreBook.gameId)
        }

        override fun deleteScoreBook(gameId: Int): Boolean = transaction {
            ScoreBookTable.deleteWhere { ScoreBookTable.gameId eq gameId } > 0
        }


    }

}