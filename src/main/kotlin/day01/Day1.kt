package day01

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day1(
        inputLines = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent().lines()
    ).test(7, 5)

    Day1(inputLines = loadInputFromServer("2021", "1"))
        .printTheAnswers()
}

class Day1(
    inputLines: List<String>,
    private val input: List<Int> = inputLines.map { it.toInt() }
) : AocDay {
    override fun part1() = countIncreases(input)
    override fun part2() = countIncreases(input.windowed(size = 3).map { it.sum() })
    private fun countIncreases(list: List<Int>) = list.zipWithNext().count { it.second > it.first }
}