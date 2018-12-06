package day6

import java.io.File
import kotlin.system.measureTimeMillis

val input = File("src/day6/input").readLines()
val chars = (IntRange(65, 90) + IntRange(97, 122)).map { it.toChar() }

private val coordinates = input.mapIndexed { i, line ->
    val parts = line.split(", ")
    Coordinate(chars[i], parts[0].toInt(), parts[1].toInt())
}

val x = coordinates.sortedByDescending { it.x }[0].x + 1
val y = coordinates.sortedByDescending { it.y }[0].y + 1

// Calculate which x/y define the boundaries.
val top = coordinates.minBy { it.y }!!.y
val bottom = coordinates.maxBy { it.y }!!.y
val left = coordinates.minBy { it.x }!!.x
val right = coordinates.maxBy { it.x }!!.x

fun main(args: Array<String>) {
    var time = measureTimeMillis {
        println(p1())
    }
    println("part 1 took $time ms")

    time = measureTimeMillis {
        println(p2())
    }
    println("part 2 took $time ms")
}

private fun p1(): Int {
    val grid = Array(x) { Array(y) { '-' } }

    // Put coordinate names on grid.
    coordinates.forEach {
        grid[it.x][it.y] = it.name
    }

    // Assign closest coordinate name to each location.
    val nearbyMap = coordinates.associate { it to 1 }.toMutableMap()
    for (i in 0 until x) {
        for (j in 0 until y) {
            if (coordinates.any { it.x == i && it.y == j }) {
                // This location is a defined coordinate.
                continue
            }

            val distances = coordinates.map {
                Pair(Coordinate(it.name, i, j).manhattanDistance(it), it)
            }.sortedBy { it.first }

            if (distances[0].first != distances[1].first) {
                val coordinate = distances[0].second
                nearbyMap[coordinate] = nearbyMap[coordinate]!! + 1
                grid[i][j] = coordinate.name + 32
            } else {
                grid[i][j] = '.'
            }
        }
    }

    return nearbyMap.filter { it.isFiniteArea(grid) }.maxBy { it.value }!!.value
}

private fun p2(): Int {
    val grid = Array(x) { Array(y) { '-' } }

    // Put coordinate names on grid.
    coordinates.forEach {
        grid[it.x][it.y] = it.name
    }

    for (i in 0 until x) {
        for (j in 0 until y) {
            val location = Coordinate('#', i, j)
            val manhattanDistanceSum = coordinates.sumBy {
                location.manhattanDistance(it)
            }
            if (manhattanDistanceSum < 10000) {
                grid[i][j] = location.name
            }
        }
    }

    return grid.flatten().count { it == '#'}
}

fun Array<Array<Char>>.print() {
    forEach {
        it.forEach { n ->
            print("$n ")
        }
        println()
    }
}


private fun Map.Entry<Coordinate, Int>.isFiniteArea(grid: Array<Array<Char>>): Boolean {
    // For each coordinate we must check if they don't stretch out to one of the boundaries.
    // If it does, this area is infinite.
    val self = arrayOf(key.name, key.name + 32)
    return key.x in (top - 1)..(bottom + 1) && key.y in (left + 1)..(right - 1) &&
            grid[left][key.y] !in self &&
            grid[right][key.y] !in self &&
            grid[key.x][top] !in self &&
            grid[key.x][bottom] !in self
}

private fun Coordinate.manhattanDistance(other: Coordinate) =
    Math.abs(this.x - other.x) + Math.abs(this.y - other.y)

data class Coordinate(val name: Char, val x: Int, val y: Int)
