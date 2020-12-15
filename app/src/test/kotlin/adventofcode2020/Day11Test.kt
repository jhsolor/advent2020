package adventofcode2020

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    val source1 = listOf(".L.", "LLL", ".L.")
    val sc1 = { SeatingChart(source1) }
    val source2: List<String> by lazy {
        Resource("11a").lines
    }

    @Test fun testMakeSeats() {
        val rows = source1.toSeatRows()
        println(rows)
        assertEquals(3, rows.size)
        assertEquals(3, rows[0].size)
        for (row in rows) {
            for (seat in row) {
                println(seat)
                assertThat(seat, isA<Seat>())
            }
        }
    }

    @Test fun testRows() {
        val adj = sc1().adjacentSeats(1, 1)
        assertEquals(4, adj.filter { it == Seat.Floor }.size)
        assertEquals(4, adj.filter { it == Seat.Empty }.size)
    }

    @Test fun testMutate() {
        val sc = sc1()
        val adj = sc.adjacentSeats(1, 1)
        assertEquals(Mutate.Occupy, sc.evaluate(adj))
    }

    @Test fun testFirstShuffle() {
        val sc = sc1()
        sc.shuffle()
        for (mutate in sc.mutateCoordinates) {
            println(mutate)
            assertEquals(Mutate.Occupy, mutate.mut)
        }
        assertEquals(5, sc.mutateCoordinates.size)
    }

    @Test fun testApply() {
        val sc = sc1()
        assertEquals(false, sc.apply())
        sc.shuffle()
        assertEquals(true, sc.apply())
        for (row in sc.rows) {
            for (seat in row.filter { it != Seat.Floor }) {
                assertEquals(Seat.Occupied, seat)
            }
        }
    }

    @Test fun testMain() {
        val sc = sc1()
        sc.main()
        assertEquals(Seat.Empty, sc.rows[1][1])
    }
}
