package aoc

import java.io.File

fun main() {

    val input = File(ClassLoader.getSystemResource("202010").file)
        .readText()
        .split("\n")
        .map { it.toInt() }
        .plus(0) // account for outlet
        .sorted()

    val p1 = input.let { it.plus(it.last() + 3) } // account for device
        .zipWithNext { a, b -> b - a }
        .let { it.count { it == 1 } * it.count { it == 3 } }

    fun part2(): Long {
        return input.fold(listOf(Adapter(0, 0L))) { pairs, cur ->
            // sum totals of previous adapters with volts within 3 of current volts
            val sum = pairs
                .takeLastWhile { it.volts >= cur - 3 }
                .map { it.total }
                .sum()
                .takeIf { it > 0L } ?: 1L

            pairs.plus(Adapter(cur, sum)) // add new total for current number to list
        }.last().total
    }

    val p2 = part2()

    println("Part1: $p1")
    println("Part2: $p2")
}

class Adapter(
    val volts: Int,
    val total: Long
)
