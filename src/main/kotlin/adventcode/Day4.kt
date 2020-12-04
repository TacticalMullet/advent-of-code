package adventcode

import java.io.File

fun main() {
    val input = File(ClassLoader.getSystemResource("202004").file).readText()
    fun String.toRequirement(): Requirement = this.split(":").let { (name, value) -> Requirement( name, value ) }

    fun String.parseInputToPassports(): List<Passport> = this.split("\n\n")
        .map { it.replace("\n", " ") }
        .map { it.split(" ") }
        .map { it.map { it.toRequirement() } }
        .map { Passport(it) }

    println("Part 1: ${input.parseInputToPassports().filter { it.allRequiredFieldsPresent() }.count()}")
    println("Part 2: ${input.parseInputToPassports().filter { it.allRequiredFieldsPresent() && it.allFieldsAreValid() }.count()}")
}

class Requirement(
    val name: String,
    val value: String
) {
    fun isValid(): Boolean = when(name) {
        // byr (Birth Year) - four digits; at least 1920 and at most 2002.
        // iyr (Issue Year) - four digits; at least 2010 and at most 2020.
        // eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
        // hgt (Height) - a number followed by either cm or in:
        //     If cm, the number must be at least 150 and at most 193.
        //     If in, the number must be at least 59 and at most 76.
        // hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
        // ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
        // pid (Passport ID) - a nine-digit number, including leading zeroes.
        // cid (Country ID) - ignored, missing or not.

        "byr" -> { { value.length == 4 && value.toInt() in 1920..2002 }.tryOr(false) }
        "iyr" -> { { value.length == 4 && value.toInt() in 2010..2020 }.tryOr(false) }
        "eyr" -> { { value.length == 4 && value.toInt() in 2020..2030 }.tryOr(false) }
        "hgt" -> {
            {
                when {
                    value.matches("""\d{3}cm""".toRegex()) -> { value.take(3).toInt() in 150..193 }
                    value.matches("""\d{2}in""".toRegex()) -> { value.take(2).toInt() in 59..76 }
                    else -> { false }
                }
            }.tryOr(false)
        }
        "hcl" -> { """#[a-zA-Z0-9]{6}""".toRegex().matches(value) }
        "ecl" -> { value in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") }
        "pid" -> { """[0-9]{9}""".toRegex().matches(value) }
        else -> true
    }
}

private fun <R> (() -> R).tryOr(r: R): R = try { invoke() } catch (e: Throwable) { r }

class Passport (private val requirements: List<Requirement>) {
    private val requireds = listOf( "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" )

    fun allFieldsAreValid() = requirements.all { it.isValid() }
    fun allRequiredFieldsPresent(): Boolean = requirements.map { it.name }.containsAll(requireds)
}
