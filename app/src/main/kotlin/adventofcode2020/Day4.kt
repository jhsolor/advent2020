
package adventofcode2020

class Day4(resource: Resource) : ResourceSolver(resource) {
    val passports by lazy {
        val pp = collapseStringsToPassport(resource.lines)
        println(pp)
        pp
    }
    override fun solve1(): Long {
        val p: Long = passports.fold(0.toLong(), { acc, next -> 
            if (next.validNorthPole()) {
                println(acc)
                acc + 1
            }
            else acc
        })
        println(p)
        return p
    }
    override fun solve2(): Long {
        TODO("Not Implemented")
    }
}

data class PassportDTO(val map: Map<String, String>){
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

// now let's strongly type our passport

class Passport() {
// byr (Birth Year) - four digits; at least 1920 and at most 2002.
// iyr (Issue Year) - four digits; at least 2010 and at most 2020.
// eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
// hgt (Height) - a number followed by either cm or in:
// If cm, the number must be at least 150 and at most 193.
// If in, the number must be at least 59 and at most 76.
// hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
// ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
// pid (Passport ID) - a nine-digit number, including leading zeroes.
// cid (Country ID) - ignored, missing or not.

}

class Year(){}
class Height(){}
class HairColor(){
}
class EyeColor(){}
class PassportId(){}

fun collapseStringsToPassport(strings: List<String>): List<PassportDTO> {
    var line = 0
    var s = String()
    // need a mutable list of passports
    val passports = mutableListOf<PassportDTO>()
    while(line < strings.size) {
        val thisRow = strings[line]
        s = s.plus(" ").plus(thisRow)
        // if this row is empty, close off our string and add it to the list 
        // or if we're on the last line, same deal
        if (thisRow.length == 0 || line + 1 == strings.size) {
            passports.add(s.toPassportDTO())
            s = String()
        }
        line++ 
    }
    return passports
}

fun String.toPassportDTO(): PassportDTO {
    // Assumes all passport blocks are on one line
    val matches = PassportDTO.PARSER_REGEX.findAll(this)
    var hm = HashMap<String, String>()
    for(match in matches) {
        val(key, value) = match.destructured
        hm.put(key, value)
    }
    if(!hm.containsKey("cid")) hm.put("cid",String()) //no need to do a default map
    return PassportDTO(hm)
}
