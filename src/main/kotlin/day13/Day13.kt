package day13

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day13(loadInputFromServer("2021", "13")).printTheAnswers()
}

class Day13(inputLines: List<String>) : AocDay {
    private val coords = inputLines.takeWhile {
        it.isNotBlank()
    }.map {
        it.split(",")
    }.map {
        Coord(it.first().toInt(), it.last().toInt())
    }

    private val foldInstructions = inputLines.dropWhile {
        !it.startsWith("fold along")
    }.map {
        it.split("=")
    }.map {
        it.first() to it.last().toInt()
    }

    override fun part1() = foldInstructions.first().let { coords.foldAccordingTo(it) }.distinct().size

    override fun part2() = foldInstructions.fold(coords) { acc, instruction ->
        acc.foldAccordingTo(instruction).distinct()
    }.toPrintable().let { "\n$it\n" }
}

data class Coord(val x: Int, val y: Int) {
    fun foldAlongX(x: Int) = if (this.x < x) this else copy(x = (2 * x) - this.x)
    fun foldAlongY(y: Int) = if (this.y < y) this else copy(y = (2 * y) - this.y)
}

fun List<Coord>.foldAccordingTo(instruction: Pair<String, Int>) = map {
    when (instruction.first) {
        "fold along x" -> it.foldAlongX(instruction.second)
        else -> it.foldAlongY(instruction.second)
    }
}

fun List<Coord>.toPrintable() = (0..maxOf { it.y }).map { y ->
    (0..maxOf { it.x }).joinToString("") { x ->
        if (any { it == Coord(x, y) }) "#" else " "
    }
}.joinToString("\n")