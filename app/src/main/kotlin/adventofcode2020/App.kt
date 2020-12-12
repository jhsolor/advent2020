package adventofcode2020

fun main(args: Array<String>) {

    // offer what day we are trying to solve
    println("What day are you on?")
    val day = readLine()!!
    val resource = resourceFactory(day)
    val solver = solverFactory(day, resource)

    println("What part are you solving?")
    val part = readLine()!!.toInt()
    when (part) {
        1 -> println(solver.solve1())
        2 -> println(solver.solve2())
    }
}
