package adventofcode2020

class Day5(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        return resource.lines.map { seatId(it) }.max()?.toLong() ?: throw IllegalStateException()
    }
    override fun solve2(): Long {
        return missingSeat(resource.lines).toLong()
    }
}


// FBFBBFFRLR

// 2^6 | 2^5 | 2^4 | 2^3 | 2^2 | 2^1 

// just use parseint

fun String.toNum(zero: Char, one: Char): Int {
    // convert F's to 1s and B's to 0
    return Integer.parseInt((this.filter { it == one || it == zero } ).replace(one,'1').replace(zero,'0'),2)
}

fun seatId(s: String): Int {
    val row = s.toNum(zero = 'F', one = 'B')
    val col = s.toNum(zero = 'L', one = 'R')
    return 8 * row + col
}

fun missingSeat(l: List<String>): Int {
    val ids = (l.map { seatId(it) }).toList().toTypedArray()
    ids.sort()
    // all possible seats 
    // rows 0..127
    // seats 0..8
    // numbers 0..1024
    var i = 0
    // 0 1 2 3 4 5 7 8 9
    //           i j
    while(i < 1024) {
        if(ids[i+1] - ids[i] > 1) {
            return ids[i] + 1
        }
        i++
    }
    throw IllegalStateException()
}

