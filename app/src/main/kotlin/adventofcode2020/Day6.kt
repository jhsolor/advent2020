
package adventofcode2020

class Day6(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        return countDistinct().toLong()
    }
    override fun solve2(): Long {
        return countUniform().toLong()
    }

    fun countDistinct(): Int{
        return resource.grouped.map { it.toCharArray().filter { it != ' ' }.distinct().size }.reduce { sum, line -> sum + line }
    }

    fun countUniform(): Int{
        var result = 0
        for (group in resource.grouped) {
            var passengers = group.split(" ").filter { it.length > 0 }
            var alpha = ('a'..'z').toMutableList()
            for (passenger in passengers) {
                alpha = alpha.intersect(passenger.asIterable()).toTypedArray().toMutableList()
            }
            result += alpha.size
        }
        return result
    }
}

