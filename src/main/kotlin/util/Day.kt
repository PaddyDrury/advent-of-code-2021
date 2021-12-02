package util

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
        println("The $inputName answer to  part 1 is ${part1()}")
        println("The $inputName answer to part 2 is ${part2()}")
    }
}

