package aoc

import java.io.File

/**
 * https://adventofcode.com/2020/day/5
 */

fun main() {
    val input = File(ClassLoader.getSystemResource("202005").file).readText().split("\n")

    val p1 = input
        .map { BoardingPass(it) }
        .maxByOrNull { it.id }!!
        .id

    val p2 = input
        .map { BoardingPass(it) }
        .map { it.id }
        .sortedBy { it }
        .findMissing()

    println("Part 1: $p1")
    println("Part 2: $p2")
}

class Location(
    val row: Int,
    val column: Int
)

class BoardingPass(
    val d: String,
    var location: Location = Location(0, 0),
    var id: Int = 0
) {
    init {
        location = findLocation()
        id = location.row * 8 + location.column
    }

    private fun findLocation() = d.fold((0 to 127) to (0 to 7), { (row, column), d ->
        when(d.toString()) {
            "B" -> { row.upperHalf() to column }
            "F" -> { row.lowerHalf() to column }
            "R" -> { row to column.upperHalf() }
            "L" -> { row to column.lowerHalf() }
            else -> throw Exception("halp. $d")
        }
    }).let {
        require(it.first.first == it.first.second ) { "${it.first.first} != ${it.first.second}" }
        require(it.second.first == it.second.second) { "${it.second.first} != ${it.second.second}" }
        Location(it.first.first, it.second.second) // ghetto
    }
}

fun Pair<Int, Int>.upperHalf(): Pair<Int, Int> = if (second - first < 2) second to second else mid() to second
fun Pair<Int, Int>.lowerHalf(): Pair<Int, Int> = if (second - first < 2) first to first else first to mid() - 1
fun Pair<Int, Int>.mid(): Int = second - ((second - first) / 2)
fun List<Int>.findMissing(): Int {
    for(i in first()..last()) {
        if (i !in this) return i
    }
    throw Exception("ain't missing")
}
