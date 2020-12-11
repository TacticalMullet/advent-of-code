package aoc

import java.io.File
import java.lang.Integer.max

fun main() {

    val input = File(ClassLoader.getSystemResource("202011").file)
        .readText()
        .split("\n")
        .map { it.map { Seat.values().first { s -> s.state == it.toString() } } }
        .let { Layout(it) }

    fun p1(): Int {
        var counter = 0

        var a = input
        var b = a.shuffleP1()

        while(a != b) {
            counter += 1
            a = b
            b = b.shuffleP1()
        }
        return a.numOccupied()
    }

    fun p2(): Int {
        var counter = 0

        var a = input
        var b = a.shuffleP2()

        while(a != b) {
            counter += 1
            a = b
            b = b.shuffleP2()
        }
        return a.numOccupied()
    }

    println("Part1: ${p1()}")
    println("Part2: ${p2()}")

}

enum class Seat(val state: String) {
    OCCUPIED("#"),
    EMPTY("L"),
    FLOOR(".");

    override fun toString(): String {
        return this.state
    }
}

class Layout(val seats: List<List<Seat>>) {

    val rows = seats.size - 1
    val cols = seats[0].size - 1

    override fun toString(): String  = seats.map { it.joinToString("") }.joinToString("\n")
    override fun equals(other: Any?): Boolean { return toString() == other.toString() }

    fun numOccupied() = seats.flatten().count { it == Seat.OCCUPIED }

    fun shuffleP1(): Layout {
        fun getNearbySeats(i: Int, j: Int): List<Seat> {
            fun List<List<Seat>>.getOrEmpty(i: Int, j: Int): Seat = try { this[i][j] } catch (e: IndexOutOfBoundsException) { Seat.FLOOR }

            return listOf(
                seats.getOrEmpty(i-1, j-1),
                seats.getOrEmpty(i-1, j),
                seats.getOrEmpty(i-1, j+1),
                seats.getOrEmpty(i, j-1),
                seats.getOrEmpty(i, j+1),
                seats.getOrEmpty(i+1, j-1),
                seats.getOrEmpty(i+1, j),
                seats.getOrEmpty(i+1, j+1)
            )
        }

        return seats.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when(c) {
                    Seat.EMPTY -> {
                        if (getNearbySeats(i,j).none { it == Seat.OCCUPIED }) Seat.OCCUPIED else c
                    }
                    Seat.OCCUPIED -> {
                        val nearbySeats = getNearbySeats(i, j)
                        val count = nearbySeats.count { it == Seat.OCCUPIED }
                        if (getNearbySeats(i, j).count { it == Seat.OCCUPIED } >= 4) Seat.EMPTY else c
                    }
                    else -> c
                }
            }
        }.let { Layout(it) }
    }

    fun shuffleP2(): Layout {
        fun getVisibleSeats(i: Int, j: Int): List<Seat> {
            fun List<List<Seat>>.getOrEmpty(i: Int, j: Int): Seat = try { this[i][j] } catch (e: IndexOutOfBoundsException) { Seat.FLOOR }

            val max = max(max(i, j),max(100-i, 100-j))

            val north = (1..max).firstOrNull { seats.getOrEmpty(i-it, j) != Seat.FLOOR }?.let { seats.getOrEmpty(i - it, j) } ?: Seat.FLOOR
            val northeast = (1..max).firstOrNull { seats.getOrEmpty(i-it, j+it) != Seat.FLOOR }?.let { seats.getOrEmpty(i-it, j+it) } ?: Seat.FLOOR
            val east = (1..max).firstOrNull { seats.getOrEmpty(i, j+it) != Seat.FLOOR }?.let { seats.getOrEmpty(i, j+it) } ?: Seat.FLOOR
            val southeast = (1..max).firstOrNull { seats.getOrEmpty(i+it, j+it) != Seat.FLOOR }?.let { seats.getOrEmpty(i+it, j+it) } ?: Seat.FLOOR
            val south = (1..max).firstOrNull { seats.getOrEmpty(i+it, j) != Seat.FLOOR }?.let { seats.getOrEmpty(i+it, j) } ?: Seat.FLOOR
            val southwest = (1..max).firstOrNull { seats.getOrEmpty(i+it, j-it) != Seat.FLOOR }?.let { seats.getOrEmpty(i+it, j-it) } ?: Seat.FLOOR
            val west = (1..max).firstOrNull { seats.getOrEmpty(i, j-it) != Seat.FLOOR }?.let { seats.getOrEmpty(i, j-it) } ?: Seat.FLOOR
            val northwest = (1..max).firstOrNull { seats.getOrEmpty(i-it, j-it) != Seat.FLOOR }?.let { seats.getOrEmpty(i-it, j-it) } ?: Seat.FLOOR

            return listOf(north, northeast, east, southeast, south, southwest, west, northwest )
        }

        return seats.mapIndexed { i, s ->
            s.mapIndexed { j, c ->
                when(c) {
                    Seat.EMPTY -> {
                        if (getVisibleSeats(i,j).none { it == Seat.OCCUPIED }) Seat.OCCUPIED else c
                    }
                    Seat.OCCUPIED -> {
                        val nearbySeats = getVisibleSeats(i, j)
                        val count = nearbySeats.count { it == Seat.OCCUPIED }
                        if (getVisibleSeats(i, j).count { it == Seat.OCCUPIED } >= 5) Seat.EMPTY else c
                    }
                    else -> c
                }
            }
        }.let { Layout(it) }
    }
}

