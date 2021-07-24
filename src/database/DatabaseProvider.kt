package com.mindeurfou.database

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.game.GameDaoImpl
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.game.scorebook.ScoreBookTable
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.database.player.PlayerTable
import com.mindeurfou.database.tournament.TournamentTable
import com.mindeurfou.database.tournament.leaderboard.LeaderBoardTable
import com.mindeurfou.model.game.incoming.PostGameBody
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseProvider : DatabaseProviderContract {

    override fun init() {
        Database.connect(HikariDataSource(HikariConfig("/hikari.properties")))

        transaction {
            SchemaUtils.create(PlayerTable)
            SchemaUtils.create(PlayerGameAssociation)
            SchemaUtils.create(GameTable)
            SchemaUtils.create(TournamentTable)
            SchemaUtils.create(LeaderBoardTable)
            SchemaUtils.create(ScoreBookTable)
            SchemaUtils.create(CourseTable)
            SchemaUtils.create(HoleTable)
        }

    }
}

interface DatabaseProviderContract {
    fun init()
}