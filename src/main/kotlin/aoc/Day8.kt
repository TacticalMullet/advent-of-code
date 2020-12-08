package aoc

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("202008").file).readText().split("\n").toMutableList()

    fun fart(instructions: List<String>): Int {
        var acc = 0
        var i = 0
        var indexesExecuted = emptyList<Int>()
        acc = 0
        while (i < instructions.size) {
            if (indexesExecuted.contains(i)) throw Exception("$i, ${instructions[i]}, $acc")
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
            "jmp" -> {
                val n = this[c].replace("jmp", "nop")
                val newList = this.toMutableList()
                newList[c] = n
                newList
            }
            "nop" -> {
                val n = this[c].replace("nop", "jmp")
                val newList = this.toMutableList()
                newList[c] = n
                newList
            }
            "acc" -> { rep(c + 1) }
            else -> this
        }

    val p1 = try { fart(input) } catch (t: Throwable) { println(t.message)}


    fun p2(): Int {
        var final = 0
        var counter = 0

        while (counter < input.size) {
            println("$counter -> ${input.size}")
            try {
                val r = input.rep(counter)
                final = fart(r)
                counter = input.size + 1
            } catch (t: Throwable) {
                println(t)
                counter += 1
            }
        }
        return final
    }

    val p2 = p2()
    println("Part 2: $p2")

}