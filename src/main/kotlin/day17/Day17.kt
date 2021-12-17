package day17

import util.AocDay
import util.loadInputFromServer

fun main() {
    Day17(loadInputFromServer("2021", "17").first()).printTheAnswers()
}

class Day17(input: String) : AocDay {
    private val targetArea = input.dropWhile {
        !it.isDigit()
    }.takeWhile {
        it.isDigit()
    }.toInt().let { xMin ->
        input.dropWhile {
            it != '.'
        }.dropWhile {
            it == '.'
        }.takeWhile {
            it.isDigit()
        }.toInt().let { xMax ->
            input.split("=").last().let { yRange ->
                yRange.takeWhile {
                    it.isDigit() || it == '-'
                }.toInt().let { yMin ->
                    yRange.dropWhile {
                        it != '.'
                    }.dropWhile {
                        it == '.'
                    }.toInt().let { yMax ->
                        TargetArea(
                            xRange = xMin..xMax,
                            yRange = yMin..yMax,
                        )
                    }
                }
            }
        }
    }

    // not too happy with this but brute force it
    private val allHittingPaths = (500 downTo targetArea.yRange.first).flatMap { y ->
        (0..targetArea.xRange.last).map { x ->
            coordSequence(Velocity(x, y)).toList()
        }
    }.filter {
        targetArea.isAHit(it.last().first)
    }

    override fun part1() = allHittingPaths.flatMap { coords ->
        coords.map { it.first }
    }.maxOf {
        it.y
    }

    override fun part2() = allHittingPaths.map {
        it.first().second
    }.distinct().size

    fun coordSequence(initial: Velocity) =
        generateSequence(Coord(0,0) to initial) { (coord, velocity) ->
            if(targetArea.isAMiss(coord) || targetArea.isAHit(coord)) null else
                coord.next(velocity) to velocity.next()
        }
}

data class Velocity(val x: Int, val y: Int) {
    fun next() = Velocity(
        x.nextXVelocity(), y - 1
    )
}

fun Int.nextXVelocity() = when {
    this == 0 -> 0
    this < 0 -> this + 1
    else -> this - 1
}

data class Coord(val x: Int, val y: Int) {
    fun next(velocity: Velocity) = Coord(x + velocity.x, y + velocity.y)
}

data class TargetArea(val xRange: IntRange, val yRange: IntRange) {
    fun isAHit(coord: Coord) = coord.x in xRange && coord.y in yRange
    fun isAMiss(coord: Coord) = coord.x > xRange.last || coord.y < yRange.first
}