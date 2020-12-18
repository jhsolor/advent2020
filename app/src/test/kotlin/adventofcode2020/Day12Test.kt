package adventofcode2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    val v90r = Vector(Direction.Right, 90)
    val v90l = Vector(Direction.Left, 90)
    val v360r = Vector(Direction.Right, 360)
    val v450r = Vector(Direction.Right, 450)

    @Test fun testTurn() {
        assertEquals(turn(Direction.North, v90r), Direction.East)
        assertEquals(turn(Direction.East, v90r), Direction.South)
        assertEquals(turn(Direction.South, v90r), Direction.West)
        assertEquals(turn(Direction.West, v90r), Direction.North)

        assertEquals(turn(Direction.North, v90l), Direction.West)
        assertEquals(turn(Direction.East, v90l), Direction.North)
        assertEquals(turn(Direction.South, v90l), Direction.East)
        assertEquals(turn(Direction.West, v90l), Direction.South)

        assertEquals(turn(Direction.North, v360r), Direction.North)
        assertEquals(turn(Direction.North, v450r), Direction.East)
    }

    @Test fun testMoveCompassDirections() {
        assertEquals(-1, Ferry().execute(listOf("N1".toVector())).coordinates().second)
        assertEquals(1, Ferry().execute(listOf("E1".toVector())).coordinates().first)
        assertEquals(1, Ferry().execute(listOf("S1".toVector())).coordinates().second)
        assertEquals(-1, Ferry().execute(listOf("W1".toVector())).coordinates().first)
    }

    @Test fun testForward() {
        val f = Ferry()
        val c = listOf("F1".toVector())
        f.execute(c)
        assertEquals(1, f.manhattanDistance())
    }

    @Test fun testManhattanDistance() {
        val d = Day12(Resource("12a"))
        assertEquals(d.solve1(), 25.toLong())
    }

    @Test fun testRotateWaypoint() {
        val c = Coordinates(10, 1) // southeast (east 10, south 1)
        c.rotate(v90r)
        assertEquals(-1, c.x) // southwest (west 1, south 10)
        assertEquals(10, c.y)
        c.rotate(v90r)
        assertEquals(-10, c.x) // northwest (west 10, north 1)
        assertEquals(-1, c.y)
        c.rotate(v90r)
        assertEquals(1, c.x) // northeast (east 1, north 10)
        assertEquals(-10, c.y)
        c.rotate(v90r)
        assertEquals(10, c.x) // southeast (east 10, north 1)
        assertEquals(1, c.y)

        // other way
        c.rotate(v90l)
        assertEquals(1, c.x)
        assertEquals(-10, c.y)
        c.rotate(v90l)
        assertEquals(-10, c.x)
        assertEquals(-1, c.y)
        c.rotate(v90l)
        assertEquals(-1, c.x)
        assertEquals(10, c.y)
        c.rotate(v90l)
        assertEquals(10, c.x)
        assertEquals(1, c.y)
    }

    @Test fun testSolve2() {
        val d = Day12(Resource("12a"))
        assertEquals(286.toLong(), d.solve2())
    }

    @Test fun testSolve2Prod() {
        val d = Day12(Resource("12"))
        val sln = d.solve2()
        println(sln)
        assert(sln > 43386)
        assert(sln < 89829)
    }

    @Test fun testSolve2b() {
        val d = Day12(Resource("12b"))
        assertEquals(0.toLong(), d.solve2())
    }
}
