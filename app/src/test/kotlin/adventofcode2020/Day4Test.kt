
package adventofcode2020

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
}

