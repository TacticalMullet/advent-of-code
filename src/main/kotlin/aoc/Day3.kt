package aoc

import java.io.File

/**
 * https://adventofcode.com/2020/day/3
 */

fun main() {
    val input = File(ClassLoader.getSystemResource("202003").file).readText().split("\n")

    val p1 = Slope(input, 3, 1).let(Slope::treesInPath)

    val p2 = listOf(
        Slope(input, 1,1).let(Slope::treesInPath),
        Slope(input, 3,1).let(Slope::treesInPath),
        Slope(input, 5,1).let(Slope::treesInPath),
        Slope(input, 7,1).let(Slope::treesInPath),
        Slope(input, 1,2).let(Slope::treesInPath)
    ).reduce { a, b -> a * b}

    println("Part 1: $p1")
    println("Part 2: $p2")
}

class Slope(
    private val input: List<String>,
    private val right: Int,
    private val down: Int
) {
    private var numTreesHit: Long = 0L

    private var x: Int = 0
    private var y: Int = 0
    private val maxX = input[0].length
    private val maxY = input.size - 1

    private fun step() {
        x += right
        y += down
        if (isTree()) numTreesHit += 1L
    }

    private fun hasNext(): Boolean = y+down <= maxY

    private fun isTree(): Boolean = input[y][x % maxX].toString() == "#"

    fun treesInPath(): Long {
        while (hasNext()) step()
        return numTreesHit
    }
}
