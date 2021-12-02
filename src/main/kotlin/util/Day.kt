package util

interface AocDay {
    val inputName: String
    fun part1(): Any = "not implemented"
    fun part2(): Any = "not implemented"

    fun printTheAnswers() {
        println("The $inputName answer to  part 1 is ${part1()}")
        println("The $inputName answer to part 2 is ${part2()}")
    }
}

