package day08

import util.AocDay
import util.loadInputFromServer

typealias Digit = Set<Char>

fun main() {
    Day8(loadInputFromServer("2021", "8")).printTheAnswers()
}


class Day8(inputLines: List<String>) : AocDay {
    private val inputToOutputMappings = inputLines.associate { line ->
        line.split(" | ")
            .let {
                it.first().toDigits() to it.last().toDigits()
            }
    }

    override fun part1() =
        inputToOutputMappings.values.sumOf { output -> output.count { it.size in listOf(2, 3, 4, 7) } }

    override fun part2() = inputToOutputMappings.entries.sumOf { rowValue(it.key, it.value) }

    private fun rowValue(input: List<Digit>, output: List<Digit>) = decipherInput(input).let { digits ->
        output.map {
            digits[it]
        }.joinToString("").toInt()
    }

    private fun decipherInput(input: List<Digit>): Map<Digit, Int> =
        input.toMutableList().let { remainingDigits ->
            mapOf(
                1 to remainingDigits.findAndRemove { it.size == 2 },
                4 to remainingDigits.findAndRemove { it.size == 4 },
                7 to remainingDigits.findAndRemove { it.size == 3 },
                8 to remainingDigits.findAndRemove { it.size == 7 },
            ).let { map ->
                map + mapOf(
                    6 to remainingDigits.findAndRemove { it.size == 6 && !it.containsAll(map[1]!!) },
                    9 to remainingDigits.findAndRemove { it.size == 6 && it.containsAll(map[4]!!) },
                    0 to remainingDigits.findAndRemove { it.size == 6 },
                    3 to remainingDigits.findAndRemove { it.size == 5 && it.containsAll(map[1]!!) },
                )
            }.let { map ->
                map + mapOf(
                    5 to remainingDigits.findAndRemove { it.size == 5 && it.intersect(map[6]!!) == it },
                    2 to remainingDigits.findAndRemove { it.size == 5 }
                )
            }.entries.associate { (k, v) -> v to k }
        }
}

fun <T> MutableList<T>.findAndRemove(predicate: (T) -> Boolean): T = first(predicate).also { remove(it) }

fun String.toDigits() = split(" ").map { it.toDigit() }

fun String.toDigit() = toCharArray().toSet()