package com.mindeurfou.database.tournament.leaderboard

import com.mindeurfou.database.player.PlayerDao
import com.mindeurfou.database.player.PlayerDaoImpl
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.utils.GBException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class LeaderBoardDaoImpl : LeaderBoardDao {

    private val playerDao: PlayerDao = PlayerDaoImpl()

    override fun getLeaderBoardByTournamentId(tournamentId: Int): Map<String, Int> = transaction {
        (LeaderBoardTable innerJoin PlayerTable).select {
            TournamentTable.id eq tournamentId
        }.associate {
            LeaderBoardDbMapper.mapFromEntity(it)
        }
    }

    override fun insertLeaderBoard(tournamentId: Int, authorId: Int): Unit = transaction {
        LeaderBoardTable.insert {
            it[playerId] = authorId
            it[TournamentTable.id] = tournamentId
            it[score] = 0
        }
    }

    override fun deleteLeaderBoard(tournamentId: Int): Boolean = transaction {
        LeaderBoardTable.deleteWhere { LeaderBoardTable.tournamentId eq tournamentId } > 0
    }

    override fun updateLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>): Map<String, Int> = transaction {
        leaderBoard.forEach { (name, score) ->
            val playerId = playerDao.getPlayerByUsername(name)?.id ?: throw GBException("Player not found")

            LeaderBoardTable.update ({ LeaderBoardTable.playerId eq playerId and (LeaderBoardTable.tournamentId eq tournamentId) } ) {
                it[LeaderBoardTable.score] = score
            }
        }
        getLeaderBoardByTournamentId(tournamentId)
    }

}