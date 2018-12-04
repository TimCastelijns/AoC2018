package day4

import substringBetween
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

val input = File("src/day4/input").readLines()

val events = input.map { line ->
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val time = LocalDateTime.parse(
        line.substringBetween("[", "]"),
        dateTimeFormatter
    )

    when {
        "#" in line -> {
            val guardId = line.substringBetween("#", " begins").toInt()
            BeginShiftEvent(time, guardId)
        }
        "l" in line -> SleepEvent(time)
        else -> WakeEvent(time)
    }
}.sortedBy { it.time }

sealed class Event(open val time: LocalDateTime)
data class BeginShiftEvent(override val time: LocalDateTime, val guardId: Int) : Event(time)
data class SleepEvent(override val time: LocalDateTime) : Event(time)
data class WakeEvent(override val time: LocalDateTime) : Event(time)

fun main(args: Array<String>) {
    println(p1())
    println(p2())
}

private fun p1(): Int {
    val sleepMap = constructSleepMap()

    val guardWhoSleptMostId = sleepMap
        .maxBy { it.value.size }!!
        .key

    val sleptMostOnMinute = sleepMap[guardWhoSleptMostId]!!
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }!!
        .key

    return guardWhoSleptMostId * sleptMostOnMinute
}

private fun p2(): Int {
    val sleepMap = constructSleepMap()
        .filter { it.value.isNotEmpty() }

    val sleptMostOnSameMinute = sleepMap
        .maxBy {
            it.value
                .groupingBy { it }
                .eachCount()
                .maxBy { it.value }!!
                .key
        }

    val guardWhoSleptMostOnSameMinuteId = sleptMostOnSameMinute!!.key
    val minute = sleptMostOnSameMinute.value
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }!!
        .key

    return guardWhoSleptMostOnSameMinuteId * minute
}

private fun constructSleepMap(): HashMap<Int, MutableList<Int>> {
    var guardOnDutyId = -1
    lateinit var fellAsleepAt: LocalDateTime
    val sleepMap = hashMapOf<Int, MutableList<Int>>()

    events.forEach { event ->
        when (event) {
            is BeginShiftEvent -> {
                if (!sleepMap.containsKey(event.guardId)) {
                    sleepMap[event.guardId] = mutableListOf()
                }
                guardOnDutyId = event.guardId
            }
            is SleepEvent -> fellAsleepAt = event.time
            is WakeEvent -> {
                val wokeUpAt = event.time
                val minsSlept = ChronoUnit.MINUTES.between(fellAsleepAt, wokeUpAt)

                for (i in 0 until minsSlept) {
                    val localDateTime = fellAsleepAt.plusMinutes(i)
                    sleepMap[guardOnDutyId]?.add(localDateTime.minute)
                }
            }
        }
    }

    return sleepMap
}
