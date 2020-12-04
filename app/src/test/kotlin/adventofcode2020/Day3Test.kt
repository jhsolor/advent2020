package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Day3Test {
    fun testDay3(): Day3 {
        return Day3(Resource("test"))
    }
    
    fun prodDay3(): Day3 {
        return Day3(Resource("3"))
    } 

    @Test fun testSolve1() {
        assertEquals(1,testDay3().solve1())
    }

    @Test fun testSolve2() {
        assertEquals(0,testDay3().solve2())
    }

    @Test fun testSolve1Prod() {
        assertEquals(151,prodDay3().solve1())
    }

    @Test fun testSolve2Prod() {
        val solution = prodDay3().solve2()
        assertNotEquals(12652101099,solution)
    }
}

