package com.mindeurfou.database.tournament.leaderboard

interface LeaderBoardDao {
    fun getLeaderBoardByTournamentId(tournamentId: Int): Map<String, Int>
    fun insertLeaderBoard(tournamentId: Int, authorId: Int)
    fun deleteLeaderBoard(tournamentId: Int): Boolean
    fun updateLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>): Map<String, Int>
}