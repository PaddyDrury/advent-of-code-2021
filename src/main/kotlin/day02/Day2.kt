package day02

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day2(
        inputLines = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent().lines()
    ).test(150, 900)

    Day2(inputLines = loadInputFromServer("2021", "2"))
        .printTheAnswers()
}

data class SubVector(
    val x: Int = 0,
    val y: Int = 0,
) {
    operator fun plus(other: SubVector) = SubVector(x = x + other.x, y = y + other.y)
    fun product() = x * y
}

class Day2(
    inputLines: List<String>,
    private val input: List<SubVector> = inputLines.map { it.toSubVector() }
) : AocDay {
    override fun part1() = input.reduce { acc, subVector -> acc + subVector }.product()
    override fun part2() = input.fold(SubVectorWithAim()) { acc, subVector -> acc.moveBy(subVector) }.vector.product()
}

data class SubVectorWithAim(
    val vector: SubVector = SubVector(),
    val aim: Int = 0,
) {
    fun moveBy(other: SubVector) = SubVectorWithAim(
        vector = vector + other.copy(y = other.x * aim),
        aim = aim + other.y,
    )
}

fun String.toSubVector() = split(' ').let {
    when (it.first()) {
        "forward" -> SubVector(x = it.last().toInt())
        "down" -> SubVector(y = it.last().toInt())
        "up" -> SubVector(y = 0 - it.last().toInt())
        else -> error("invalid vector")
    }
}