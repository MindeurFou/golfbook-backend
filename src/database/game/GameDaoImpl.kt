package com.mindeurfou.database.game

import com.mindeurfou.database.game.scorebook.ScoreBookDao
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.game.Game
import com.mindeurfou.model.game.PostGameBody
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class GameDaoImpl(
    private val gameDbMapper: GameDbMapper,
    private val gameTable: GameTable,
    private val scoreBookDao: ScoreBookDao,
) : GameDao{

    override fun getGameById(gameId: Int): Game? = transaction {

        val scorebook = scoreBookDao.getScoreBookByGameId(gameId)

        gameTable.select {
            GameTable.id eq gameId
        }.mapNotNull {
            gameDbMapper.mapFromEntity(it, scorebook)
        }.singleOrNull()
    }

    override fun insertGame(postGame: PostGameBody): Int = transaction {
        val gameId = gameTable.insertAndGetId {
            it[state] = GBState.WAITING
            it[currentHole] = 0
            it[courseId] = postGame.courseId
            it[TournamentTable.id] = postGame.tournamentId
        }.value

        scoreBookDao.insertScoreBook(gameId, postGame.authorId, postGame.courseId)

        gameId
    }

    override fun addGamePlayer(gameId: Int, playerId: Int, courseId: Int) = scoreBookDao.insertScoreBook(gameId, playerId, courseId)

    override fun deleteGamePlayer(gameId: Int, playerId: Int) = scoreBookDao.deleteScoreBookPlayer(gameId, playerId)

    override fun updateGame(game: Game): Game? = transaction {
        scoreBookDao.updateScoreBook(game.id, game.scoreBook)

        gameTable.update( {GameTable.id eq game.id} ) {
            it[state] = game.state
            it[currentHole] = game.currentHole
            it[courseId] = courseId
        }

        getGameById(game.id)
    }

    override fun deleteGame(gameId: Int): Boolean = transaction {
        scoreBookDao.deleteScoreBook(gameId)
        gameTable.deleteWhere { gameTable.id eq gameId } > 0
    }

    override fun getGamesByTournamentId(tournamentId: Int, limit: Int, offset: Int): List<Game> = transaction {
        gameTable.select {
            gameTable.tournamentId eq tournamentId
        }.map {
            val scoreBook = scoreBookDao.getScoreBookByGameId(it[GameTable.id].value)
            gameDbMapper.mapFromEntity(it,scoreBook)
        }
    }





}