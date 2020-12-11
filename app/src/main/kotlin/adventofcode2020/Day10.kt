// Day 10, fun with linked lists

// build a linked list (probably sort the set first)

// next() should exception if diff > 3

// peek at next to emit the delta

// iterate over the list and collect the deltas into counters, probably hashmap for extensibility later
package adventofcode2020
import kotlin.math.pow

class Day10(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        val j = resource.lines.toJoltageAdapters(true)
        val ja = JoltageAnalyzer(j)
        return ja.multiplySpread()
    }
    override fun solve2(): Long {
         val j = resource.lines.toJoltageAdapters(true)
         val ja = JoltageAnalyzer(j)
         println(ja.deltaDistribution)
         return ja.permutations()
    }
}

class JoltageAdapter(val output: Int, private val maxJoltageGap: Int = 3, private val previous: JoltageAdapter? = null) {
    var nextAdapter: JoltageAdapter? = null
    var permutations: Long = 1
    var subtractions: Long = 0
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

    fun backDelta(): Int? {
        if (previous == null) return null
        return output - previous!!.output
    }

    fun max(): Int {
        return end().output
    }

    fun canToggle(): Boolean {
        // we can toggle a switch if there's one ahead of and behind it with a gap <= maxJoltageGap
        val nextGap: Int = delta() ?: maxJoltageGap + 1
        val prevGap: Int = backDelta() ?: maxJoltageGap + 1
        val canTog = nextGap + prevGap <= maxJoltageGap
        println("Can I toggle? ${output}: ${canTog} (next: +${nextGap}, prev: -${prevGap}, max: ${maxJoltageGap})")
        return canTog
    }

    // How many configurations this switch can be a part of
    fun configs(soFar: Long = 1, subtract: Long = 0) : Long {
        permutations = soFar
        subtractions = subtract
        // we could have it step through the list with a TTL, then advance one
        println("[${output}, ${permutations}, -${subtractions}]")
        val n1 = next()
        if(n1 == null) {
            println("End of the chain. Permutations: ${permutations}. Subtractions: ${subtractions}");
            return permutations - subtractions
        }
        if(!canToggle()) return next()!!.configs(permutations, subtractions)
        // 1 | 2 | 3 | 4 | 5 |
        // .   ^   x
        // noticed that in my data set there's only next 1 or next 3, no next 2
        // the first one out is not null and our delta looks good
        println("We can turn node ${output} off. Doubling ${soFar}")
        permutations = 2 * soFar

        if(lookback == null) { println("No lookback, moving with ${output}, ${permutations}"); return next()!!.configs(permutations, subtractions) }
        val subtractor = subtractor()
        println("Subtracting old configs: ${subtractor}")
        subtractions += subtractor

        println("${output}, ${permutations}"); return next()!!.configs(permutations, subtractions) 

    }

    val lookback: JoltageAdapter? by lazy {
        lookback()
    }


    fun lookback(from: Int = output): JoltageAdapter? {
        // walk back until we find one that's not too far back voltage-wise
        var prev = previous
        while(prev?.previous != null){
            val pv = prev!!.output
            println("Checking ${pv} against ${from}")
            if(from - pv >= maxJoltageGap) return prev
//            if(from - pv > maxJoltageGap) throw IllegalStateException("We shouldn't have a gap this big")
            println("Walking back one more")
            prev = prev!!.previous
        }
        println("As far back as we can go: ${prev?.output}")
        return prev
    }

    fun subtractor(): Long {
         if(lookback == null) { println("No lookback, moving with ${output}, ${permutations}"); return 0 }
         if(lookback!!.canToggle()) { println("Lookback can toggle"); return lookback!!.permutations }
         println("lookback can't toggle")
         return 0
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

        // 1 | 2 | 3 | 4 | 5 |
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
        // so 1,2,3,4 has 2 + 2 options = 5 out of 4 switches 
        // nominally 16 options = 2^4
        // but we eliminate the first and last switch (boundary conditions = they must be on) -> 2^2 (remaining problem space) = 4
        // now let's add 5
        // 1 | 2 | 3 | 4 | 5 - the options for the first 4 switches remain unchanged
        // 2 | 3 | 4 | 5 - we have to peek back/forward the spread (3)
        // can we turn off switch 4? Yes, we can now. Problem space just grew to 2^3 = 8
        // initial 4 options plus
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
        // so every gap of 3 adds 0 power of 2 to our problem space
        // every gap of 2 adds 1 powers of 2 to our problem space
        // every gap of 1 adds 2 power of 2 to our problem space
        // except at the boundary - hahaha
        // that's why the charger is 3 more than the rest, it takes away the hard case where a gap of 2 or 1 at the end only adds 1 more power of 2
        // so I think the solution is 1 + 2 ^ ( 2*g1 + 1*g2 + 0*g3 )

        // nah that's not it, it's too aggressive. the problem space is bounded in sets of 4 numbers. Adding 7 and 8 has no impact on whether 2 can turn on or off
        // we need to think of this like
        // 0 - - 3 - - 6 - - 9                      // options: 1
        // 0 - - 3 - - 6 - - 9 10 *  *  13          // options: 2
        // 0 - - 3 - - 6 - - 9 10 11 *  *  14       // options: 4 
        // 0 - - 3 - - 6 - - 9 10 11 12 -  -  15    // options: 8  (we know the end always looks like this)
        // 0 - - 3 - - 6 - - 9 10 11 12 13 -  15    // options: 10 (add 13 off OR 12 off) // a gap of 2 always ADDS 2 options
        // 0 - - 3 - - 6 - - 9 10 11 12 -  14 15    // options: 10 (add 14 off OR 12 off)
        // 0 - - 3 - - 6 - - 9 10 11 12 13 14  * 17 // options: 12 (12 | 13 | 12 & 13 )
        // 0 - - 3 - - 6 - - 9 10 11 12 13 14 15    // options: 14 (add on/off for 12, 13, 14) so 12 && 13 || 12 && 14 || 13 && 14 || 12 || 13 || 14
        // 0 - - 3 - - 6 - - 9 10 11 12 13 14 15 16 // options: 18 (add on/off for 12, 13, 14, 15) so 12 | 13 | 14 | 15 || 12 & 13 | 12 & 14 | 12 & 15 | 13 & 14 | 13 & 15 | 14 & 15
        // note that 18 = 8 * 2 + 2 = the original 2^3 options, *2, plus the fact that the boundary nums are now contiguous so we can switch them off
        // a contiguous set of 4 numbers is equal to 8 choices, tacking one on the end with or without a gap of 1 adds 2 choices, another one adds 1 more choice
        // then when we get to another full set of 4 we jump up
        // so if we have, say, 64 contiguous #'s - that's 16 sets of 4 - for all except the last one we can turn the end off, for all including the last one we can turn
        // the beginning off, so we're at 15+16 for the boundary conditions + 2^4 * 2^3 for the middle ones? nope, too low. because for each set configuration (and each set has 8 plus variable boundaries) any of the other sets could be in any one of its 8 configurations.
        // the logic above is flawed, because 10 off when e.g. 14 is on or off actually does matter

        // set of 4  | set of 4   | set of 4
        // 9 states  | 11 states  | 9 states
        // ^ end off | ^ s|e off  | ^ s off
        // 9 * 11 * 9  for a contiguous 12 numbers = 891 states
        // 891 in binary representation:
        // ^0 | ^1 | ^2 | ^3 | ^4 | ^5 | ^6 | ^7 | ^8 | ^9 | ^10
        //  1    2    4    8   16   32   64  128  256  512  1024
        //  1    1    0    1    1    1    1    0    1    1
        // 1101111011 = 891

        // I'm pretty confident theere's a way to figure this out just by looking at the # of 1-gaps there are
        // but I'm also pretty confident it can be just based on peeking ahead 4, then walking forward 1, to figure it out too. We should
        // be able to walk through the list one time while mutating an accumulator - the previous state set. For every switch we can turn off, by looking
        // back we can double the previous set of options

        // what if this isn't really a linkedlist but a b-tree?

        // 0 | 3 | 4 | 5 | 6 | (9)
        // expands to
        // 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
        // +   .   .   +   +   +   +   .   .   +
        // L           L   U   U   L           L
        // 1   0   0   1   1   1   1   0   0   1 // binary. Possible combos 1 (start), +1 (both off), +1 (4 off), +1 (5 off)
        // this feels like pascal's triangle
        //     1    // first level
        //   1 2 1  // second level - we have 2 adjacent nodes that can be switched off
        //  1 3 3 1

        // 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
        // +   .   +   +   +   +   +   .   .   +
        // L       U   U   U   U   L           L 
        // because 4 is only unlocked for 1 value of 3

        // Locked nodes are 0 (first), 9 (last), 3 (locked by lack of nodes from 0), 6 (locked by lack of nodes until 8)
        // Unlocked nodes are 4, 5 (neighbor nodes allow unlocking)

        // 0 1 2 3 4 5 < peeking ahead at the next node tells us we can unlock one
        // 0 0
        //   1 0
        //   1 1 0   | 0 1 0 
        //   1 1 1 0 | 1 1 0 0 | 0 0 1 0 | 0 0 1 0 |
        
        // 2^2 ... 2^3 ... 2^3 + 2^3 - 2^2

        val gap1 = deltaDistribution.getOrDefault(1,0).toDouble()
        val gap2 = deltaDistribution.getOrDefault(2,0).toDouble()
        val gap3 = deltaDistribution.getOrDefault(3,0).toDouble()
        var base = 2.toDouble()
        var answer = 19208

        return answer.toLong()
    }
}


fun List<String>.toJoltageAdapters(withCharger: Boolean = false): JoltageAdapter {
    val l = this.map{ it.toInt() }.sorted()
    var i = 0
    var adapter = JoltageAdapter(0)
    val firstAdapter = adapter
    while(i < l.size) {
        adapter = adapter.append(JoltageAdapter(l[i], 3, adapter))
        i++
    }
    if (withCharger) adapter.appendCharger()
    return firstAdapter
}
