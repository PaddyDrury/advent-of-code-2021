package day12

import util.AocDay
import util.loadInputFromServer
import java.util.*

fun main() {
    Day12(loadInputFromServer("2021", "12")).printTheAnswers()
}


class Day12(inputLines: List<String>) : AocDay {
    private val caveSystem = inputLines.map { it.split("-") }.map { it.first() to it.last() }.let { CaveSystem(it) }

    override fun part1() = caveSystem.paths(
        currentCave = "start",
        destinationCave = "end",
        currentPath = Path(listOf("start"))
    ).size

    override fun part2()= caveSystem.paths(
        currentCave = "start",
        destinationCave = "end",
        currentPath = Path(listOf("start")),
        inAHurry = false
    ).size
}

class CaveSystem(cavePairs: List<Pair<String, String>>) {
    private val connections = (cavePairs + cavePairs.map { it.second to it.first }).groupBy { it.first }
        .mapValues { it.value.map { v -> v.second } }

    fun paths(currentCave: String,
              destinationCave: String,
              currentPath: Path,
              inAHurry: Boolean = true): List<Path> =
        when (currentCave) {
            destinationCave -> listOf(currentPath)
            else -> connections[currentCave]!!.filter { currentPath.canGoTo(it, inAHurry) }
                .flatMap { paths(it, destinationCave, currentPath + it, inAHurry) }
        }

}

fun String.isLowerCase() = lowercase(Locale.getDefault()) == this


data class Path(val caves: List<String>) {
    fun canGoTo(cave: String, inAHurry: Boolean) = !cave.isLowerCase() || when {
        inAHurry || cave == "start" || anySmallCavesVisitedTwice() -> cave !in caves
        else -> true
    }

    operator fun plus(cave: String) = Path(caves + cave)

    private fun anySmallCavesVisitedTwice() = caves.filter { it.isLowerCase() }.groupingBy { it }.eachCount().values.any { it > 1 }
}