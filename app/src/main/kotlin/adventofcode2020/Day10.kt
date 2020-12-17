// Day 10, fun with linked lists

// build a linked list (probably sort the set first)

// next() should exception if diff > 3

// peek at next to emit the delta

// iterate over the list and collect the deltas into counters, probably hashmap for extensibility later
package adventofcode2020

class Day10(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        val j = resource.lines.toJoltageAdapters(true)
        val ja = JoltageAnalyzer(j)
        return ja.multiplySpread()
    }
    override fun solve2(): Long {
        val j = resource.lines.toJoltageAdapters(true)
        return j.end().paths()
    }
}

class JoltageAdapter(val output: Int, private val maxJoltageGap: Int = 3, private val previous: JoltageAdapter? = null) {
    var nextAdapter: JoltageAdapter? = null
    fun append(joltageAdapter: JoltageAdapter): JoltageAdapter {
        val delta = joltageAdapter.output - output
        if (delta > maxJoltageGap) throw IllegalArgumentException("$delta is greater than max allowed gap $maxJoltageGap")
        nextAdapter = joltageAdapter
        return nextAdapter as JoltageAdapter
    }

    fun end(): JoltageAdapter {
        if (nextAdapter != null) return nextAdapter!!.end()
        return this
    }

    fun next(): JoltageAdapter? {
        return nextAdapter
    }

    fun delta(): Int? {
        if (nextAdapter == null) return null
        return nextAdapter!!.output - output
    }

    fun appendCharger() {
        val e = end()
        var j = JoltageAdapter(e.output + 3, 3, e)
        e.append(j)
    }

    fun backDelta(): Int? {
        if (previous == null) return null
        return output - previous.output
    }

    fun max(): Int {
        return end().output
    }

    // How many configurations this switch can be a part of
    fun paths(): Long {
        // Break this down into:
        // how many paths are there to this point
        // from the last 3 points
        // e.g.

        // 4 5 6 7
        // how many valid paths are there to #7
        // 4 .. 7, 4 .. 5 .. 7, 4 .. 6 .. 7, 4 .. 5 .. 6 .. 7        // then, how many valid paths are there to 4, 5, and 6 (? do we need to worry about duplicates? let's not yet)
        // (recurse through those paths, or hashmap)
// there can be 1, 2, or 3 nodes that can lead us here
        var p: Long = 0
        val allPaths = lookback.map { it.paths }
        for (path in allPaths) {
            if (path == 0.toLong()) p++
            else p += path
        }

        return p
    }

    val paths: Long by lazy {
        var p = paths()
        p
    }

    // give us a list of all the nodes that can lead us here
    val lookback: List<JoltageAdapter> by lazy {
        lookback()
    }

    fun lookback(from: Int = output): List<JoltageAdapter> {
        // walk back until we find one that's not too far back voltage-wise
        var l = mutableListOf<JoltageAdapter>()
        if (previous == null) return l
        var prev = previous
        while (prev != null) {
            val pv = prev.output
            if (from - pv > maxJoltageGap) return l
            l.add(prev)
            prev = prev.previous
        }
        return l
    }

    override fun toString(): String {
        var s = "$output {$paths}"
        return s
    }
}

class JoltageAnalyzer(private val firstLink: JoltageAdapter) {
    val deltaDistribution: HashMap<Int, Int> by lazy {
        findDistribution()
    }

    // 1 | 2 | 3 | 4 | 5 |
    fun multiplySpread(): Long {
        return deltaDistribution.get(1)!!.toLong() * deltaDistribution.get(3)!!.toLong()
    }

    private fun findDistribution(): HashMap<Int, Int> {
        var d = HashMap<Int, Int> ()
        var l: JoltageAdapter? = firstLink
        while (l != null) {
            var delta = l.nextAdapter?.output ?: 0 - l.output
            if (delta < 0) break
            d.put(delta, d.getOrDefault(delta, 0) + 1)
            l = l.nextAdapter
        }
        return d
    }
}

fun List<String>.toJoltageAdapters(withCharger: Boolean = false): JoltageAdapter {
    val l = this.map { it.toInt() }.sorted()
    var i = 0
    var adapter = JoltageAdapter(0, 3, null)
    val firstAdapter = adapter
    while (i < l.size) {
        adapter = adapter.append(JoltageAdapter(l[i], 3, adapter))
        i++
    }
    if (withCharger) adapter.appendCharger()
    return firstAdapter
}
