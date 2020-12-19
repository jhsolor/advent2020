package adventofcode2020

class Day13(resource: Resource) : ResourceSolver(resource) {
    val departure: Int by lazy {
        resource.lines.elementAt(0).toInt()
    }
    override fun solve1(): Long {
        val times = resource.lines.elementAt(1).toBusDepartures()
        var minWait = departure
        var minWaitDeparture = 0
        for (time in times) {
            val wait = time - departure % time
            if (wait >= minWait) continue
            minWait = wait
            minWaitDeparture = time
        }
        return minWaitDeparture.toLong() * minWait.toLong()
    }
    override fun solve2(): Long {
        val times = resource.lines.elementAt(1).toPotentialBusDepartures()
        return timestamp(times, departure.toLong())
    }
}

fun String.toBusDepartures(): List<Int> {
    return this.split(',').filter { it != "x" }.map { it.toInt() }
}

fun String.toPotentialBusDepartures(): List<Pair<Int, Int>>{
    return this.split(',').mapIndexed { idx, item -> 
        var num = if (item == "x") -1 else item.toInt()
        var offset = num - idx % num
        if (offset == num) offset = 0
        Pair(num, offset)
    }.filter { it.first >= 0 }
}

fun offsetModulo(dividend: Long = 1, vararg divisorRemainders: Pair<Int, Int>): Long {
    var compoundDivisor: Long = 1
//    var found = 0
    var i = dividend - 1
    for (divisorRemainder in divisorRemainders) {
        val (divisor, remainder) = divisorRemainder
        if (remainder >= divisor) throw IllegalArgumentException()
        println(compoundDivisor)
        var j: Long = 0
        while (j != remainder.toLong()) {
            if (i < 0) {
                throw IllegalStateException()
            }
            i += compoundDivisor 
            j = i % divisor
        }
        compoundDivisor *= divisor
        println("Found: $i % $divisor = $j")
    }
    return i
}

fun timestamp(timetable: List<Pair<Int, Int>>, startTime: Long = 1): Long {
    println(timetable)
    val m = offsetModulo(startTime, *(timetable.toTypedArray()))
    println(m)
    return m
}

