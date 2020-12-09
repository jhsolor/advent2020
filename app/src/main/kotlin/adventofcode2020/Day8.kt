package adventofcode2020

class Day8(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        TODO("Implement")
    }

    override fun solve2(): Long {
        TODO("Implement")
    }
}

enum class InstructionType() {
    NoOp, Accumulate, Jump, Terminate
}

class Instruction(val type: InstructionType, val num: Int) {
    var visited: Boolean = false
    fun visit() {
        if(visited) throw InfiniteLoopException("Already visited this instruction ${type} ${num}")
        visited = true
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
    var cur: Int = 0

    fun main(): Long {
        var i = instructions[0]
        try {
            while(i.type != InstructionType.Terminate) {
                i = handle(i)
            }
        } catch (ex: InfiniteLoopException) { 
            println("Exiting infinite loop: ${acc}")
        }
        return acc
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
        cur += j
        if(cur >= instructions.size) return Instruction(InstructionType.Terminate, 0)
        instructions[cur].visit()
        return instructions[cur]
    }
}

class GameException(message:String): Exception(message)
class InfiniteLoopException(message:String): Exception(message)
