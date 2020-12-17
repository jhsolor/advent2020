package adventofcode2020

class Day11(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        val sc = SeatingChart(resource.lines)
        sc.main()
        var occupied: Long = 0
        for (row in sc.rows) {
            for (seat in row) if (seat == Seat.Occupied) occupied++
        }
        return occupied
    }
    override fun solve2(): Long {
        val sc = SeatingChart(resource.lines, FirstVisibleSeatingStrategy(), 5)
        sc.main()
        var occupied: Long = 0
        for (row in sc.rows) {
            for (seat in row) if (seat == Seat.Occupied) occupied++
        }
        return occupied
    }
}

// L...L.L
// LLLL.LL
// LL.LL

fun List<String>.toSeatRows(): Array<Array<Seat>> {
    val r = mutableListOf<Array<Seat>>()
    for (line in this) {
        val row = Array(line.length) { Seat.Floor }
        var j = 0
        for (seat in line.toCharArray()) {
            row[j] = when (seat) {
                'L' -> Seat.Empty
                '#' -> Seat.Occupied
                else -> Seat.Floor
            }
            j++
        }
        r.add(row)
    }
    return r.toTypedArray()
}

fun Array<Array<Seat>>.dump() {
    for (row in this) {
        for (seat in row) {
            var c = "$seat".toCharArray()[0]
            var s = when (c) {
                'O' -> '#'
                'E' -> 'L'
                'F' -> '.'
                else -> ' '
            }
            print(s)
        }
        print("\n")
    }
}

interface SeatingStrategy {
    fun nearbySeats(rows: Array<Array<Seat>>, col: Int, row: Int): List<Seat>
}

class SeatingChart(private val rowSource: List<String>, val seatStrategy: SeatingStrategy = AdjacentSeatingStrategy(), private val maxNearby: Int = 4) {
    val rows: Array<Array<Seat>> by lazy {
        rowSource.toSeatRows()
    }

    val width: Int by lazy {
        rows[0].size
    }

    val height: Int by lazy {
        rows.size
    }

    val mutateCoordinates = mutableListOf<Mutator>()

    fun main() {
        var changes = true
        var i = 1
        while (changes) {

            shuffle()

            changes = apply()
            i++
        }
    }

    fun shuffle() {
        var row = 0
        while (row < height) {
            var col = 0
            while (col < width) {
                if (rows[row][col] != Seat.Floor) {
                    val m = evaluate(seatStrategy.nearbySeats(rows, col, row))
                    val mut = Mutator(row, col, m)

                    mutateCoordinates.add(mut)
                }
                col++
            }
            row++
        }
    }

    fun apply(): Boolean {
        var changes = false
        while (mutateCoordinates.size > 0) {
            val mc = mutateCoordinates.removeAt(0)
            if (mc.mut == Mutate.NoChange) continue
            var newSeat = when (mc.mut) {
                Mutate.Occupy -> Seat.Occupied
                Mutate.Vacate -> Seat.Empty
                else -> throw IllegalStateException()
            }
            if (rows[mc.row][mc.col] == newSeat) continue
            else rows[mc.row][mc.col] = newSeat
            changes = true
        }
        return changes
    }

    fun evaluate(adjacent: List<Seat>): Mutate {

        val occupied = adjacent.filter { it == Seat.Occupied }
        return when {
            occupied.size == 0 -> Mutate.Occupy
            occupied.size >= maxNearby -> Mutate.Vacate
            else -> Mutate.NoChange
        }
    }
}

abstract class RowSeatingStrategy() : SeatingStrategy {
    var width = 0
    var height = 0

    override fun nearbySeats(rows: Array<Array<Seat>>, col: Int, row: Int): List<Seat> {

        width = rows[0].size
        height = rows.size
        val seats = mutableListOf<Seat?>()
        var i = -1
        while (i <= 1) {
            var j = -1
            while (j <= 1) {
                if (i == 0 && j == 0) { j++; continue }
                seats.add(seat(rows, col, row, i, j))
                j++
            }
            i++
        }

        return seats.filterIsInstance<Seat>()
    }

    abstract fun seat(rows: Array<Array<Seat>>, col: Int, row: Int, horiz: Int, vert: Int): Seat?

    fun seek(rows: Array<Array<Seat>>, col: Int, row: Int, horizontal: Int, vertical: Int, min_x: Int, max_x: Int, min_y: Int, max_y: Int): Seat? {

        var x = col
        var y = row
        // seek to the boundary
        while (y >= min_y && y <= max_y && x >= min_x && x <= max_x) {

            if (x != col || y != row) {
                try {

                    if (rows[y][x] != Seat.Floor) return rows[y][x]
                } catch (ex: ArrayIndexOutOfBoundsException) {}
            }
            x += horizontal
            y += vertical
        }
        return null
    }
}

class AdjacentSeatingStrategy() : RowSeatingStrategy() {
    override fun seat(rows: Array<Array<Seat>>, col: Int, row: Int, horiz: Int, vert: Int): Seat? {
        return seek(rows, col, row, horiz, vert, col - 1, col + 1, row - 1, row + 1)
    }
}

class FirstVisibleSeatingStrategy() : RowSeatingStrategy() {
    override fun seat(rows: Array<Array<Seat>>, col: Int, row: Int, horiz: Int, vert: Int): Seat? {
        return seek(rows, col, row, horiz, vert, 0, width, 0, height)
    }
}

data class Mutator(val row: Int, val col: Int, val mut: Mutate)

enum class Mutate {
    Vacate, Occupy, NoChange
}

enum class Seat {
    Empty, Occupied, Floor
}
