package day02

import util.loadInputFromResource

fun main() {
    println("Part 1 test   ${Day2("test").part1()}")
    println("Part 1 actual ${Day2("input").part1()}")
    println("Part 2 test   ${Day2("test").part2()}")
    println("Part 2 actual ${Day2("input").part2()}")
}

data class SubVector(
    val x: Int = 0,
    val y: Int = 0,
) {
    operator fun plus(other: SubVector) = SubVector(x = x + other.x, y = y + other.y)
    fun product() = x * y
}

data class SubVectorWithAim(
    val x: Int = 0,
    val y: Int = 0,
    val aim: Int = 0,
) {
    fun moveBy(vector: SubVector) = SubVectorWithAim(
        x = x + vector.x,
        y = y + (vector.x * aim),
        aim = aim + vector.y,
    )

    fun product() = x * y
}

fun String.toSubVector() = split(' ').let {
    when (it.first()) {
        "forward" -> SubVector(x = it.last().toInt())
        "down" -> SubVector(y = it.last().toInt())
        "up" -> SubVector(y = 0 - it.last().toInt())
        else -> error("invalid vector")
    }
}

class Day2(
    inputName: String,
    private val input: List<SubVector> = loadInputFromResource<Day2>(inputName).map { it.toSubVector() }
) {
    fun part1() = input.reduce { acc, subVector -> acc + subVector }.product()
    fun part2() = input.fold(SubVectorWithAim()) { acc, subVector -> acc.moveBy(subVector) }.product()
}