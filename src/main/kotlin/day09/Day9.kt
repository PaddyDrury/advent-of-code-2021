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
    }.take(3).map {
        it.size
    }.reduce { a, b -> a * b }
}

data class Coord(val x: Int, val y: Int) {
    fun adjacentCoords() = listOf(
        Coord(x - 1, y),
        Coord(x + 1, y),
        Coord(x, y - 1),
        Coord(x, y + 1),
    )
}

data class HeightMap(
    private val map: List<List<Int>>,
) {
    fun lowValues() = lowPoints().map { valueAt(it) }

    private fun lowPoints() = map.first().indices.flatMap { x ->
        map.indices.map { y ->
            Coord(x, y)
        }
    }.filter { currPos ->
        valueAt(currPos).let { currVal ->
            currPos.adjacentCoords().filter {
                it in this
            }.all {
                valueAt(it) > currVal
            }
        }
    }

    operator fun contains(coord: Coord) = coord.x in map.first().indices && coord.y in map.indices

    fun basins() = lowPoints().map { pointsDrainingTo(it) }

    private fun pointsDrainingTo(coord: Coord, visited: Set<Coord> = setOf(coord)): Set<Coord> =
        valueAt(coord).let { currVal ->
            coord.adjacentCoords().filterNot {
                it in visited
            }.filter {
                it in this
            }.let { newAdjacentCoords ->
                newAdjacentCoords.filter {
                    valueAt(it) in (currVal + 1) until 9
                }.flatMap {
                    pointsDrainingTo(it, visited + newAdjacentCoords)
                }
            }
        }.toSet() + coord

    private fun valueAt(coord: Coord) = map[coord.y][coord.x]
}