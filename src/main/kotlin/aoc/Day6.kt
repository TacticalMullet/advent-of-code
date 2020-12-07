package aoc

import java.io.File

/**
 * https://adventofcode.com/2020/day/6
 */

fun main() {
    val input = File(ClassLoader.getSystemResource("202006").file).readText()
        .split("\n\n")
        .map { it.split("\n") }

    val p1 = input.map {
        it.flatMap { it.toList() }.distinct().count()
    }.sum()

    val p2 = input.map {
        it.flatMap { it.toList() }.distinct().filter { c -> it.all { c in it } }.count()
    }.sum()

    println("Part 1: $p1")
    println("Part 2: $p2")
}