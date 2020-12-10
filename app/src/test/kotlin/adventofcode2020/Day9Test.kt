
package adventofcode2020

import adventofcode2020.Year
import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws

class Day9Test { 
    @Test fun testFindOutlier() {
        val l = Resource("9a").lines
        val cipher = XmasCipher(l, 5)
        assertEquals(127,cipher.main())
    }

    @Test fun testFindSummers() {
        val l = Resource("9a").lines
        val cipher = XmasCipher(l, 5)
        val list = cipher.findSummers(127)
        val answer: Array<Long> = arrayOf(15, 25, 47, 40)
        assertEquals(4, answer.intersect(list).size)
    }
}

