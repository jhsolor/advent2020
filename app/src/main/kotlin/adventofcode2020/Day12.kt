package adventofcode2020

import kotlin.math.absoluteValue
import kotlin.math.sign

class Day12(resource: Resource) : ResourceSolver(resource) {
    private val commands: List<Vector> by lazy {
        resource.lines.map { it.toVector() }
    }

    override fun solve1(): Long {
        val f = Ferry()
        f.execute(commands)
        return f.manhattanDistance().toLong()
    }

    override fun solve2(): Long {
        val f = WaypointFerry()
        f.execute(commands)
        return f.manhattanDistance().toLong()
    }
}

open class Ferry(protected var x: Long = 0, protected var y: Long = 0, var facing: Direction = Direction.East) {

    open fun execute(commands: List<Vector>): Ferry {
        for (command in commands) {
            when (command.direction) {
                Direction.Forward -> goForward(command.magnitude)
                Direction.Right, Direction.Left -> facing = turn(facing, command)
                else -> slide(command.direction, command.magnitude)
            }
        }
        return this
    }

    fun manhattanDistance(): Long {
        return x.absoluteValue + y.absoluteValue
    }

    fun coordinates(): Pair<Long, Long> {
        return Pair(x, y)
    }

    protected open fun goForward(magnitude: Int) {
        slide(facing, magnitude)
    }

    private fun slide(direction: Direction, magnitude: Int) {
        when (direction) {
            Direction.North -> y -= magnitude
            Direction.East -> x += magnitude
            Direction.South -> y += magnitude
            Direction.West -> x -= magnitude
            else -> throw IllegalArgumentException("Can only slide a cardinal direction")
        }
    }
}

class WaypointFerry(private val waypoint: Coordinates = Coordinates(10, -1), x: Long = 0, y: Long = 0) : Ferry(x, y, Direction.North) {

    override fun execute(commands: List<Vector>): WaypointFerry {
        for (command in commands) {
            when (command.direction) {
                Direction.Forward -> goForward(command.magnitude)
                Direction.Right, Direction.Left -> waypoint.rotate(command)
                else -> waypoint.slide(command)
            }
        }
        return this
    }

    override fun goForward(magnitude: Int) {
        val mutx = waypoint.x * magnitude
        val muty = waypoint.y * magnitude
        x += mutx
        y += muty
    }
}

enum class Direction {
    North, East, South, West, Forward, Right, Left
}

fun turn(facing: Direction, command: Vector): Direction {
    val directions = arrayOf(Direction.North, Direction.East, Direction.South, Direction.West)
    val start = directions.indexOf(facing)
    return directions[quadrantShift(start, command)]
}

private fun quadrantShift(startQuadrant: Int, command: Vector): Int {
    val circle = if (command.magnitude > 360) { command.magnitude % 360 } else { command.magnitude }
    val sign = when (command.direction) {
        Direction.Right -> 1
        Direction.Left -> -1
        else -> throw IllegalArgumentException("$command is not a turning command")
    }
    val quadrants = circle / 90 * sign
    val rotation = startQuadrant + quadrants
    return when {
        rotation < 0 -> 4 + rotation
        rotation > 3 -> rotation - 4
        else -> rotation
    }
}

data class Coordinates(var x: Int, var y: Int) {

    fun rotate(command: Vector, xc: Int = 0, yc: Int = 0) {
        // could do matrix but maybe they won't always be 90s
        val x0 = x
        val y0 = y
        val sign_theta = when {
            command.direction == Direction.Left -> 1
            else -> -1
        }
        val theta = command.magnitude * sign_theta
        x = (x0 - xc) * cosine(theta) - (y0 - yc) * sine(theta) + xc
        y = (x0 - xc) * sine(theta) + (y0 - yc) * cosine(theta) + yc
    }

    private fun sine(degrees: Int): Int {
        var t = degrees % 360
        if (t < 0) t += 360
        val v = when (t) {
            90 -> -1
            0, 180 -> 0
            270 -> 1
            else -> throw IllegalArgumentException("What am I, kotlin.math??")
        }
        return v
    }

    private fun cosine(degrees: Int): Int {
        return -1 * sine(degrees + 90)
    }

    fun slide(v: Vector) {
        when (v.direction) {
            Direction.North -> y -= v.magnitude
            Direction.East -> x += v.magnitude
            Direction.South -> y += v.magnitude
            Direction.West -> x -= v.magnitude
            else -> throw IllegalArgumentException("Can only slide a cardinal direction")
        }
    }
}

fun Coordinates.toPair(): Pair<Int, Int> {
    return Pair(this.x, this.y)
}

data class Vector(val direction: Direction, val magnitude: Int)

fun String.toVector(): Vector {
    val ca = this.toCharArray()
    val dir = when (ca[0]) {
        'N' -> Direction.North
        'S' -> Direction.South
        'E' -> Direction.East
        'W' -> Direction.West
        'R' -> Direction.Right
        'L' -> Direction.Left
        'F' -> Direction.Forward
        else -> IllegalArgumentException("${ca[0]} cannot be mapped to a direction")
    }
    val mag = this.substring(1).toInt()
    return Vector(dir as Direction, mag)
}
