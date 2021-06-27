package com.mindeurfou.database.tournament

import com.mindeurfou.database.tournament.leaderboard.LeaderBoardDao
import com.mindeurfou.database.tournament.leaderboard.LeaderBoardDaoImpl
import com.mindeurfou.model.GBState
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.model.tournament.PutTournamentBody
import com.mindeurfou.model.tournament.Tournament
import com.mindeurfou.model.tournament.TournamentDetails
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

    private fun getTournamentPreviewById(tournamentId: Int): Tournament? {
        return TournamentTable.select {
            TournamentTable.id eq tournamentId
        }.mapNotNull {
            TournamentDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }

    override fun insertTournament(postTournament: PostTournamentBody): Int = transaction {
        TournamentTable.insertAndGetId {
            it[name] = postTournament.name
            it[state] = GBState.WAITING
        }.value
    }

    override fun updateTournament(putTournament: PutTournamentBody): Tournament? = transaction {
        TournamentTable.update( {TournamentTable.id eq putTournament.id}) {
            putTournament.name?.let { newName -> it[name] = newName  }
            putTournament.state?.let { newState -> it[state] = newState }
        }
        getTournamentPreviewById(putTournament.id)
    }

    override fun updateTournamentLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>) = leaderBoardDao.updateLeaderBoard(tournamentId, leaderBoard)

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

}