
package adventofcode2020

import adventofcode2020.Year
import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws

class Day4Test {
    val validPassportDTO: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
    val validNorthPoleCreds: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 hgt:183cm"
    val invalidPassportDTO: String = "pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"

    @Test fun testValidPassportDTO() {
        val pp = validPassportDTO.toPassportDTO()
        assertThat(pp,isA<PassportDTO>())
        assert(pp.validNorthPole())
        assert(pp.valid())
    }

    @Test fun testNorthPoleCredsMakePassportDTO() {
        val pp = validNorthPoleCreds.toPassportDTO()
        assertThat(pp,isA<PassportDTO>())
        assert(pp.validNorthPole())
        assert(!pp.valid())
    }

    @Test fun testInvalidPassportDTONotValid() {
        val pp = invalidPassportDTO.toPassportDTO()
        assertThat(pp,isA<PassportDTO>())
        assert(!pp.validNorthPole())
        assert(!pp.valid())
     }

    @Test fun testCollapseMultiLineToPassportDTO() {
        val s = mutableListOf<String>(
            "ecl:gry pid:860033327",
            "eyr:2020 hcl:#fffffd",
            "byr:1937 iyr:2017 cid:147 hgt:183cm",
            "")
        s.add(validPassportDTO)
        assertEquals(5,s.size)
        var pps = adventofcode2020.collapseStringsToPassport(s)
        assertEquals(2,pps.size)
        assertThat(pps[0],isA<PassportDTO>())
    }

    private fun harnessBoundedYear(min: Int, max: Int, fcn: (year: String) -> Year){
        assertThat(fcn(min.toString()),isA<Year>())
        assertThat(fcn(max.toString()),isA<Year>())
        assertThat({fcn((min - 1).toString())},throws<IllegalArgumentException>())
        assertThat({fcn((max + 1).toString())},throws<IllegalArgumentException>())
    }

    @Test fun testBirthYearBetween1920and2002(){
        harnessBoundedYear(1920, 2002, ::BirthYear)
    }

    @Test fun testIssueYearBetween2010and2020(){
        harnessBoundedYear(2010, 2020, ::IssueYear)
    }

    @Test fun testExpiryYearBetween2020and2030(){
        harnessBoundedYear(2020, 2030, ::ExpiryYear)
    }

    @Test fun testHeightFromString() {
        assertThat(Height.fromString("150cm"), isA<Height>())
        assertThat(Height.fromString("70in"), isA<Height>())
        assertThat({Height.fromString("70yd")}, throws<IllegalArgumentException>())
        assertThat({Height.fromString("Kinda Tall")}, throws<IllegalArgumentException>())
    }

    @Test fun testHeightWithinBoundaries() {
        assertThat(Height(Measure.Inches, 75), isA<Height>())
        assertThat(Height(Measure.Centimeters, 150), isA<Height>())
        assertThat({Height(Measure.Inches, 58)}, throws<IllegalArgumentException>())
        assertThat({Height(Measure.Inches, 77)}, throws<IllegalArgumentException>())
        assertThat({Height(Measure.Centimeters, 149)}, throws<IllegalArgumentException>())
        assertThat({Height(Measure.Centimeters, 194)}, throws<IllegalArgumentException>())
    }

    @Test fun testMakingPassports() {
        assertThat(Passport(
            BirthYear("1921"),
            IssueYear("2015"),
            ExpiryYear("2025"),
            Height.fromString("70in"),
            "#000000",
            "amb",
            "012345678",
            ""),
        isA<Passport>())

        assertThat(validPassportDTO.toPassportDTO().toPassport(), isA<Passport>())
    }

    @Test fun testNotMakingPassports() {
        assertThat({Passport(
            BirthYear("1919"), //BAD
            IssueYear("2015"),
            ExpiryYear("2025"),
            Height.fromString("70in"),
            "#000000",
            "amb",
            "012345678",
            "")},
        throws<IllegalArgumentException>())
        
        assertThat({invalidPassportDTO.toPassportDTO().toPassport()}, throws<IllegalArgumentException>())
    }
}
