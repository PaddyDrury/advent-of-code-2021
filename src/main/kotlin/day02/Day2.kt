package day02

import util.AocDay
import util.loadInputFromResource

fun main() {
    Day2("test").printTheAnswers()
    Day2("actual").printTheAnswers()
}

data class SubVector(
    val x: Int = 0,
    val y: Int = 0,
) {
    operator fun plus(other: SubVector) = SubVector(x = x + other.x, y = y + other.y)
    fun product() = x * y
}

class Day2(
    override val inputName: String,
    private val input: List<SubVector> = loadInputFromResource<Day2>(inputName).map { it.toSubVector() }
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