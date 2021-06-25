package com.mindeurfou.database.game.scorebook

interface ScoreBookDao {
    fun getScoreBookByGameId(gameId: Int): Map<String, List<Int?>>
    fun insertScoreBook(gameId: Int, authorId: Int, courseId: Int)
    fun deleteScoreBookPlayer(gameId: Int, playerId: Int): Boolean
    fun updateScoreBook(gameId: Int, scoreBook: Map<String, List<Int?>>): Map<String, List<Int?>>
    fun deleteScoreBook(gameId: Int): Boolean
}