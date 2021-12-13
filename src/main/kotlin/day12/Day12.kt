package day12

import util.AocDay
import util.loadInputFromServer
import java.util.Locale.getDefault

typealias Path = List<String>

fun main() {
    Day12(loadInputFromServer("2021", "12")).printTheAnswers()
}


class Day12(inputLines: List<String>) : AocDay {
    private val caveSystem = inputLines.map { it.split("-") }.map { it.first() to it.last() }.let { CaveSystem(it) }

    override fun part1() = caveSystem.paths(
        destinationCave = "end",
        currentPath = listOf("start"),
        visitableFilter = { path, cave -> !cave.isSmallCave() || cave !in path }
    ).size

    override fun part2() = caveSystem.paths(
        destinationCave = "end",
        currentPath = listOf("start"),
        visitableFilter = { path, cave -> !cave.isSmallCave() || cave !in path || (cave != "start" && path.allSmallCavesVisitedNoMoreThanOnce()) }
    ).size
}

class CaveSystem(cavePairs: List<Pair<String, String>>) {
    private val connections = (cavePairs + cavePairs.map { it.second to it.first }).groupBy { it.first }
        .mapValues { it.value.map { v -> v.second } }

    fun paths(
        destinationCave: String,
        currentPath: Path,
        visitableFilter: (Path, String) -> Boolean
    ): List<Path> =
        when (currentPath.last()) {
            destinationCave -> listOf(currentPath)
            else -> connections[currentPath.last()]!!.filter { visitableFilter.invoke(currentPath, it) }
                .flatMap { paths(destinationCave, currentPath + it, visitableFilter) }
        }

}

fun String.isSmallCave() = lowercase(getDefault()) == this

fun Path.allSmallCavesVisitedNoMoreThanOnce() =
    filter { it.isSmallCave() }.groupingBy { it }.eachCount().values.all { it < 2 }
