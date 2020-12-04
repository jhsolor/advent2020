package adventofcode2020

fun solverFactory(day: String, resource: Resource): Solver {
    when(day) {
      "1" -> return Day1(resource)
      "2" -> return Day2(resource)
      "3" -> return Day3(resource)
      "4" -> return Day4(resource)
    }

    throw IllegalArgumentException("Day \"${day}\" is not a valid optoion")
}

interface Solver {
  fun solve1(): Long 
  fun solve2(): Long 
}

abstract class ResourceSolver(val resource: Resource): Solver {

}

