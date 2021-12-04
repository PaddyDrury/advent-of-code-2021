package day04

import util.AocDay
import util.chunkWhen
import util.loadInputFromServer
import util.toColumns

fun main() {
    Day4(
        inputLines = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7""".trimIndent().trim().lines()
    ).test(4512, 1924)

    Day4(inputLines = loadInputFromServer("2021", "4"))
        .printTheAnswers()
}


class Day4(
    inputLines: List<String>,
    private val allDrawNumbers: List<Int> = inputLines.first().split(",").map { it.toInt() },
    private val boards: List<BingoBoard> = inputLines.drop(2)
        .chunkWhen { it.isBlank() }
        .map { grid ->
            grid.map { row ->
                row.split(" ").filterNot {
                    it.isBlank()
                }.map {
                    it.toInt()
                }
            }.let {
                BingoBoard(it)
            }
        }
) : AocDay {
    override fun part1() =
        allDrawNumbers.indices.map {
            allDrawNumbers.take(it)
        }.firstNotNullOf { draw ->
            boards.firstOrNull { it.isBingo(draw) }?.score(draw)
        }

    override fun part2() =
        allDrawNumbers.indices.reversed().map {
            allDrawNumbers.take(it)
        }.zipWithNext().firstNotNullOf { draws ->
            boards.firstOrNull() { !it.isBingo(draws.second) }?.score(draws.first)
        }
}


class BingoBoard(
    private val rows: List<List<Int>>,
    private val columns: List<List<Int>> = rows.toColumns()
) {
    fun isBingo(draw: List<Int>) = (rows + columns).any { draw.containsAll(it) }

    fun score(draw: List<Int>) = rows.sumOf { row ->
        row.filterNot { it in draw }.sum()
    } * draw.last()
}