// Day1
package adventofcode2020

import com.google.common.io.Resources;

class Day1() : Solver {

    private val list: List<Int> by lazy {
       Resources.readLines(Resources.getResource("advent_1.txt"),Charsets.UTF_8).map { it.toInt() }.sorted()
    }

    override fun solve1(): Int {
        TODO("Implement")
    }

    override fun solve2(): Int {
        // solve with a fast runner
        // but let's not make it fancy yet
        var i = 0
        while (i < list.size) {
            for (j in i..(list.size - 1)) {
                for (k in (j..list.size - 1)) {
                    if (list[i] + list[j] + list[k] == 2020) {
                        return list[i] * list[j] * list[k]
                    }
                }
            }


        }
        TODO("Didn't find an answer")
    }

}