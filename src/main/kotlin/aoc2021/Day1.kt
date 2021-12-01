package aoc2021

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("202101").file).readText().split("\n")
        .map { it.toInt() }

    fun part1(): Int =
        input.foldIndexed(0) { i, acc, it ->
            if (i > 0 && input[i - 1] < it) acc + 1 else acc
        }

    fun part2(): Int =
        input.foldIndexed(0) { i, acc, it ->
            if (i > 2) {
                val aSum = (1..3).sumOf { input[i - it] }
                val bSum = (0..2).sumOf { input[i - it] }
                if (aSum < bSum) acc + 1 else acc
            } else acc
        }

    println("p1: ${part1()}")
    println("p2: ${part2()}")
}
