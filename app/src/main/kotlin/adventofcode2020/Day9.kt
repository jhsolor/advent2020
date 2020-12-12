package adventofcode2020
import java.util.LinkedList
import java.util.Queue
// import mu.KotlinLogging

class Day9(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        val cipher = XmasCipher(resource.lines)
        return cipher.main()
    }
    override fun solve2(): Long {
        val cipher = XmasCipher(resource.lines)
        val target = cipher.main()
        val list = cipher.findSummers(target)
        return list.min()!! + list.max()!!
    }
}

class XmasCipher(val encode: List<String>, val preambleLength: Int = 25) {
    private var encodePtr = 0
    private var outlier: Long = -1

    private val raw: List<Long> by lazy {
        encode.map { it.toLong() }
    }

    private val queue: Queue<Long> = LinkedList<Long>()

    // initially, populate first 25 preamble in queue
    private fun preamble() {
        while (encodePtr < preambleLength) {
            queue.add(raw[encodePtr])
            encodePtr++
        }
//        println(queue)
    }

    init {
        preamble()
    }

    // march through the list
    fun next() {
//       println(queue)
        val _q = queue.poll()
//      println(_q)
        queue.add(raw[encodePtr])
//       println(queue)
        encodePtr++
    }

    // find the number missing
    fun findOutlier(potentialOutlier: Long): Boolean {
        // var potentialOutlier = raw[encodePtr + 1]

        // two runner solution to the find the things that sum
        var i = 0

        // 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8
        // i   j
        // i       j ...               j
        //     i   j ... 

        var q = queue.toMutableList()
        while (i < q.size - 1) {
//          println(q)
            var j = i + 1
            while (j < q.size) {
//                println("Potential Outlier: ${potentialOutlier}, q[${i}]=${q[i]} and q[${j}]=${q[j]} are ${q[i] + q[j]}")
                if (q[i] != q[j] && q[i] + q[j] == potentialOutlier) {
//                    println("Match found!")
                    return true
                }
                j++
            }
            i++
        }
        outlier = potentialOutlier
        return false
    }

    // now sum the set
    fun findSummers(target: Long): List<Long> {
        val outlierPtr = raw.indexOf(target)
        println("$target, $outlierPtr")
        var i = 0
        while (i < outlierPtr - 1) {
            var j = i + 1
            var acc = raw[i]
            while (j < outlierPtr && acc <= target) {
                acc += raw[j]
                j++ // subList is exclusive of max
                if (acc == target) return raw.subList(i, j)
            }
            i++
        }
        throw IllegalStateException("No matching sum found")
    }

    fun main(): Long {
//        println(raw)
        while (!queue.isEmpty() && encodePtr < raw.size) {
            val p = raw[encodePtr]
            if (!findOutlier(p)) return p
            next()
        }
        throw IllegalStateException("No outlier found")
    }
}
