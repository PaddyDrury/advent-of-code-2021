package day11

import util.AocDay
import util.loadInputFromServer

typealias EnergyGrid = Array<Array<Int>>

fun main() {
    Day11(
        """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent().trim().lines()
    ).printTheAnswers()

    Day11(loadInputFromServer("2021", "11")).printTheAnswers()
}

class Day11(inputLines: List<String>) : AocDay {
    private val energyGrid = inputLines.map { it.map { char -> "$char".toInt() }.toTypedArray() }.toTypedArray()

    override fun part1() =
        generateSequence(energyGrid) { it.nextLevels() }.map { it.countFlashed() }.take(101).toList().sum()

    override fun part2() = generateSequence(energyGrid) { it.nextLevels() }.indexOfFirst { it.allFlashed() }
}

data class Coord(val x: Int, val y: Int) {
    fun surroundingCoords() =
        (x - 1..x + 1).flatMap { x1 -> (y - 1..y + 1).map { y1 -> Coord(x1, y1) } }.filterNot { it == this }
}

operator fun EnergyGrid.contains(coord: Coord) = coord.y in indices && coord.x in this[0].indices

fun EnergyGrid.valueAt(coord: Coord) = this[coord.y][coord.x]

fun EnergyGrid.allCoords() = this.indices.flatMap { y -> this[y].indices.map { x -> Coord(x, y) } }

fun EnergyGrid.copyAndIncrement() = map { row -> row.map { it + 1 }.toTypedArray() }.toTypedArray()

fun EnergyGrid.countFlashed() = sumOf { row -> row.count { it == 0 } }

fun EnergyGrid.allFlashed() = allCoords().all { flashedAt(it) }

fun EnergyGrid.flashedAt(coord: Coord) = valueAt(coord) == 0

fun EnergyGrid.anyDueToFlash() = allCoords().any { dueToFlashAt(it) }

fun EnergyGrid.dueToFlashAt(coord: Coord) = valueAt(coord) > 9

fun EnergyGrid.reset(coord: Coord) {
    this[coord.y][coord.x] = 0
}

fun EnergyGrid.incrementIfNotFlashedAt(coord: Coord) {
    if (!flashedAt(coord)) this[coord.y][coord.x]++
}

fun EnergyGrid.flashAt(coord: Coord) = reset(coord).also {
    coord.surroundingCoords().filter {
        it in this
    }.forEach {
        incrementIfNotFlashedAt(it)
    }
}

fun EnergyGrid.nextLevels() = copyAndIncrement().let { it ->
    while (it.anyDueToFlash()) {
        it.allCoords().forEach { pos ->
            if (it.dueToFlashAt(pos)) {
                it.flashAt(pos)
            }
        }
    }
    it
}