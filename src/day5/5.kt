package day5

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.system.measureTimeMillis

private val input = File("src/day5/input").readText()

fun main(args: Array<String>) = runBlocking {
    var time = measureTimeMillis {
        println(p1())
    }
    println("part 1 took $time ms")

    time = measureTimeMillis {
        println(p2())
    }

    println("part 2 took $time ms")
}

private fun p1() = fullyReact(input).length

private suspend fun p2(): Int {
    val jobs = mutableListOf<Deferred<Int>>()

    for (c in 'a'..'z') {
        jobs.add(GlobalScope.async {
            val strippedInput = input.replace(Regex("[$c${c.toUpperCase()}]"), "")
            fullyReact(strippedInput).length
        })
    }

    return jobs.map { it.await() }.minBy { it }!!
}

private fun fullyReact(input: String): String {
    var leftover = input
    var previousLeftover: String
    do {
        previousLeftover = leftover
        leftover = triggerFirstNextReaction(previousLeftover)
    } while (leftover.length < previousLeftover.length)

    return leftover
}

private fun triggerFirstNextReaction(sequence: String): String {
    var indexLeft = 0
    var indexRight = 1

    while (indexRight < sequence.length) {
        val left = sequence[indexLeft]
        val right = sequence[indexRight]

        indexLeft++
        indexRight++

        if (left.reactsWith(right)) {
            // Remove both.
            return sequence.replaceFirst("$left$right", "")
        } else if (indexRight + 1 == sequence.length) {
            // Reached the end. Return what is left
            return sequence
        }
    }

    return "something went wrong"
}

// Difference between upper and lowercase chars on ascii table is always 32.
private fun Char.reactsWith(other: Char) = Math.abs(this - other) == 32
