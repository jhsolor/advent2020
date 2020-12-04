
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

fun String.toPassport(): Passport {
    val matches = Passport.PARSER_REGEX.findAll(this)
//    var pp = Passport()
    var hm = HashMap<String, String>()
    for(match in matches) {
        val(key, value) = match.destructured
        hm.put(key, value)
//        when(key){
//            "byr" -> pp.birthYear = value
//            "iyr" -> pp.issueYear = value
//            "eyr" -> pp.expirationYear = value
//            "hgt" -> pp.height = value
//            "hcl" -> pp.hairColor = value
//            "ecl" -> pp.eyeColor = value
//            "pid" -> pp.passportId = value
//            "cid" -> pp.countryId = value
//        }
    }
    if(!hm.containsKey("cid")) hm.put("cid",String()) //no need to do a default map
    return Passport(hm)
}
