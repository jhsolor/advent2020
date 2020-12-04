
package adventofcode2020

import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws

class Day4Test {
    val validPassport: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
    val validNorthPoleCreds: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 hgt:183cm"
    val invalidPassport: String = "pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"

    @Test fun testValidPassport() {
        val pp = validPassport.toPassport()
        assertThat(pp,isA<Passport>())
        assert(pp.validNorthPole())
        assert(pp.valid())
    }

    @Test fun testNorthPoleCredsMakePassport() {
        val pp = validNorthPoleCreds.toPassport()
        assertThat(pp,isA<Passport>())
        assert(pp.validNorthPole())
        assert(!pp.valid())
    }

    @Test fun testInvalidPassportNotValid() {
        val pp = invalidPassport.toPassport()
        assertThat(pp,isA<Passport>())
        assert(!pp.validNorthPole())
        assert(!pp.valid())
     }

    @Test fun testCollapseMultiLineToPassport() {
        val s = mutableListOf<String>(
            "ecl:gry pid:860033327",
            "eyr:2020 hcl:#fffffd",
            "byr:1937 iyr:2017 cid:147 hgt:183cm",
            "")
        s.add(validPassport)
        assertEquals(5,s.size)
        var pps = adventofcode2020.collapseStringsToPassport(s)
        assertEquals(2,pps.size)
        assertThat(pps[0],isA<Passport>())
    }
}

