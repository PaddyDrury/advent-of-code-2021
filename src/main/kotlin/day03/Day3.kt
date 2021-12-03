package day03

import util.AocDay
import util.columns
import util.loadInputFromServer

fun main() {
    Day3(
        inputLines = """
            00100
            11110
            10110
            10111
            10101
            01111
            00111
            11100
            10000
            11001
            00010
            01010""".trimIndent().lines()
    ).test(198, 230)

    Day3(inputLines = loadInputFromServer("2021", "3"))
        .printTheAnswers()
}

class Day3(
    private val inputLines: List<String>,
    private val columns: List<String> = inputLines.columns()
) : AocDay {

    override fun part1() = gamma().let { gamma ->
        gamma * toEpsilon(gamma)
    }

    // todo - do this in one pass
    override fun part2() = o2GeneratorRating() * co2ScrubberRating()

    private fun String.characterCounts() = associate {
        it to this.count { c -> it == c }
    }

    private fun String.modalCharacterOr(default: Char) = characterCounts().let { counts ->
        when {
            counts.values.maxOrNull().let { max -> counts.values.count { it == max } } > 1 -> default
            else -> counts.maxByOrNull { it.value }!!.key
        }
    }

    private fun String.modalCharacter() = characterCounts().maxByOrNull {
        it.value
    }!!.key

    private fun List<Char>.fromBinaryString() = String(this.toCharArray()).toInt(2)

    private fun gamma() = columns.map { it.modalCharacter() }.fromBinaryString()

    private fun toEpsilon(gamma: Int) = gamma xor columns.map { '1' }.fromBinaryString()

    private fun o2GeneratorRating() = (columns.indices).fold(inputLines) { rows, idx ->
        if (rows.size == 1) rows else
            rows.columns()[idx].modalCharacterOr('1').let { char ->
                rows.filter { it[idx] == char }
            }
    }.first().toInt(2)

    private fun co2ScrubberRating() = (columns.indices).fold(inputLines) { rows, idx ->
        if (rows.size == 1) rows else
            rows.columns()[idx].modalCharacterOr('1').let { char ->
                rows.filter {
                    it[idx] == when (char) {
                        '1' -> '0'
                        else -> '1'
                    }
                }
            }
    }.first().toInt(2)
}