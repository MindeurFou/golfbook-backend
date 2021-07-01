package com.mindeurfou.database.tournament

import com.mindeurfou.model.tournament.PutLeaderBoardBody
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.model.tournament.incoming.PutTournamentBody
import com.mindeurfou.model.tournament.outgoing.Tournament
import com.mindeurfou.model.tournament.outgoing.TournamentDetails

interface TournamentDao {
    fun getTournamentById(tournamentId: Int): TournamentDetails?
    fun insertTournament(postTournament: PostTournamentBody): Int
    fun updateTournament(putTournament: PutTournamentBody): TournamentDetails
    fun deleteTournament(tournamentId: Int): Boolean
    fun getTournaments(filters: Map<String, String?>?, limit : Int? = null, offset: Int? = null): List<Tournament>?

    // leaderBoard specific operations
    fun updateTournamentLeaderBoard(putLeaderBoard: PutLeaderBoardBody): Map<String, Int>?
    fun addTournamentPlayer(tournamentId: Int, playerId: Int): Map<String, Int>?
    fun deleteTournamentPlayer(tournamentId: Int, playerId: Int): Map<String, Int>?
}