package com.mindeurfou.utils

class GBException(
    message: String,
    throwToUser: Boolean = true
) : Exception(message) {

    companion object {
        const val GAME_NOT_FIND_MESSAGE        = "Game hasn't been found"
        const val COURSE_NOT_FIND_MESSAGE      = "Course hasn't been found"
        const val PLAYER_NOT_FIND_MESSAGE      = "Player hasn't been found"
        const val TOURNAMENT_NOT_FIND_MESSAGE  = "Tournament hasn't been found" 
        const val SCOREBOOK_NOT_FIND_MESSAGE   = "Scorebook hasn't been found"
        const val LEADERBOARD_NOT_FIND_MESSAGE = "Leaderboard hasn't been found"

        const val INVALID_OPERATION_MESSAGE    = "Invalid operation"
    }
}


