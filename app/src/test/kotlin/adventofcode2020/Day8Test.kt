package adventofcode2020

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8Test {
    @Test fun testCanParseRules() {
        val nop = Instruction.fromString("nop +0")
        val jmp = Instruction.fromString("jmp +4")
        val acc = Instruction.fromString("acc -10")
        assertThat(nop, isA<Instruction>())
        assertThat(jmp, isA<Instruction>())
        assertThat(acc, isA<Instruction>())
        assertEquals(InstructionType.NoOp, nop.type)
        assertEquals(InstructionType.Jump, jmp.type)
        assertEquals(InstructionType.Accumulate, acc.type)
        assertEquals(0, nop.num)
        assertEquals(4, jmp.num)
        assertEquals(-10, acc.num)
    }

    @Test fun testThrowsLoopException() {
        val nop = Instruction.fromString("nop +0")
        nop.visit()
        assertThat({ nop.visit() }, throws<InfiniteLoopException>())
    }

    @Test fun testCanAccumulate() {
        val instructions = listOf(Instruction.fromString("nop +0"), Instruction.fromString("acc +10"))
        val g = Game(instructions)
        assertEquals(10, g.main())
    }

    @Test fun testEndToEnd() {
        var g = Day8(Resource("8b")).game()
        assertEquals(8, g.fix())
    }
}
