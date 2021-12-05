package day05

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day5(
        input = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
        """.trimIndent().trim().lines()
    ).apply {
        test(5)
        printTheAnswers()
    }

    Day5(loadInputFromServer("2021", "5")).printTheAnswers()
}

class Day5(input: List<String>) : AocDay {
    private val vents = input.map { it.toVent() }

    override fun part1() = vents.filterNot {
        it.isDiagonal()
    }.countOverlaps()

    override fun part2() = vents.countOverlaps()
}

fun Collection<Vent>.countOverlaps() = flatMap { it.allCoords() }.let { allCoords ->
    allCoords.groupingBy { it }.eachCount().count { it.value > 1 }
}

data class Vent(val start: Coords, val end: Coords) {
    private fun isHorizontal() = start.y == end.y
    private fun isVertical() = start.x == end.x
    fun isDiagonal() = !isHorizontal() && !isVertical()

    fun allCoords() = when {
        isVertical()-> yRange().map { Coords(start.x, it) }
        isHorizontal() -> xRange().map { Coords(it, start.y) }
        else -> xRange().zip(yRange()).map { Coords(it.first, it.second) }
    }

    private fun xRange() = start.x eitherWayRangeTo end.x
    private fun yRange() = start.y eitherWayRangeTo end.y
}

data class Coords(val x: Int, val y: Int)

fun String.toVent() = split(" -> ").let {
    Vent(it.first().toCoords(), it.last().toCoords())
}

fun String.toCoords() = split(",").let {
    Coords(it.first().toInt(), it.last().toInt())
}

infix fun Int.eitherWayRangeTo(other: Int) = when {
    this <= other -> this .. other
    else -> this downTo other
}