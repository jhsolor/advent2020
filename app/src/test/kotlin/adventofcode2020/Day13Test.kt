package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test fun testModuloTargets() {
        assertEquals(328.toLong(), offsetModulo(1, Pair(5,3), Pair(7,6), Pair(11,9)))
    }

    @Test fun testPotentialDepartures() {
        val timetable = "7,13,x,x,59,x,31,19".toPotentialBusDepartures()
        println(timetable)
        assertEquals(0,timetable.elementAt(0).second)
        assertEquals(12,timetable.elementAt(4).second)
    }

    @Test fun testSolve2() {
        val t1 = "67,7,59,61".toPotentialBusDepartures()
        val t2 = "67,x,7,59,61".toPotentialBusDepartures()
        val t3 = "67,7,x,59,61".toPotentialBusDepartures()
        val t4 = "1789,37,47,1889".toPotentialBusDepartures()
        assertEquals(754018, timestamp(t1))
        assertEquals(779210, timestamp(t2))
        assertEquals(1261476, timestamp(t3))
        assertEquals(1202161486, timestamp(t4))
    }
}
