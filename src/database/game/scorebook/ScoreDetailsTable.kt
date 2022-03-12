package com.mindeurfou.database.game.scorebook

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ScoreDetailsTable : IntIdTable() {
    val score = integer("score")
    val net = varchar("net", 255)
    val holeNumber = integer("holeNumber")
    val playerScoreId = reference(
        name = "playerScoreId",
        foreign = PlayerScoreTable,
        onDelete = ReferenceOption.CASCADE
    )
}