
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
        var goodPassports = 0
        var badPassports = 0
        for(dto in passports) {
            try {
                dto.toPassport()
                goodPassports++
            } catch(ex: IllegalArgumentException) {
                println(ex)
                badPassports++
            }
        }
        println("Good passports: {$goodPassports} Bad passports: {$badPassports}")
        return goodPassports.toLong()
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
            println("DTO invalid because the map is missing an element: ${ex}")
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
            println("DTO invalid because the map is missing an element: ${ex.message}")
            return false
        }
    }

    companion object {
        val PARSER_REGEX = Regex("(\\w{3})\\:([^\\s]+)")
    }
}

fun PassportDTO.toPassport(): Passport {
    if(!this.validNorthPole()) throw IllegalArgumentException("Can only convert valid DTOs")
    return Passport(BirthYear(this.byr), IssueYear(this.iyr), ExpiryYear(this.eyr), Height.fromString(this.hgt), this.hcl, this.ecl, this.pid, this.cid)
}

// now let's strongly type our passport

class Passport(byr: Year, iyr: Year, eyr: Year, hgt: Height, hcl: String, ecl: String, pid: String, cid: String) {
    // hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
    // ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
    // pid (Passport ID) - a nine-digit number, including leading zeroes.
    // cid (Country ID) - ignored, missing or not.

    private val eyeColors: List<String> = listOf("amb","blu","brn","gry","grn","hzl","oth")

    init {
        // I did the rest as types to fool around with that system but now I'm getting bored with it
        """#[0-9a-f]{6}""".toRegex().matchEntire(hcl) ?: throw IllegalArgumentException("Hair color must be a hex color code")
        """[\d]{9}""".toRegex().matchEntire(pid) ?: throw IllegalArgumentException("Not a valid pid: ${pid}")
        if(!eyeColors.contains(ecl)) throw IllegalArgumentException("Not a valid eye color: ${ecl}")
    }
}

class Year(val year: Int){
}

// Pure OO would say let's do these as subclasses. Experimenting with Factoriees

fun BirthYear(s: String): Year {
    // byr (Birth Year) - four digits; at least 1920 and at most 2002.
    return boundedYear(s, 1920, 2002)
}

fun IssueYear(s: String): Year {
    // iyr (Issue Year) - four digits; at least 2010 and at most 2020.
    return boundedYear(s, 2010, 2020)
}

fun ExpiryYear(s: String): Year {
    // eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
    return boundedYear(s, 2020, 2030)
}

private fun boundedYear(s: String, min: Int, max: Int): Year {
    val regex = """([\d]{4})""".toRegex()
    val m = regex.matchEntire(s) ?: throw IllegalArgumentException("Year must be YYYY")
    val y = m.value.toInt()
    return when {
        y < min -> throw IllegalArgumentException("Invalid before ${min}")
        y > max -> throw IllegalArgumentException("Invalid after ${max}")
        else -> Year(y)
    }
}

class Height(unit: Measure, value: Int){
    // hgt (Height) - a number followed by either cm or in:
    // If cm, the number must be at least 150 and at most 193.
    // If in, the number must be at least 59 and at most 76.
    private val MIN_INCHES: Int = 59
    private val MAX_INCHES: Int = 76
    private val MIN_CM: Int = 150
    private val MAX_CM: Int = 193
    init {
        if(unit == Measure.Inches && (value > MAX_INCHES || value < MIN_INCHES)) throw IllegalArgumentException("Inches must be between ${MIN_INCHES} and ${MAX_INCHES}")
        if(unit == Measure.Centimeters && (value > MAX_CM || value < MIN_CM)) throw IllegalArgumentException("Centimeters must be between ${MIN_CM} and ${MAX_CM}")
    }

    companion object {
        fun fromString(s: String): Height {
            val (height, measure) = """([\d]{2,3})(in|cm)""".toRegex().matchEntire(s)?.destructured ?: throw IllegalArgumentException("Height must be DDDuu")
            return when(measure) {
                "cm" -> Height(Measure.Centimeters, height.toInt())
                "in" -> Height(Measure.Inches, height.toInt())
                else -> throw IllegalArgumentException("No match found for measure: ${measure}")
            }
        }
    }
}

enum class Measure(){
    Centimeters, Inches
}

// enum class HairColor(){
// }
// class EyeColor(){}
// class PassportId(){}

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
