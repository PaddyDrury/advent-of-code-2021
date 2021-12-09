package util

import java.time.Duration
import java.time.Instant
import java.time.Instant.now

interface AocDay {
    fun part1(): Any = "not implemented"
    fun part2(): Any = "not implemented"

    fun test(part1: Any = "not implemented", part2: Any = "not implemented") =
    part1().let {
        assert(it == part1) { "expected $it to be $part1"}
    }.also {
        part2().let {
            assert(it == part2) { "expected $it to be $part2"}
        }
    }

    fun printTheAnswers(inputName: String = "actual") {
        now().also { start ->
            part1().also { answer ->
                println("The $inputName answer to  part 1 is $answer and it took ${Duration.between(start, now())}")
            }
        }
        now().also { start ->
            part2().also { answer ->
                println("The $inputName answer to part 2 is $answer and it took ${Duration.between(start, now())}")
            }
        }
    }
}

