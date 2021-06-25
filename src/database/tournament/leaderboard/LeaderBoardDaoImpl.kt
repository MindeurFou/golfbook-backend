package com.mindeurfou.database.tournament.leaderboard

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class LeaderBoardDaoImpl(
    private val leaderBoardMapper: LeaderBoardDbMapper,
    private val leaderBoardTable: LeaderBoardTable,
    private val playerDao: PlayerDao
) : LeaderBoardDao {

    override fun getLeaderBoardByTournamentId(tournamentId: Int): Map<String, Int> = transaction {
        (leaderBoardTable innerJoin PlayerTable).select {
            TournamentTable.id eq tournamentId
        }.associate {
            leaderBoardMapper.mapFromEntity(it)
        }
    }

    override fun insertLeaderBoard(tournamentId: Int, authorId: Int): Unit = transaction {
        leaderBoardTable.insert {
            it[playerId] = authorId
            it[TournamentTable.id] = tournamentId
            it[score] = 0
        }
    }

    override fun deleteLeaderBoard(tournamentId: Int): Boolean = transaction {
        leaderBoardTable.deleteWhere { leaderBoardTable.tournamentId eq tournamentId } > 0
    }

    override fun updateLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>): Map<String, Int> = transaction {
        leaderBoard.forEach { (name, score) ->
            val playerId = playerDao.getPlayerByUsername(name)?.id ?: throw GBException("Player not found")

            leaderBoardTable.update ({ leaderBoardTable.playerId eq playerId and (leaderBoardTable.tournamentId eq tournamentId) } ) {
                it[leaderBoardTable.score] = score
            }
        }
        getLeaderBoardByTournamentId(tournamentId)
    }

}