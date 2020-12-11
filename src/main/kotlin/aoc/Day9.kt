package aoc

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("202009").file).readText().split("\n").map { it.toLong() }

    fun List<Long>.sumsTo(sum: Long): List<Pair<Long, Long>>? = withIndex().run {
        this.mapNotNull { currentNumber ->
            this.firstOrNull { it != currentNumber && it.value + currentNumber.value == sum }
                ?.let { currentNumber.value to it.value }
        }
    }.takeUnless { it.isEmpty() }

    fun List<Long>.sumsToContiguous(sum: Long): List<Long>? {
        this.forEachIndexed { i, _ ->
            this.forEachIndexed { j, _ ->
                if (j > i && this.subList(i, j).reduce { a, b -> a + b } == sum) return this.subList(i, j)
            }
        }
        return null
    }

    fun List<Long>.valid(index: Int, priorNumbers: Int): Boolean =
        this.withIndex()
            .filter { it.index in index-priorNumbers..index }
            .map { it.value }
            .sumsTo(this[index]) != null

    val p1 = input.mapIndexed { i, it -> it to (i < 24 || input.valid(i, 25)) }
        .filterNot { it.second }
        .first()
        .first

    val p2 = input.sumsToContiguous(p1)
        ?.sorted()
        ?.let { it.first() + it.last() }

    println("Part 1: $p1")
    println("Part 2: $p2")
}

