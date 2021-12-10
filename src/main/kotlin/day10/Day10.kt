package day10

import util.AocDay
import util.loadInputFromServer

fun main() {

    Day10(
        """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent().trim().lines()
    ).printTheAnswers()

    Day10(loadInputFromServer("2021", "10")).printTheAnswers()
}

class Day10(private val input: List<String>) : AocDay {
    override fun part1() =
        input.map {
            NavigationLine(it)
        }.mapNotNull {
            it.syntaxErrorScore()
        }.sum()

    override fun part2() = input.map {
        NavigationLine(it)
    }.mapNotNull {
        it.completionStringScore()
    }.sorted().let {
        it[it.size / 2]
    }
}

data class NavigationLine(val line: String) {
    companion object {
        private val START_TO_END_MAPPINGS = mapOf(
            '(' to ')',
            '<' to '>',
            '[' to ']',
            '{' to '}',
        )
        private val END_TO_START_MAPPINGS = START_TO_END_MAPPINGS.entries.associate { (k, v) -> v to k }
        private val SYNTAX_ERROR_SCORE = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )
        private val COMPLETION_CHAR_SCORE = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4,
        )
    }

    private fun corruptChar() = ArrayDeque<Char>().let { stack ->
        line.firstNotNullOfOrNull { c ->
            when {
                c in START_TO_END_MAPPINGS.keys -> null.also { stack.addLast(c) }
                END_TO_START_MAPPINGS[c]!! == stack.last() -> null.also { stack.removeLast() }
                else -> c
            }
        }
    }

    fun syntaxErrorScore() = corruptChar()?.let {
        SYNTAX_ERROR_SCORE[it]
    }


    private fun completionString(): String? = line.fold(ArrayDeque<Char>()) { stack, c ->
        when {
            c in START_TO_END_MAPPINGS.keys -> stack.addLast(c)
            END_TO_START_MAPPINGS[c]!! == stack.last() -> stack.removeLast()
            else -> return null
        }
        stack
    }.reversed().map {
        START_TO_END_MAPPINGS[it]!!
    }.let {
        String(it.toCharArray())
    }

    fun completionStringScore() = completionString()?.let {
        it.map { c ->
            COMPLETION_CHAR_SCORE[c]!!
        }.fold(0L) { acc, score ->
            (acc * 5) + score
        }
    }
}
