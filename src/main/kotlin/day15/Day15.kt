package day15

import util.AocDay
import util.loadInputFromServer
import java.util.*


typealias Grid = Array<Array<Int>>

fun main() {
    Day15(
        """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent().trim().lines()
    ).printTheAnswers()

    Day15(loadInputFromServer("2021", "15")).printTheAnswers()
}

class Day15(inputLines: List<String>) : AocDay {
    private val riskLevelMap = inputLines.map { it.map { char -> "$char".toInt() }.toTypedArray() }.toTypedArray()

    private fun generateBigMap(map: Grid) =
        generateSequence(map) { it.nextTile() }.take(5).map { f ->
            generateSequence(f) { it.nextTile() }.take(5).reduce { acc, it ->
                acc.appendRight(it)
            }
        }.reduce { acc, it ->
            acc.appendBelow(it)
        }

    override fun part1() = aStarSearchShortestPathScore(riskLevelMap, Coord(0, 0), riskLevelMap.bottomRight())

    override fun part2() =
        generateBigMap(riskLevelMap).let { map -> aStarSearchShortestPathScore(map, Coord(0, 0), map.bottomRight()) }

    private fun aStarSearchShortestPathScore(entryRiskMap: Grid, start: Coord, end: Coord): Int {
        // map of lowest total risk score from start
        val lowestRiskFromStartMap = Array(entryRiskMap.size) { Array(entryRiskMap[0].size) { Int.MAX_VALUE } }.also {
            it.setAt(start, 0)
        }
        // priority queue of coordinates. Elements with lowest total risk score from start position are presented first
        val searchQueue = PriorityQueue<Coord> { a, b ->
            lowestRiskFromStartMap.valueAt(a).compareTo(lowestRiskFromStartMap.valueAt(b))
        }
        // set of positions we've processed
        val processed = mutableSetOf<Coord>()

        // start at top left
        searchQueue.add(start)

        while (searchQueue.isNotEmpty()) {
            // pop element with lowest total risk score
            val current = searchQueue.poll().also { processed.add(it) }

            fun unprocessedNeighbours() = current.adjacentCoords().filter {
                it in entryRiskMap
            }.filter {
                it !in processed
            }

            // iterate through unprocessed neighbours
            unprocessedNeighbours().forEach { neighbour ->
                // calculate the total risk score for going to this location from the start
                val tentativeRiskTotal = lowestRiskFromStartMap.valueAt(current) + entryRiskMap.valueAt(neighbour)
                if (tentativeRiskTotal < lowestRiskFromStartMap.valueAt(neighbour)) {
                    // total is lower than any previous total at this position so we add it to the map and add the
                    // neighbour to the queue of positions to process
                    lowestRiskFromStartMap.setAt(neighbour, tentativeRiskTotal)
                    searchQueue.add(neighbour)
                }
            }
        }

        // after processing we should have a map of lowest possible risk score for travelling to that location from the start, so the score at the end is the total risk
        return lowestRiskFromStartMap.valueAt(end)
    }
}

data class Coord(val x: Int, val y: Int) {
    fun adjacentCoords() =
        listOf(
            copy(y = y + 1),
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y - 1),
        )

}

operator fun Grid.contains(coord: Coord) = coord.y in indices && coord.x in this[0].indices

fun Grid.valueAt(coord: Coord) = this[coord.y][coord.x]


fun Grid.nextTile() = map { row ->
    row.map {
        when (it) {
            9 -> 1
            else -> it + 1
        }
    }.toTypedArray()
}.toTypedArray()

fun Grid.appendRight(riskLevelMap: Grid) = zip(riskLevelMap).map {
    (it.first + it.second)
}.toTypedArray()

fun Grid.appendBelow(riskLevelMap: Grid) = this + riskLevelMap

fun Grid.setAt(coord: Coord, value: Int) {
    this[coord.y][coord.x] = value
}

fun Grid.bottomRight() = Coord(last().lastIndex, lastIndex)