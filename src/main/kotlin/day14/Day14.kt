package day14

import day14.Polymer.Companion.elementCountsFor
import day14.Polymer.Companion.pairCountsFor
import util.AocDay
import util.loadInputFromServer

fun main() {
    Day14(loadInputFromServer("2021", "14")).printTheAnswers()
}

class Day14(input: List<String>) : AocDay {
    private val initialTemplate = input.first()
    private val pairInsertionRules = input.drop(2).map { it.split(" -> ") }.associate { it.first() to it.last() }
    private val initialPolymer = Polymer(
        pairInsertionRules = pairInsertionRules,
        pairCounts = pairCountsFor(initialTemplate),
        elementCounts = elementCountsFor(initialTemplate)
    )

    override fun part1() = generateSequence(initialPolymer) { it.next() }.drop(10).first().let {
        it.mostCommonElementCount() - it.leastCommonElementCount()
    }

    override fun part2() = generateSequence(initialPolymer) { it.next() }.drop(40).first().let {
        it.mostCommonElementCount() - it.leastCommonElementCount()
    }

}

data class Polymer(
    val pairInsertionRules: Map<String, String>,
    val pairCounts: Map<String, Long>,
    val elementCounts: Map<Char, Long>,
) {

    companion object {
        fun pairCountsFor(polymerTemplate: String) = polymerTemplate
            .windowed(2)
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        fun elementCountsFor(polymerTemplate: String) =
            polymerTemplate
                .groupingBy { it }
                .eachCount()
                .mapValues { it.value.toLong() }
    }

    fun next() = copy(
        pairCounts = nextPairCounts(),
        elementCounts = nextLetterCounts()
    )

    private fun nextPairCounts() = pairCounts.flatMap { e ->
        pairInsertionRules[e.key]!!.let {
            listOf(
                "${e.key.first()}$it" to e.value,
                "$it${e.key.last()}" to e.value
            )
        }
    }.groupBy { it.first }.mapValues { e ->
        e.value.sumOf { it.second }
    }

    private fun nextLetterCounts() = (pairCounts.map {
        pairInsertionRules[it.key]!!.first() to it.value
    } + elementCounts.map { it.key to it.value }).groupBy {
        it.first
    }.mapValues { e -> e.value.sumOf { it.second } }

    fun mostCommonElementCount() = elementCounts.values.maxOrNull()!!
    fun leastCommonElementCount() = elementCounts.values.minOrNull()!!
}