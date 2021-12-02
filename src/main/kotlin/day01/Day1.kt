package day01

import util.AocDay
import util.loadInputFromResource

fun main() {
    Day1("test").printTheAnswers()
    Day1("actual").printTheAnswers()
}

class Day1(
    override val inputName: String,
    private val input: List<Int> = loadInputFromResource<Day1>(inputName).map { it.toInt() }) : AocDay {
    override fun part1() = countIncreases(input)
    override fun part2() = countIncreases(input.windowed(size = 3).map { it.sum() })
    private fun countIncreases(list: List<Int>) = list.zipWithNext().count { it.second > it.first }
}