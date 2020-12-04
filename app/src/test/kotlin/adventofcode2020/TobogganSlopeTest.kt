package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals

class TobogganSlopeTest {
    val testSlope: TobogganSlope by lazy {
        TobogganSlope(TEST_SLOPE)
    }

    @Test fun testSlopesDownOne() {
        assertEquals(1, testSlope.slide(3, 1))
        assertEquals(4, testSlope.slide(1, 1))
        assertEquals(4, testSlope.slide(0, 1))
    }

    @Test fun testSlopesDownTwo() {
        assertEquals(1, testSlope.slide(1, 2))
        assertEquals(3, testSlope.slide(2, 2))
    }

    fun testSlope(): TobogganSlope {
        return TobogganSlope(TEST_SLOPE)
    }

// #...#
// ##..#
// #.#.#
// #..##
// #...#

    companion object {
        val TEST_SLOPE = listOf("#...#","##..#","#.#.#","#..##","#...#")
    }
}
