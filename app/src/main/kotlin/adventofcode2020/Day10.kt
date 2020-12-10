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
        TODO()
    }
}

class JoltageAdapter(val output: Int, private val maxJoltageGap: Int = 3) {
    var nextAdapter: JoltageAdapter? = null

    fun append(joltageAdapter: JoltageAdapter): JoltageAdapter {
        val delta = joltageAdapter.output - output
        if (delta > maxJoltageGap) throw IllegalArgumentException("${delta} is greater than max allowed gap ${maxJoltageGap}")
        nextAdapter = joltageAdapter
        return nextAdapter as JoltageAdapter
    }

    private fun end(): JoltageAdapter {
        if (nextAdapter != null) return nextAdapter!!.end()
        return this
    }

    fun appendCharger() { 
        val e = end()
        var j = JoltageAdapter(e.output + 3)
        e.append(j)
    }

    fun next(): JoltageAdapter? {
        return nextAdapter
    }

    fun delta(): Int? {
        if (nextAdapter == null) return null
        return nextAdapter!!.output - output
    }

    fun max(): Int {
        return end().output
    }

    override fun toString(): String {
        var s = "${output}"
        if (nextAdapter != null){ s = "${s} + ${nextAdapter!!.toString()}" }
        return s
    }

    companion object {
    }
}

class JoltageAnalyzer(private val firstLink: JoltageAdapter) {
    val deltaDistribution: HashMap<Int,Int> by lazy {
        findDistribution()
    }
    private fun findDistribution(): HashMap<Int, Int> { 
        var d = HashMap<Int,Int> ()
        var l: JoltageAdapter? = firstLink
        while(l != null){
            var delta = l.delta()
            if (delta == null) break
            d.put(delta, d.getOrDefault(delta, 0) + 1 )
            l = l.next()
        }
        return d
    }

    fun multiplySpread(): Long {
        return deltaDistribution.get(1)!!.toLong() * deltaDistribution.get(3)!!.toLong()
    }

    fun permutations(): Long {
        // walk the linked list
        // every time we find another adapter we *could remove* we multiply? the previous # of permutations
        // if we think about this like a binary system - an adapter is either on (present) or off (missing)
        // and there's a constraint on the maximum # of off switches (missing)
        // and an initial condition of which switches are already off
        // so the solution space fits within 2^(max()) because that's the full set of on/off switches we could have
        // if we start small with
        // 1 | 2 | 3 | 4 | 5 | 6
        // 2^6 = 64 possible total combinations
        // 1 | - | - | 4 | - | 6 is one of the minimal solution set
        // 1 | 2 | - | - | 5 | 6 is another one of the minimal solutions
        // on bigger sets we get to exclude all of the permutations above the #'s we're missing, so the actual # doesn't matter
        // e.g.  1,2,3,4,7 is equivalent to 1,2,3,4,5 for establishing the upper bound (but they differ in the lower bound, because in the 1st set, 4 is not removable)

    }
}


fun List<String>.toJoltageAdapters(withCharger: Boolean = false): JoltageAdapter {
    val l = this.map{ it.toInt() }.sorted()
    var i = 0
    var adapter = JoltageAdapter(0)
    val firstAdapter = adapter
    while(i < l.size) {
        adapter = adapter.append(JoltageAdapter(l[i]))
        i++
    }
    if (withCharger) adapter.appendCharger()
    return firstAdapter
}
