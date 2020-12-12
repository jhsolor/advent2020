
package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day9Test {
    @Test fun testFindOutlier() {
        val l = Resource("9a").lines
        val cipher = XmasCipher(l, 5)
        assertEquals(127, cipher.main())
    }

    @Test fun testFindSummers() {
        val l = Resource("9a").lines
        val cipher = XmasCipher(l, 5)
        val list = cipher.findSummers(127)
        val answer: Array<Long> = arrayOf(15, 25, 47, 40)
        assertEquals(4, answer.intersect(list).size)
    }
}
