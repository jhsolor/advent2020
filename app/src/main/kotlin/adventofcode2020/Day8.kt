package adventofcode2020

class Day8(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        return game().main()
    }

    override fun solve2(): Long {
        TODO("Implement")
    }

    fun game(): Game {
        return Game(resource.lines.map { Instruction.fromString(it) })

    }
}

enum class InstructionType() {
    NoOp, Accumulate, Jump, Terminate
}

class Instruction(var type: InstructionType, val num: Int) {
    var visited: Boolean = false
    var mutated: Int = 0
    val originalType = type
    fun visit() {
        println(this)
        if(visited) throw InfiniteLoopException("Already visited this instruction ${type} ${num}")
        visited = true
    }

    fun mutate() {
        //if(mutated > 2) throw InfiniteLoopException("Already mutated twice")
        when(type) {
            InstructionType.Jump -> type = InstructionType.NoOp
            InstructionType.NoOp -> type = InstructionType.Jump
            else -> throw MutationInvalidException("Cannot mutate this type: ${type}")
        }
        mutated++
    }

    fun reset() {
        visited = false
    }

    fun revert() {
        type = originalType
    }

    override fun toString(): String {
        return "${type} ${num}"
    }

    companion object {
        val regex = """(\w\w\w)\s+([+-])(\d+)""".toRegex()
        fun fromString(s: String): Instruction {
            val (instr, sign, num) = regex.matchEntire(s)!!.destructured
            val type = when(instr) {
                "acc" -> InstructionType.Accumulate
                "nop" -> InstructionType.NoOp
                "jmp" -> InstructionType.Jump
                else -> throw IllegalArgumentException("Not an instruction type: ${instr}")
            }
            val n = when(sign) {
                "+" -> num.toInt()
                "-" -> num.toInt() * -1
                else -> throw IllegalArgumentException("Not a valid sign: ${sign}")
            }
            return Instruction(type, n)
        }
    }
}

class Game(val instructions: List<Instruction>) {
    var acc: Long = 0
    var curPtr: Int = 0
    var visited = mutableListOf<Int>()

    fun main(): Long {
        try {
            loop() 
        } catch (ex: InfiniteLoopException) { 
            println("Exiting infinite loop: ${acc}")
        }
        return acc
    }

    fun loop() {
        var i = instructions[0]
        while(i.type != InstructionType.Terminate) { i = handle(i) }
    }
 
    fun reset() { acc = 0; curPtr = 0; visited = mutableListOf<Int>(); instructions.forEach { it.reset() } }

    fun fix(): Long {
        main()
        var walkBack = 0
        val maxWalkBack = visited.size - 2
        // val looper = instructions[loop] // memoize the looper
        // println("Looper: ${looper}")
        while(walkBack < maxWalkBack) { 
            var loopPtr = visited[maxWalkBack - walkBack]
            try { 
                fixState(loopPtr)
                return acc
            } catch(ex: GameException) {
                when(ex) {
                    is InfiniteLoopException -> { println("Exiting infinite loop: ${acc}"); revert(loopPtr) }
                    is MutationInvalidException -> println("Cannot mutate this type")
                    else -> throw ex
                }
            }
            walkBack++ 
        }
        return acc
    }

    fun fixState(ptr: Int) {
        println("Starting loop with ${instructions[ptr]}")
        instructions[ptr].mutate()
        reset()
        loop()
    }
        
    fun revert(ptr: Int) {
        println("Reverting ${instructions[ptr]}")
        instructions[ptr].revert()
    }

    fun handle(i: Instruction): Instruction { 
        when(i.type) {
            InstructionType.NoOp -> { return next() }
            InstructionType.Accumulate -> { acc += i.num; return next() }
            InstructionType.Jump -> { return next(i.num) }
            InstructionType.Terminate -> { return i }
        }
    }

    fun next(j: Int = 1): Instruction {
        curPtr += j
        if(curPtr >= instructions.size) return Instruction(InstructionType.Terminate, 0)
        visited.add(curPtr)
        println(curPtr)
        instructions[curPtr].visit()
        return instructions[curPtr]
    }
}

open class GameException(message:String): Exception(message)
class InfiniteLoopException(message:String): GameException(message)
class MutationInvalidException(message:String): GameException(message)

