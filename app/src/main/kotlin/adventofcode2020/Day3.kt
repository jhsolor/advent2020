package adventofcode2020

class Day3(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        val ts = TobogganSlope(resource.lines)
        return ts.slide(3,1).toLong()
    }
    override fun solve2(): Long {
        val ts = TobogganSlope(resource.lines)
        val slopes: List<Pair<Int,Int>> = listOf(Pair(1,1), Pair(3,1), Pair(5,1), Pair(7,1), Pair(1,2))
        var trees: MutableList<Int> = mutableListOf()
        for(slope in slopes) {
            val (x, y) = slope
            val theseTrees = ts.slide(x,y)
            trees.add(theseTrees)
        }
        var multiple: Long = 1
        for(tree in trees) {
            multiple = multiple * tree
        }
        // Not 12652101099
        return multiple
    }
}

class TobogganSlope(val rows: List<String>) {
    // We're going "down a slope"
    // we need a horizontal pointer and a vertical pointer
    // vertical pointer is incrementing one at a time, currently
    private val width: Int by lazy {
        rows[0].length
    }
    private val height: Int by lazy {
        rows.size
    }

    fun slide(over: Int, down: Int): Int{
        var trees = 0
        var x = 0
        var y = 0
        while (y < height) {
            // if y > 1, we have to stop at each x value on the way down the slope...
            x = x + over
            // lazily handle "buffer overflows"
            if (x >= width) x = (x % width)
            // for (i in 1..down) { // lol apparently we fly over trees in between us & our target
            
                y += down
                if (y >= height) {
                    println("Slope ${over}, ${down} encountered ${trees} trees!")
                    return trees
                }
                var row = rows[y].toCharArray()
                if (row[x] == '#') {
                    trees++
                    row[x] = 'X'
                } else {
                    row[x] = 'O'
                }
                val string = String(row)
                println("(${x.format(2)},${y.format(3)}): ${string}")
           // }
        }
        return trees
    }
}

fun Int.format(digits: Int) = "%0${digits}d".format(this)
