package day01

fun main(args: Array<String>) {
    println("Part 1 test ${Day1("test").part1()}")
    println("Part 1 actual ${Day1("input").part1()}")
    println("Part 2 test ${Day1("test").part2()}")
    println("Part 2 actual ${Day1("input").part2()}")
}

class Day1(
    inputName: String,
    val input: List<Int> = Day1::class.java.getResource(inputName).readText().lines().map { it.toInt() }) {

    fun part1() = input.zipWithNext().count { it.second > it.first }

    fun part2() = input.windowed(size = 3).map { it.sum() }.zipWithNext().count { it.second > it.first }
}