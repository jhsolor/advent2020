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
        assertEquals(0, testSlope.slide(1, 2)) // because we teleport through the first tree
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
