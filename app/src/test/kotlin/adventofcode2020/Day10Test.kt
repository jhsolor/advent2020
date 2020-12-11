package adventofcode2020

import adventofcode2020.Year
import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws

class Day10Test { 
    val j1 = { JoltageAdapter(1) }
    val j4 = { JoltageAdapter(4) }
    val j6 = { JoltageAdapter(6) }
    val l = listOf("1","4","6")
    val lj = { l.toJoltageAdapters() }
    val ljc = { l.toJoltageAdapters(withCharger = true) }

        
    @Test fun testCanAppendWithinGap() {
        val j = j1()
        j.append(j4())
        assertThat(j, isA<JoltageAdapter>())
        assertEquals(3, j.delta())
    }

    @Test fun testCannotAppendBeyondGap() {
        assertThat({j1().append(j6())}, throws<IllegalArgumentException>())
    }

    @Test fun testCanCreateLinkedAdapters() {
        val j = lj()
    }

    @Test fun testCanFindMax() {
        val j = lj()
        assertEquals(6, j.max())
    }

    @Test fun testCanAppendCharger() {
        val j = ljc()
        assertEquals(9, j.max())
    }

    @Test fun testAnalyzeJoltage() {
        val l = ljc()
        val ja = JoltageAnalyzer(l)
        assertEquals(2, ja.deltaDistribution.get(3))
        assertEquals(1, ja.deltaDistribution.get(2))
    }

   @Test fun testDistributeJoltage() {
        val l = Resource("10b").lines.toJoltageAdapters(true)
        println(l)
        val ja = JoltageAnalyzer(l)
        println(ja.deltaDistribution)
        assertEquals(220, ja.multiplySpread())
    }
   @Test fun testPermutations() {
        val ja = Resource("10a").lines.toJoltageAdapters(true)
        assertEquals(8, ja.configs())
    }

   @Test fun testPerm1() {
        val l = listOf("1","2","3","4","5").toJoltageAdapters(true)
        println(l)
        assertEquals(12, l.configs())
   }

    @Test fun testPerm1to6() {
        val l = listOf("1","2","3","4","5","6").toJoltageAdapters(true)
        println(l)
        assertEquals(14, l.configs())
   }
     
   @Test fun testBigPermutations() {
        val jb = Resource("10b").lines.toJoltageAdapters(true)
        assertEquals(19208, jb.configs())

   }
}
