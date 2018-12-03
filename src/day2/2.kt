package day2

import java.io.File

val input = File("src/day2/input").readLines()

fun main(args: Array<String>) {
    println(p1())
    println(p2())
}

private fun p1(): Int {
    var twos = 0
    var threes = 0

    input.forEach { line ->
        val counts = mutableMapOf<Char, Int>()
        line.groupingBy { it }.eachCountTo(counts)
        println(counts.toString())
        if (counts.containsValue(2)) {
            twos++
        }
        if (counts.containsValue(3)) {
            threes++
        }
    }
    return twos * threes
}

private fun p2(): String {
    input.forEachIndexed { i1, line1 ->
        input.forEachIndexed inner@{ i2, line2 ->
            val differentChars = mutableListOf<Pair<Char, Int>>()

            // Compare both lines char by char
            line1.zip(line2).forEachIndexed { i, (c1, c2) ->
                if (c1 != c2) {
                    differentChars.add(Pair(c1, i))
                }
            }

            if (differentChars.size == 1) {
                val commonLetters = line1.toMutableList().filterIndexed { i, _ ->
                    i != differentChars[0].second
                }.joinToString("")
                return commonLetters
            }
        }
    }

    return "none"
}
