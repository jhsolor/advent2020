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
    val source3 = listOf("..#..", ".....", "#.L..", ".L...", "....L")
    val sc3 = { SeatingChart(source3, FirstVisibleSeatingStrategy()) }

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

    @Test fun testAdjacentSeatingStrategy() {
        val rows = source1.toSeatRows()
        val adj = AdjacentSeatingStrategy().nearbySeats(rows, 1, 1)
        assertEquals(4, adj.filter { it == Seat.Empty }.size)
    }

    @Test fun testMutate() {
        val sc = sc1()
        val rows = source1.toSeatRows()
        val adj = AdjacentSeatingStrategy().nearbySeats(rows, 1, 1)
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
        assertThat(sc.seatStrategy, isA<AdjacentSeatingStrategy>())
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

    @Test fun testMainBigData() {
        val d = Day11(Resource("11a"))
        assertEquals(37.toLong(), d.solve1())
    }

    @Test fun testMainProdData() {
        val d = Day11(Resource("11"))
        assertEquals(2424.toLong(), d.solve1())
    }

    @Test fun testSeek() {
        source3.toSeatRows().dump()
        val sc = sc3()
        assertThat(sc.seatStrategy, isA<FirstVisibleSeatingStrategy>())
        val seats = sc.seatStrategy.nearbySeats(sc.rows, 2, 2)
        assertEquals(2, seats.filter { it == Seat.Empty }.size)
        assertEquals(2, seats.filter { it == Seat.Occupied }.size)
    }

    @Test fun testSolution2BigData() {
        val d = Day11(Resource("11a"))
        assertEquals(26.toLong(), d.solve2())
    }
}
