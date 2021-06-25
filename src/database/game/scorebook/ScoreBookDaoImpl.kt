package com.mindeurfou.database.game.scorebook

import com.mindeurfou.database.course.CourseDao
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.game.ScoreBookDbMapper
import com.mindeurfou.database.game.ScoreBookTable
import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ScoreBookDaoImpl(
    private val scoreBookTable: ScoreBookTable,
    private val scoreBookDbMapper: ScoreBookDbMapper,
    private val courseDao : CourseDao,
    private val playerDao : PlayerDao
) : ScoreBookDao{

    override fun getScoreBookByGameId(gameId: Int): Map<String, List<Int?>> = transaction {
        (scoreBookTable innerJoin PlayerTable).select {
            scoreBookTable.gameId eq gameId
        }.mapNotNull {
            scoreBookDbMapper.mapFromEntity(it)
        }.toMap()
    }

    override fun insertScoreBook(gameId: Int, authorId: Int, courseId: Int): Unit = transaction {
        val numberOfHole = courseDao.getCourseById(courseId)?.numberOfHOles ?: throw GBException("Course not found")

        scoreBookTable.insert {
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
        scoreBookTable.deleteWhere { ScoreBookTable.gameId eq gameId } > 0
    }

    override fun updateScoreBook(gameId: Int, scoreBook: Map<String, List<Int?>>): Map<String, List<Int?>> = transaction {
        scoreBook.forEach { (name, scoreBookEntry) ->
            val playerId = playerDao.getPlayerByUsername(name)?.id ?: throw GBException("Player not found")

            scoreBookTable.update( {GameTable.id eq gameId and (PlayerTable.id eq playerId) } ) {
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
        getScoreBookByGameId(gameId)
    }

    override fun deleteScoreBook(gameId: Int): Boolean = transaction {
        scoreBookTable.deleteWhere { GameTable.id eq gameId } > 0
    }


}