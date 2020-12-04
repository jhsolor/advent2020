
package adventofcode2020

class Day4(resource: Resource) : ResourceSolver(resource) {
    override fun solve1(): Long {
        TODO("Not Implemented")
    }
    override fun solve2(): Long {
        TODO("Not Implemented")
    }
}

data class Passport(val map: Map<String, String>){
    val byr by map // birth year
    val iyr by map // issue year
    val eyr by map // expiry year
    val hgt by map // height
    val hcl by map // hair color
    val ecl by map // eye color
    val pid by map // passport id
    val cid by map // country id
    
    fun valid(): Boolean{
        try {
            return validNorthPole() &&
            cid.length > 0
        } catch (ex: NoSuchElementException) {
            return false
        }
    }

    fun validNorthPole(): Boolean{
        try {
            return ecl.length > 0 &&
            byr.length > 0 &&
            iyr.length > 0 &&
            eyr.length > 0 &&
            hgt.length > 0 &&
            hcl.length > 0 &&
            ecl.length > 0 &&
            pid.length > 0 
        } catch (ex: NoSuchElementException) {
            return false
        }
    }

    companion object {
        val PARSER_REGEX = Regex("(\\w{3})\\:([^\\s]+)")
    }
}

fun collapseStringsToPassport(strings: List<String>): List<Passport> {
    var line = 0
    var s = String()
    // need a mutable list of passports
    val passports = mutableListOf<Passport>()
    while(line < strings.size) {
        val thisRow = strings[line]
        s = s.plus(" ").plus(thisRow)
        // if this row is empty, close off our string and add it to the list 
        // or if we're on the last line, same deal
        if (thisRow.length == 0 || line + 1 == strings.size) {
            passports.add(s.toPassport())
            s = String()
        }
        line++ 
    }
    return passports
}

fun String.toPassport(): Passport {
    // Assumes all passport blocks are on one line
    val matches = Passport.PARSER_REGEX.findAll(this)
    var hm = HashMap<String, String>()
    for(match in matches) {
        val(key, value) = match.destructured
        hm.put(key, value)
    }
    if(!hm.containsKey("cid")) hm.put("cid",String()) //no need to do a default map
    return Passport(hm)
}
