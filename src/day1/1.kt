package day1

import java.io.File

fun main(args: Array<String>) {
    println(p1())
    println(p2())
}

private fun p1() = File("src/day1/input").readLines().sumBy { it.toInt() }

private fun p2(): Int {
    val input = File("src/day1/input").readLines().map { it.toInt() }

    var frequency = 0
    val seen = mutableSetOf(frequency)

    while (true) {
        input.forEach {
            frequency += it

            if (!seen.add(frequency)) {
                return frequency
            }
        }
    }
}
