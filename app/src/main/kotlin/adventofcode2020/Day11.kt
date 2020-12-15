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
        TODO()
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

class SeatingChart(private val rowSource: List<String>) {
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
        while (changes) {
            shuffle()
            changes = apply()
        }
    }

    fun shuffle() {
        var row = 0
        while (row < height) {
            var col = 0
            while (col < width) {
                if (rows[row][col] != Seat.Floor) {
                    val m = evaluate(adjacentSeats(row, col))
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

    fun adjacentSeats(row: Int, col: Int): List<Seat> {
        var i = row - 1
        val s = mutableListOf<Seat>()
        while (i <= row + 1) {
            var j = col - 1
            if (i < 0 || i >= height) { i++; continue }
            while (j <= col + 1) {
                if (j < 0 || j >= width || (i == row && j == col)) { j++; continue }
                s.add(rows[i][j])
                j++
            }
            i++
        }
        return s
    }

    fun evaluate(adjacent: List<Seat>): Mutate {
        val occupied = adjacent.filter { it == Seat.Occupied }
        return when (occupied.size) {
            0 -> Mutate.Occupy
            1, 2, 3 -> Mutate.NoChange
            else -> Mutate.Vacate
        }
    }
}

data class Mutator(val row: Int, val col: Int, val mut: Mutate)

enum class Mutate {
    Vacate, Occupy, NoChange
}

enum class Seat {
    Empty, Occupied, Floor
}
