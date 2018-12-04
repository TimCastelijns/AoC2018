package day3

import substringBetween
import java.io.File

val input = File("src/day3/input").readLines()

private val claims = input.map { line ->
    val id = line.substringBetween("#", " @").toInt()
    val x = line.substringBetween("@ ", ",").toInt()
    val y = line.substringBetween(",", ":").toInt()
    val w = line.substringBetween(": ", "x").toInt()
    val h = line.substringAfter("x").toInt()
    Claim(id, x, y, w, h)
}

fun main(args: Array<String>) {
    println(p1())
    println(p2())
}

private fun p1(): Int {
    val grid = constructGrid()
    return grid.sumBy { it.filter { it >= 2 }.map { 1 }.sum() }
}

private fun p2(): Int {
    val grid = constructGrid()

    repeat(1000) { row ->
        repeat(1000) { col ->
            if (grid[row][col] == 1) {
                // This might be the start of a claim that does not overlap.
                // Check if there is a claim that has this row,col as starting point.
                val claim = claims.firstOrNull { claim -> claim.x == row && claim.y == col }
                claim?.let {
                    // Now check if the rest of the claim also does not.
                    var noOverlap = true
                    for (i in claim.x until claim.x + claim.w) {
                        for (j in claim.y until claim.y + claim.h) {
                            if (grid[i][j] > 1) {
                                noOverlap = false
                            }
                        }
                    }

                    if (noOverlap) {
                        return claim.id
                    }
                }
            }
        }
    }

    return -1
}

private fun constructGrid(): Array<Array<Int>> {
    val grid = Array(1000) { Array(1000) { 0 } }
    claims.forEach { claim ->
        // grid[y][x] is the starting point.
        // Draw the rest from here up until the borders defined by x + w and y + h.
        for (i in claim.x until claim.x + claim.w) {
            for (j in claim.y until claim.y + claim.h) {
                grid[i][j] += 1
            }
        }
    }
    return grid
}

private fun Array<Array<Int>>.print() {
    forEach {
        it.forEach { n ->
            print("$n ")
        }
        println()
    }
}

data class Claim(val id: Int, val x: Int, val y: Int, val w: Int, val h: Int)
