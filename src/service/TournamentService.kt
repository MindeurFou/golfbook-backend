package com.mindeurfou.service

import com.mindeurfou.utils.GBException
import com.mindeurfou.database.tournament.TournamentDao
import com.mindeurfou.database.tournament.TournamentDaoImpl
import com.mindeurfou.model.GBState
import com.mindeurfou.model.tournament.incoming.PutTournamentBody
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

	fun updateTournament(putTournament: PutTournamentBody) : TournamentDetails {
		val tournamentDetails = tournamentDao.getTournamentById(putTournament.id) ?: throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)

		when (putTournament.state) {
			GBState.DONE -> {
				if (tournamentDetails.state == GBState.PENDING) {
					// check games are done
				} else if (tournamentDetails.state == GBState.WAITING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.WAITING -> {
				if (tournamentDetails.state != GBState.WAITING) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
            GBState.PENDING -> {
            	if (tournamentDetails.state == GBState.WAITING) {
            		// check players are ready
				} else if (tournamentDetails.state == GBState.DONE) throw GBException(GBException.INVALID_OPERATION_MESSAGE)
            }
		}

		return tournamentDao.updateTournament(putTournament)
	}

	fun deleteTournament(tournamentId: Int): Boolean {
        val tournamentDetails = tournamentDao.getTournamentById(tournamentId) ?: throw GBException(GBException.TOURNAMENT_NOT_FIND_MESSAGE)
		var deleted = false

		when (tournamentDetails.state) {
			GBState.WAITING -> {
				deleted = tournamentDao.deleteTournament(tournamentId)
			}
			GBState.PENDING -> {
				throw GBException(GBException.INVALID_OPERATION_MESSAGE)
			}
			GBState.DONE -> {
				// are you sure ?
			}
		}

		return deleted
	}

	fun getTournaments(filters: Map<String, String>?, limit: Int? = null, offset: Int? = null) =
		tournamentDao.getTournaments(filters, limit, offset)


}