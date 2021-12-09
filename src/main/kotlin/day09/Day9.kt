package day09

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day9(
        """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
    """.trimIndent().trim().lines()
    ).printTheAnswers("test")

    Day9(loadInputFromServer("2021", "9")).printTheAnswers()
}

class Day9(input: List<String>) : AocDay {
    private val heightMap = HeightMap(input.map { it.map { char -> "$char".toInt() } })

    override fun part1() = heightMap.lowValues().sumOf { it + 1 }

    override fun part2() = heightMap.basins().sortedByDescending {
        it.size
    }.take(3).fold(1L) { acc, coords ->
        acc * coords.size
    }
}

data class Coord(val xPos: Int, val yPos: Int) {
    private fun surroundingCoords() = (xPos - 1..xPos + 1).flatMap { x ->
        (yPos - 1..yPos + 1).map { y ->
            Coord(x, y)
        }
    }.filterNot { it == this }.toSet()

    fun adjacentCoords() = surroundingCoords().filterNot {
        it.xPos != xPos && it.yPos != yPos
    }
}

data class HeightMap(
    val map: List<List<Int>>,
    val allCoords: Set<Coord> = (0 until map.first().size).flatMap { x ->
        (map.indices).map { y ->
            Coord(x, y)
        }
    }.toSet()
) {

    fun lowValues() = lowPoints().map { valueAt(it) }

    private fun lowPoints() = allCoords.filter { currPos ->
        valueAt(currPos).let { currVal ->
            currPos.adjacentCoords().filter {
                it in allCoords
            }.all {
                valueAt(it) > currVal
            }
        }
    }

    fun basins() = lowPoints().map { pointsDrainingTo(it) }

    private fun pointsDrainingTo(coord: Coord, visited: Set<Coord> = setOf(coord)): Set<Coord> =
        valueAt(coord).let { currVal ->
            coord.adjacentCoords().filterNot {
                it in visited
            }.filter {
                it in allCoords
            }.let { newAdjacentCoords ->
                newAdjacentCoords.filter {
                    valueAt(it) in (currVal + 1) until 9
                }.flatMap {
                    pointsDrainingTo(it, visited + newAdjacentCoords)
                }
            }
        }.toSet() + coord

    private fun valueAt(coord: Coord) = map[coord.yPos][coord.xPos]
}