package adventofcode2020

fun solverFactory(day: String, resource: Resource): Solver {
    when (day) {
        "1" -> return Day1(resource)
        "2" -> return Day2(resource)
        "3" -> return Day3(resource)
        "4" -> return Day4(resource)
        "5" -> return Day5(resource)
        "6" -> return Day6(resource)
        "7" -> return Day7(resource)
        "8" -> return Day8(resource)
        "9" -> return Day9(resource)
        "10" -> return Day10(resource)
    }

    throw IllegalArgumentException("Day \"${day}\" is not a valid optoion")
}

interface Solver {
    fun solve1(): Long
    fun solve2(): Long
}

abstract class ResourceSolver(val resource: Resource) : Solver
