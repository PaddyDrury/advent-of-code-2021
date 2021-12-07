package day07

import util.AocDay
import util.loadInputFromServer
import kotlin.math.abs

fun main() {
    Day7("16,1,2,0,4,2,7,1,2,14").printTheAnswers()
    Day7(loadInputFromServer("2021", "7").first()).printTheAnswers()
}

class Day7(input: String) :AocDay {
    val crabSubs =
        CrabSubs(input.split(",").map { CrabSub(it.toInt()) })

    override fun part1() = crabSubs.cheapestAlignmentCost()

    override fun part2() = crabSubs.cheapestAlignmentCost(fixedRate = false)
}

data class CrabSub(val position: Int) {
    fun costToMoveTo(position: Int, fixedRate: Boolean = true) = if(fixedRate) distanceTo(position) else
        (0.. distanceTo(position)).sumOf { it }

    private fun distanceTo(position: Int) = abs(this.position - position)

}

data class CrabSubs(val subs: List<CrabSub>) {
    fun costToMoveTo(position: Int, fixedRate: Boolean = true) = subs.sumOf { it.costToMoveTo(position, fixedRate) }

    fun minPos() = subs.minOf { it.position }
    fun maxPos() = subs.maxOf { it.position }

    fun cheapestAlignmentCost(fixedRate: Boolean = true) = (minPos() .. maxPos()).minOf { costToMoveTo(it, fixedRate) }
}