package com.mindeurfou.service

import com.mindeurfou.utils.GBException
import com.mindeurfou.database.tournament.TournamentDao
import com.mindeurfou.database.tournament.TournamentDaoImpl
import com.mindeurfou.model.tournament.incoming.PostTournamentBody
import com.mindeurfou.model.tournament.outgoing.TournamentDetails

class TournamentService : ServiceNotification() {

	private val tournamentDao : TournamentDao = TournamentDaoImpl()

	fun addNewTournament(postTournament: PostTournamentBody): TournamentDetails {
		val tournamentId = tournamentDao.insertTournament(postTournament)
		return tournamentDao.getTournamentById(tournamentId)!!
	}

	fun getTournament(tournamentId: Int): TournamentDetails {
		return tournamentDao.getTournamentById(tournamentId) ?: throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)
	}


}