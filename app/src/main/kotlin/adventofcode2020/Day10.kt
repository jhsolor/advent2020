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

        // maybe something around 2 ^ # of removable switches is the solution space

        // there is a minimum # we need to have
        // 1 | - | - | 4 | is our set, that's it. there's 0
        // 1 | 2 | - | 4 | is our set, there's 2 options: 1 | - | - | 4 is the other one
        // 1 | 2 | 3 | 4 | is our set, there's also 1 | - | 3 | 4, and no other additional options - the addition of a switch added 2 more options
        // so 1,2,3,4 has 2 + 2 + 1 options = 5 out of 4 switches 
        // nominally 16 options = 2^4
        // but we eliminate the first and last switch (boundary conditions = they must be on) -> 2^2 (remaining problem space) + 1 (boundary set) = 5
        // now let's add 5
        // 1 | 2 | 3 | 4 | 5 - the options for the first 4 switches remain unchanged
        // 2 | 3 | 4 | 5 - we have to peek back/forward the spread (3)
        // can we turn off switch 4? Yes, we can now. Problem space just grew to 2^3 + 1 = 9
        // initial 5 options plus
        // 1 | 2 | - | - | 5
        // 1 | - | 3 | - | 5
        // 1 | - | 3 | - | 5
        // 1 | 2 | 3 | - | 5 (all the versions where 4 is off)
        // . if we can't, we need to move to the next one. Let's add 7 as an example instead of 5
        // 1 | 2 | 3 | 4 | 7
        // is the same as
        // 1 | 2 | 3 | 4 | - | - | - | 7
        // we gain 0 options, because we can't turn 4 off
        // if we add 6, we gain all the "4 off options" because 6 is 3 greater than 3 - it's still 2^3 + 1
        // 1 | 2 | 3 | 4 | - | 6
        // so every gap of 3 adds 1 power of 2 to our problem space
        // every gap of 2 adds 2 powers of 2 to our problem space
        // every gap of 1 adds 3 power of 2 to our problem space
        // except at the boundary - hahaha
        // that's why the charger is 3 more than the rest, it takes away the hard case where a gap of 2 or 1 at the end only adds 1 more power of 2
        // so I think the solution is 1 + 2 ^ ( 3*g1 + 2*g2 + 1*g3 )
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
