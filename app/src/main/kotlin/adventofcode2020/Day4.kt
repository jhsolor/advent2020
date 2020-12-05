
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
    // Fooling around with map delegate properties

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
    return Passport(
        byr = Year.fromString(this.byr),
        iyr = Year.fromString(this.iyr),
        eyr = Year.fromString(this.eyr),
        hgt = Height.fromString(this.hgt),
        hcl = this.hcl,
        ecl = this.ecl,
        pid = this.pid,
        cid = this.cid
    )
}

// now let's strongly type our passport

class Passport(byr: Year, iyr: Year, eyr: Year, hgt: Height, hcl: String, ecl: String, pid: String, cid: String) {
    private val eyeColors: List<String> = listOf("amb","blu","brn","gry","grn","hzl","oth")

    init {
        Bounded<Year>(byr, { y -> y.year >= 1920 && y.year <= 2002 })
        Bounded<Year>(iyr, { y -> y.year >= 2010 && y.year <= 2020 })
        Bounded<Year>(eyr, { y -> y.year >= 2020 && y.year <= 2030 })
        Bounded<Height>(hgt, Passport.Companion::validHeight)
        Bounded<String>(hcl, { s -> """#[0-9a-f]{6}""".toRegex().matchEntire(s) != null })
        Bounded<String>(pid, { s -> """[\d]{9}""".toRegex().matchEntire(s) != null })
        Bounded<String>(ecl, Passport.Companion::validEyeColor)
    }

    companion object {
        private val eyeColors = listOf("amb","blu","brn","gry","grn","hzl","oth")

        fun validEyeColor(s: String): Boolean = eyeColors.contains(s)
        fun validHeight(h: Height): Boolean {
            return when(h.unit) {
                Measure.Inches -> h.value >=59 && h.value <= 76
                Measure.Centimeters -> h.value >= 150 && h.value <= 193
            }
        }
    }
}

class Bounded<T>(val value: T, val boundary: (t: T) -> Boolean) {
    init {
        if(!boundary(value)) throw IllegalArgumentException("Value did not meet the boundary condition")
    }
}


class Year(val year: Int){
    companion object {
        fun fromString(s: String): Year {
            val y = """([\d]{4})""".toRegex().matchEntire(s)?.value?.toInt() ?: throw IllegalArgumentException("Year must be YYYY")
            return Year(y)
        }
    }
}

class Height(val unit: Measure, val value: Int){
    // hgt (Height) - a number followed by either cm or in:
    // If cm, the number must be at least 150 and at most 193.
    // If in, the number must be at least 59 and at most 76.
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
