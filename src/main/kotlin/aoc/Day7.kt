package aoc

import java.io.File

/**
 * https://adventofcode.com/2020/day/7
 */

/*
muted lime bags contain 1 wavy lime bag, 1 vibrant green bag, 3 light yellow bags.
light red bags contain 2 clear indigo bags, 3 light lime bags.
wavy beige bags contain 4 faded chartreuse bags.
muted blue bags contain 3 mirrored tan bags.
vibrant cyan bags contain 4 drab beige bags, 4 vibrant maroon bags, 2 dull coral bags.
posh indigo bags contain 1 dim cyan bag, 4 striped violet bags, 2 posh olive bags.
dark black bags contain 5 dotted purple bags, 3 dotted orange bags, 5 shiny gold bags, 3 wavy brown bags.
dull teal bags contain 1 posh aqua bag.
dim aqua bags contain 3 muted indigo bags, 5 vibrant green bags, 3 dotted teal bags.
clear bronze bags contain 1 plaid gold bag, 4 pale tan bags, 1 light teal bag, 5 dim lavender bags.
shiny fuchsia bags contain 5 striped orange bags, 2 faded plum bags.
dim bronze bags contain 2 plaid tan bags, 4 muted green bags.
muted white bags contain 1 wavy black bag, 2 striped olive bags.
wavy maroon bags contain 3 striped magenta bags, 3 bright teal bags, 2 dark crimson bags.
muted beige bags contain 4 dull plum bags, 2 plaid fuchsia bags, 3 clear coral bags, 1 clear red bag.
drab chartreuse bags contain 2 dull gray bags, 2 striped olive bags, 2 dark aqua bags.
plaid turquoise bags contain 1 muted teal bag.
muted maroon bags contain 1 faded chartreuse bag, 1 wavy gray bag, 5 faded black bags, 2 posh tan bags.
muted bronze bags contain 1 muted white bag.
muted teal bags contain 1 striped beige bag.
faded indigo bags contain 5 mirrored green bags.
drab tan bags contain 4 dim lavender bags.
bright turquoise bags contain 2 pale olive bags, 4 posh salmon bags.
dull aqua bags contain 2 dark orange bags, 2 pale aqua bags, 1 faded plum bag.
striped coral bags contain 3 wavy purple bags, 2 dull gray bags.
 */

fun main() {
    val input = File(ClassLoader.getSystemResource("202007").file).readText().split("\n")

    val bags = input.map {
        val color = """(.*) bags contain""".toRegex().find(it)!!.groupValues[1]
        val children = """(\d+) ([a-zA-Z| ]+) bag""".toRegex().findAll(it).map { it.groupValues[1].toInt() to Bag(it.groupValues[2]) }.toList()
        Bag(color, children, it)
    }

    fun getBag(c: String): Bag? = bags.find { it.color == c }
    fun getBag(c: Bag): Bag? = bags.find { it.color == c.color }
    fun Bag.findParents(): List<Bag> = bags.filter { it.children.any { it.second.color == color } }

    fun Bag.p1(v: List<Bag> = listOf()): List<Bag> {
        val parents = findParents()
        return if(parents.isEmpty()) v else parents.flatMap { it.p1(parents.plus(v)) }.distinct()
    }


    fun Bag.p2(v: Long = 1L): Long {
        val bag = getBag(this)!!
        return if (bag.children.isEmpty()) v
        else bag.children.map { it.second.p2(v) * it.first }.sum() + 1
    }

    val p1 = getBag("shiny gold")!!.p1().count()
    val p2 = getBag("shiny gold")!!.p2() - 1

    println("Part 1: $p1")
    println("Part 2: $p2")
}

fun String.numBags(): Pair<Int, Bag> = this.split(" ", limit = 2).let { it[0].toInt() to Bag(it[1]) }

class Bag(
    val color: String,
    var children: List<Pair<Int, Bag>> = emptyList(),
    var parsedFrom: String? = null
)
