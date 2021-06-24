package com.mindeurfou.database.hole

import com.mindeurfou.DatabaseMapper
import com.mindeurfou.model.player.Hole
import org.jetbrains.exposed.sql.ResultRow

object HoleDbMapper : DatabaseMapper<ResultRow, Hole> {

    override fun mapFromEntity(resultRow: ResultRow) = Hole(
        id = resultRow[HoleTable.id].value,
        holeNumber = resultRow[HoleTable.holeNumber],
        par = resultRow[HoleTable.par]
    )
}