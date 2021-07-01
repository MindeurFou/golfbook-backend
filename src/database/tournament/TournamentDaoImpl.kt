package com.mindeurfou.database.tournament

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.leaderboard.LeaderBoardDbMapper
import com.mindeurfou.database.tournament.leaderboard.LeaderBoardTable
import com.mindeurfou.model.GBState
import com.mindeurfou.model.tournament.PutLeaderBoardBody
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.model.tournament.incoming.PutTournamentBody
import com.mindeurfou.model.tournament.outgoing.Tournament
import com.mindeurfou.model.tournament.outgoing.TournamentDetails
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TournamentDaoImpl : TournamentDao {

    private val leaderBoardDao: LeaderBoardDao = LeaderBoardDaoImpl()

    override fun getTournamentById(tournamentId: Int): TournamentDetails? = transaction {

        val leaderBoard = leaderBoardDao.getLeaderBoardByTournamentId(tournamentId)

        TournamentTable.select {
            TournamentTable.id eq tournamentId
        }.mapNotNull {
            TournamentDetailsDbMapper.mapFromEntity(it, leaderBoard)
        }.singleOrNull()
    }

    override fun insertTournament(postTournament: PostTournamentBody): Int = transaction {
        TournamentTable.insertAndGetId {
            it[name] = postTournament.name
            it[state] = GBState.WAITING
        }.value
    }

    override fun updateTournament(putTournament: PutTournamentBody): TournamentDetails = transaction {
        val updatedColumns = TournamentTable.update( {TournamentTable.id eq putTournament.id}) {
            putTournament.name?.let { newName -> it[name] = newName  }
            putTournament.state?.let { newState -> it[state] = newState }
        }

        if (updatedColumns == 0) throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)
        getTournamentById(putTournament.id)!!
    }

    override fun updateTournamentLeaderBoard(putLeaderBoard: PutLeaderBoardBody): Map<String, Int>? =
        leaderBoardDao.updateLeaderBoard(putLeaderBoard)

    override fun addTournamentPlayer(tournamentId: Int, playerId: Int): Map<String, Int>? {
        leaderBoardDao.insertLeaderBoardPlayer(tournamentId, playerId)
        return leaderBoardDao.getLeaderBoardByTournamentId(tournamentId)
    }

    override fun deleteTournamentPlayer(tournamentId: Int, playerId: Int): Map<String, Int>? {
        val deleted = leaderBoardDao.deleteLeaderBoardPlayer(tournamentId, playerId)
        if (!deleted) throw GBException("Couldn't deleted player from leaderboard")

        return leaderBoardDao.getLeaderBoardByTournamentId(tournamentId)
    }

    override fun deleteTournament(tournamentId: Int): Boolean = transaction {
        leaderBoardDao.deleteLeaderBoard(tournamentId)
        TournamentTable.deleteWhere { TournamentTable.id eq tournamentId } > 0
    }

    override fun getTournaments(filters: Map<String, String?>?, limit: Int?, offset: Int?): List<Tournament>? = transaction {
        // TODO handle filters
        getALlTournaments()
    }

    private fun getALlTournaments(limit: Int = 20, offset: Long = 0) : List<Tournament> =
        TournamentTable.selectAll()
            .limit(limit, offset)
            .orderBy(TournamentTable.createdAt to SortOrder.DESC)
            .mapNotNull {
                TournamentDbMapper.mapFromEntity(it)
            }

    interface LeaderBoardDao {
        fun getLeaderBoardByTournamentId(tournamentId: Int): Map<String, Int>?
        fun insertLeaderBoardPlayer(tournamentId: Int, authorId: Int)
        fun deleteLeaderBoardPlayer(tournamentId: Int, playerId: Int): Boolean
        fun updateLeaderBoard(putLeaderBoard: PutLeaderBoardBody): Map<String, Int>?
        fun deleteLeaderBoard(tournamentId: Int): Boolean
    }

    private inner class LeaderBoardDaoImpl : LeaderBoardDao {

        private val playerDao: PlayerDao = PlayerDaoImpl()

        override fun getLeaderBoardByTournamentId(tournamentId: Int): Map<String, Int>? = transaction {
            val query = (LeaderBoardTable innerJoin PlayerTable).select {
                LeaderBoardTable.tournamentId eq tournamentId
            }
            if (query.empty()) return@transaction null

            query.associate {
                LeaderBoardDbMapper.mapFromEntity(it)
            }
        }

        override fun insertLeaderBoardPlayer(tournamentId: Int, authorId: Int): Unit = transaction {
            playerDao.getPlayerById(authorId) ?: throw GBException(GBException.PLAYER_NOT_FIND_MESSAGE)
            getTournamentById(tournamentId) ?: throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)

            val query = LeaderBoardTable.select { LeaderBoardTable.tournamentId eq tournamentId and (LeaderBoardTable.playerId eq authorId) }
            if (!query.empty()) throw GBException("Player already in leaderboard")

            LeaderBoardTable.insert {
                it[playerId] = authorId
                it[LeaderBoardTable.tournamentId] = tournamentId
                it[score] = 0
            }
        }

        override fun deleteLeaderBoardPlayer(tournamentId: Int, playerId : Int): Boolean = transaction {
            LeaderBoardTable.deleteWhere { LeaderBoardTable.tournamentId eq tournamentId and (LeaderBoardTable.playerId eq playerId ) } > 0
        }

        override fun updateLeaderBoard(putLeaderBoard: PutLeaderBoardBody): Map<String, Int>? = transaction {
            putLeaderBoard.leaderBoard.forEach { (name, score) ->
                val playerId = playerDao.getPlayerByUsername(name)?.id ?: throw GBException(GBException.PLAYER_NOT_FIND_MESSAGE)

                val updatedColumns = LeaderBoardTable.update ({ LeaderBoardTable.playerId eq playerId and (LeaderBoardTable.tournamentId eq putLeaderBoard.tournamentId) } ) {
                    it[LeaderBoardTable.score] = score
                }
                if (updatedColumns == 0) throw GBException(GBException.LEADERBOARD_NOT_FIND_MESSAGE)
            }
            getLeaderBoardByTournamentId(putLeaderBoard.tournamentId)
        }

        override fun deleteLeaderBoard(tournamentId: Int): Boolean = transaction {
            LeaderBoardTable.deleteWhere { LeaderBoardTable.tournamentId eq tournamentId } > 0
        }

    }
}