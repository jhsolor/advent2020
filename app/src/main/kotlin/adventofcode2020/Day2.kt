package adventofcode2020

import com.google.common.io.Resources

class Day2(resource: Resource) : Solver {

    private val list: List<String> by lazy {
        Resources.readLines(Resources.getResource("advent_1.txt"), Charsets.UTF_8)
    }

    val passwords: List<Password> by lazy {
        list.map { it.toPassword() }
    }

    override fun solve1(): Long {
        var valid = 0
        for (pw in passwords) if (pw.valid()) valid++
        return valid.toLong()
    }
    override fun solve2(): Long {
        var valid = 0
        for (pw in passwords) if (pw.hardValid()) valid++
        return valid.toLong()
    }
}

// Just make a password object
// string format is
// 5-11 t: glhbttzvzttkdx
class Password(val specialCharacter: Char, val minimum: Int, val maximum: Int, val password: String) {
    // part 1
    fun valid(): Boolean {
        var found = 0
        for (char in password) if (char == specialCharacter) found++
        if (found >= minimum && found <= maximum) {
            return true
        }
        return false
    }

    // part 2
    fun hardValid(): Boolean {
        // print()
        val pos1 = password[minimum - 1]
        val pos2 = password[maximum - 1]
        if (pos1 == specialCharacter && pos2 == specialCharacter) return false
        if (pos1 == specialCharacter || pos2 == specialCharacter) return true
        return false
    }

    fun print() {
        println("Min: $minimum")
        println("Max: $maximum")
        println("Special: $specialCharacter")
        println("Password: $password")
    }
}

fun String.toPassword(): Password {
    // split the string and create a tuple
    val substrings = this.split(" ")
    val specialCharacter = substrings[1][0]
    val range = substrings[0].split("-")
    val password = substrings[2]
    return Password(specialCharacter, range[0].toInt(), range[1].toInt(), password)
}
