package com.mindeurfou.database

import com.mindeurfou.database.course.CourseTable
import com.mindeurfou.database.game.GameTable
import com.mindeurfou.database.game.scorebook.ScoreBookTable
import com.mindeurfou.database.hole.HoleTable
import com.mindeurfou.database.player.PlayerTable
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
            SchemaUtils.create(ScoreBookTable)
            SchemaUtils.create(CourseTable)
            SchemaUtils.create(HoleTable)
        }

    }

    private fun wipeOutDatabase() {
        transaction {
            SchemaUtils.drop(PlayerGameAssociation)
            SchemaUtils.drop(ScoreBookTable)
            SchemaUtils.drop(HoleTable)
            SchemaUtils.drop(GameTable)
            SchemaUtils.drop(CourseTable)
            SchemaUtils.drop(PlayerTable)
        }
    }
}

interface DatabaseProviderContract {
    fun init()
}