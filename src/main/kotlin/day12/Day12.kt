package day12

import util.AocDay
import util.loadInputFromServer

typealias Path = List<String>

fun main() {
    Day12(loadInputFromServer("2021", "12")).printTheAnswers()
}

class Day12(inputLines: List<String>) : AocDay {
    private val caveSystem = inputLines.map { it.split("-") }.map { it.first() to it.last() }.let { CaveSystem(it) }

    override fun part1() = caveSystem.paths(
        destinationCave = "end",
        path = listOf("start"),
        visitableFilter = { path, cave -> !cave.isSmallCave() || cave !in path }
    ).size

    override fun part2() = caveSystem.paths(
        destinationCave = "end",
        path = listOf("start"),
        visitableFilter = { path, cave -> !cave.isSmallCave() || cave !in path || (cave != "start" && path.noDuplicateSmallCaves()) }
    ).size
}

class CaveSystem(cavePairs: List<Pair<String, String>>) {
    private val connections = (cavePairs + cavePairs.map { it.second to it.first }).groupBy { it.first }
        .mapValues { it.value.map { v -> v.second } }

    fun paths(
        destinationCave: String,
        path: Path,
        visitableFilter: (Path, String) -> Boolean
    ): List<Path> =
        when (path.last()) {
            destinationCave -> listOf(path)
            else -> connections[path.last()]!!.filter { visitableFilter.invoke(path, it) }
                .flatMap { paths(destinationCave, path + it, visitableFilter) }
        }

}

fun String.isSmallCave() = this.all { it.isLowerCase() }

fun Path.noDuplicateSmallCaves() =
    filter { it.isSmallCave() }.groupingBy { it }.eachCount().values.all { it < 2 }
