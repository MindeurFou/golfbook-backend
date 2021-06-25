package com.mindeurfou.database.tournament

import com.mindeurfou.database.tournament.leaderboard.LeaderBoardDao
import com.mindeurfou.model.GBState
import com.mindeurfou.model.tournament.PostTournamentBody
import com.mindeurfou.model.tournament.PutTournamentBody
import com.mindeurfou.model.tournament.Tournament
import com.mindeurfou.model.tournament.TournamentDetails
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TournamentDaoImpl(
    private val tournamentTable: TournamentTable,
    private val tournamentDbMapper: TournamentDbMapper,
    private val leaderBoardDao : LeaderBoardDao,
    private val tournamentDetailsDbMapper: TournamentDetailsDbMapper
) : TournamentDao {

    override fun getTournamentById(tournamentId: Int): TournamentDetails? = transaction {
        val leaderBoard = leaderBoardDao.getLeaderBoardByTournamentId(tournamentId)

        tournamentTable.select {
            tournamentTable.id eq tournamentId
        }.mapNotNull {
            tournamentDetailsDbMapper.mapFromEntity(it, leaderBoard)
        }.singleOrNull()
    }

    private fun getTournamentPreviewById(tournamentId: Int): Tournament? {
        return tournamentTable.select {
            tournamentTable.id eq tournamentId
        }.mapNotNull {
            tournamentDbMapper.mapFromEntity(it)
        }.singleOrNull()
    }

    override fun insertTournament(postTournament: PostTournamentBody): Int = transaction {
        tournamentTable.insertAndGetId {
            it[name] = postTournament.name
            it[state] = GBState.WAITING
        }.value
    }

    override fun updateTournament(putTournament: PutTournamentBody): Tournament? = transaction {
        tournamentTable.update( {tournamentTable.id eq putTournament.id}) {
            putTournament.name?.let { newName -> it[name] = newName  }
            putTournament.state?.let { newState -> it[state] = newState }
        }
        getTournamentPreviewById(putTournament.id)
    }

    override fun updateTournamentLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>) = leaderBoardDao.updateLeaderBoard(tournamentId, leaderBoard)

    override fun deleteTournament(tournamentId: Int): Boolean = transaction {
        leaderBoardDao.deleteLeaderBoard(tournamentId)
        tournamentTable.deleteWhere { tournamentTable.id eq tournamentId } > 0
    }

    override fun getTournaments(filters: Map<String, String?>?, limit: Int?, offset: Int?): List<Tournament>? = transaction {
        // TODO handle filters
        getALlTournaments()
    }

    private fun getALlTournaments(limit: Int = 20, offset: Long = 0) : List<Tournament> =
        tournamentTable.selectAll()
            .limit(limit, offset)
            .orderBy(tournamentTable.createdAt to SortOrder.DESC)
            .mapNotNull {
                tournamentDbMapper.mapFromEntity(it)
            }

}