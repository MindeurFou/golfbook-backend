package com.mindeurfou.database.game

import com.mindeurfou.database.player.PlayerTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

// 0 for a non played yet hole and null for a non present hole
object ScoreBookTable : Table(){
    val hole1 = integer("hole1").default(0)
    val hole2 = integer("hole2").default(0)
    val hole3 = integer("hole3").default(0)
    val hole4 = integer("hole4").default(0)
    val hole5 = integer("hole5").default(0)
    val hole6 = integer("hole6").default(0)
    val hole7 = integer("hole7").default(0)
    val hole8 = integer("hole8").default(0)
    val hole9 = integer("hole9").default(0)
    val hole10 = integer("hole10").nullable().default(null)
    val hole11 = integer("hole11").nullable().default(null)
    val hole12 = integer("hole12").nullable().default(null)
    val hole13 = integer("hole13").nullable().default(null)
    val hole14 = integer("hole14").nullable().default(null)
    val hole15 = integer("hole15").nullable().default(null)
    val hole16 = integer("hole16").nullable().default(null)
    val hole17 = integer("hole17").nullable().default(null)
    val hole18 = integer("hole18").nullable().default(null)

    val gameId = reference("gameId", GameTable)
    val playerId = reference("playerId", PlayerTable)
    override val primaryKey = PrimaryKey(gameId, playerId, name = "scoreBookId")
}

object ScoreBookDbMapper {

    fun mapFromEntity(resultRow: ResultRow): Pair<String, List<Int?>> {
        val listPartOne = listOf(
            resultRow[ScoreBookTable.hole1],
            resultRow[ScoreBookTable.hole2],
            resultRow[ScoreBookTable.hole3],
            resultRow[ScoreBookTable.hole4],
            resultRow[ScoreBookTable.hole5],
            resultRow[ScoreBookTable.hole6],
            resultRow[ScoreBookTable.hole7],
            resultRow[ScoreBookTable.hole8],
            resultRow[ScoreBookTable.hole9]
        )
        val listPartTwo = listOf(
            resultRow[ScoreBookTable.hole10],
            resultRow[ScoreBookTable.hole11],
            resultRow[ScoreBookTable.hole12],
            resultRow[ScoreBookTable.hole13],
            resultRow[ScoreBookTable.hole14],
            resultRow[ScoreBookTable.hole15],
            resultRow[ScoreBookTable.hole16],
            resultRow[ScoreBookTable.hole17],
            resultRow[ScoreBookTable.hole18],
        )

        val list = mutableListOf<Int?>()

        listPartOne.forEach {
            if (it == 0)
                list.add(null)
            else
                list.add(it)
        }

        listPartTwo[0]?.let {
            listPartTwo.forEach { score ->
                if (score == 0)
                    list.add(null)
                else
                    list.add(score)
            }
        }
        return Pair(resultRow[PlayerTable.username], list)
    }
}