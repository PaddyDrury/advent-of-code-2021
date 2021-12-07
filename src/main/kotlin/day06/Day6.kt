package day06

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day6("3,4,3,1,2").printTheAnswers()
    Day6(loadInputFromServer("2021", "6").first()).printTheAnswers()
}

class Day6(input: String) : AocDay {

    private val initialTimerValues = input.split(",").map { it.toInt() }

    override fun part1() = initialTimerValues.map { LanternFish(it) }.let {
        (0 until 80).fold(it) { acc, _ ->
            acc.flatMap {  fish -> fish.nextDay() }
        }.size
    }

    override fun part2() = LanternFishCounts(initialTimerValues.groupingBy { it }.eachCount().mapValues { it.value.toLong() }).let {
        (0 until 256).fold(it) { acc, _ ->
            acc.nextCounts()
        }.total()
    }
}

data class LanternFishCounts(val counts: Map<Int, Long>) {
    fun nextCounts() = LanternFishCounts(
        counts.keys.associate {
            when (it) {
                0,7 -> 6 to (counts[0] ?: 0) + (counts[7] ?: 0)
                else -> (it - 1) to counts[it]!!
            }
        } + mapOf(8 to (counts[0] ?: 0))
    )

    fun total() = counts.values.sum()
}

data class LanternFish(val timer : Int) {
    fun nextDay() = when(timer) {
        0 -> listOf(LanternFish(6), LanternFish(8))
        else -> listOf(LanternFish(timer - 1))
    }
}