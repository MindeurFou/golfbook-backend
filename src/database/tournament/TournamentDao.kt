package com.mindeurfou.database.tournament

import com.mindeurfou.model.tournament.PostTournamentBody
import com.mindeurfou.model.tournament.PutTournamentBody
import com.mindeurfou.model.tournament.Tournament

interface TournamentDao {
    fun getTournamentById(tournamentId: Int): Tournament?
    fun insertTournament(postTournament: PostTournamentBody): Int
    fun updateTournament(putTournament: PutTournamentBody): Tournament?
    fun updateTournamentLeaderBoard(tournamentId: Int, leaderBoard: Map<String, Int>): Map<String, Int>
    fun deleteTournament(tournamentId: Int): Boolean
    fun getTournaments(filters: Map<String, String?>?, limit : Int? = null, offset: Int? = null): List<Tournament>?
}