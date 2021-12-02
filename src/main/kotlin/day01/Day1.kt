package day01

import util.loadInputFromResource

fun main() {
    println("Part 1 test   ${Day1("test").part1()}")
    println("Part 1 actual ${Day1("input").part1()}")
    println("Part 2 test   ${Day1("test").part2()}")
    println("Part 2 actual ${Day1("input").part2()}")
}

class Day1(
    inputName: String,
    private val input: List<Int> = loadInputFromResource<Day1>(inputName).map { it.toInt() }) {
    fun part1() = countIncreases(input)
    fun part2() = countIncreases(input.windowed(size = 3).map { it.sum() })
    private fun countIncreases(list: List<Int>) = list.zipWithNext().count { it.second > it.first }
}