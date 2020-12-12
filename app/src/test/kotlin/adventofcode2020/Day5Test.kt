package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    @Test fun testStringToBinary() {
        assertEquals(9, "1001".toNum(one = '1', zero = '0'))
        assertEquals(9, "BFFB".toNum(one = 'B', zero = 'F'))
        assertEquals(9, "BFFBRLLR".toNum(one = 'B', zero = 'F'))
        assertEquals(9, "BFFBRLLR".toNum(one = 'R', zero = 'L'))
        assertEquals(44, "FBFBBFFRLR".toNum(one = 'B', zero = 'F'))
    }
}
