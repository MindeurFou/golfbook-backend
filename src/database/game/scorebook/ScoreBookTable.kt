package com.mindeurfou.database.game

import com.mindeurfou.database.player.PlayerTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

// 0 for a non played yet hole and null for a non present hole
object ScoreBookTable : Table(){
    val hole1 = integer("hole1").default(0)
    val hole2 = integer("hole1").default(0)
    val hole3 = integer("hole1").default(0)
    val hole4 = integer("hole1").default(0)
    val hole5 = integer("hole1").default(0)
    val hole6 = integer("hole1").default(0)
    val hole7 = integer("hole1").default(0)
    val hole8 = integer("hole1").default(0)
    val hole9 = integer("hole1").default(0)
    val hole10 = integer("hole1").nullable().default(null)
    val hole11 = integer("hole1").nullable().default(null)
    val hole12 = integer("hole1").nullable().default(null)
    val hole13 = integer("hole1").nullable().default(null)
    val hole14 = integer("hole1").nullable().default(null)
    val hole15 = integer("hole1").nullable().default(null)
    val hole16 = integer("hole1").nullable().default(null)
    val hole17 = integer("hole1").nullable().default(null)
    val hole18 = integer("hole1").nullable().default(null)

    val gameId = reference("gameId", GameTable)
    val playerId = reference("playerId", PlayerTable)
    override val primaryKey = PrimaryKey(PlayerTable.id, GameTable.id, name = "id")
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
        return Pair(resultRow[PlayerTable.name], list)
    }
}