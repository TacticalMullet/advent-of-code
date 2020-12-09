package aoc

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("202008").file).readText().split("\n").toMutableList()

    fun p(instructions: List<String>): Int {
        var i = 0
        var indexesExecuted = emptyList<Int>()
        var acc = 0

        while (i < instructions.size) {
            if (indexesExecuted.contains(i)) throw Exception("$acc")
            indexesExecuted = indexesExecuted.plus(i)
            val instruction = instructions[i]
            when(instruction.take(3)) {
                "nop" -> { i++ }
                "acc" -> {
                    acc += instruction.takeLast(instruction.length - 4).toInt()
                    i++
                }
                "jmp" -> {
                    i += instruction.takeLast(instruction.length - 4).toInt()
                }
            }
        }
        return acc
    }

    fun MutableList<String>.rep(c: Int): List<String> =
        when(input[c].take(3)) {
            "jmp" -> { this.mapIndexed { i, s -> if (i == c) s.replace("jmp", "nop") else s } }
            "nop" -> { this.mapIndexed { i, s -> if (i == c) s.replace("nop", "jmp") else s } }
            "acc" -> { rep(c + 1) }
            else -> this
        }

    fun p2(): Int {
        var final = 0
        var counter = 0

        while (counter < input.size) {
            try {
                val r = input.rep(counter)
                final = p(r)
                counter = input.size + 1
            } catch (t: Throwable) {
                counter += 1
            }
        }
        return final
    }

    val p1 = try { p(input) } catch (t: Throwable) { t.message }
    val p2 = p2()
    println("Part 1: $p1")
    println("Part 2: $p2")

}
