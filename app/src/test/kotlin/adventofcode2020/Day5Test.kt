package adventofcode2020

import adventofcode2020.Year
import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws

class Day5Test { 
    @Test fun testStringToBinary() {
        assertEquals(9,"1001".toNum(one = '1', zero = '0'))
        assertEquals(9,"BFFB".toNum(one = 'B', zero = 'F'))
        assertEquals(9,"BFFBRLLR".toNum(one = 'B', zero = 'F'))
        assertEquals(9,"BFFBRLLR".toNum(one = 'R', zero = 'L'))
        assertEquals(44,"FBFBBFFRLR".toNum(one = 'B', zero = 'F'))
    }
}

