
package adventofcode2020

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA
import com.natpryce.hamkrest.throws
import kotlin.test.Test
import kotlin.test.assert
import kotlin.test.assertEquals

class Day4Test {
    val validPassportDTO: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"
    val validNorthPoleCreds: String = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 hgt:183cm"
    val invalidPassportDTO: String = "pid:860033327 eyr:2020 hcl:#fffffd byr:1937 iyr:2017 cid:147 hgt:183cm"

    @Test fun testValidPassportDTO() {
        val pp = validPassportDTO.toPassportDTO()
        assertThat(pp, isA<PassportDTO>())
        assert(pp.validNorthPole())
        assert(pp.valid())
    }

    @Test fun testNorthPoleCredsMakePassportDTO() {
        val pp = validNorthPoleCreds.toPassportDTO()
        assertThat(pp, isA<PassportDTO>())
        assert(pp.validNorthPole())
        assert(!pp.valid())
    }

    @Test fun testInvalidPassportDTONotValid() {
        val pp = invalidPassportDTO.toPassportDTO()
        assertThat(pp, isA<PassportDTO>())
        assert(!pp.validNorthPole())
        assert(!pp.valid())
    }

    @Test fun testCollapseMultiLineToPassportDTO() {
        val s = mutableListOf<String>(
            "ecl:gry pid:860033327",
            "eyr:2020 hcl:#fffffd",
            "byr:1937 iyr:2017 cid:147 hgt:183cm",
            ""
        )
        s.add(validPassportDTO)
        assertEquals(5, s.size)
        var pps = adventofcode2020.collapseStringsToPassport(s)
        assertEquals(2, pps.size)
        assertThat(pps[0], isA<PassportDTO>())
    }

    @Test fun testHeightFromString() {
        assertThat(Height.fromString("150cm"), isA<Height>())
        assertThat(Height.fromString("70in"), isA<Height>())
        assertThat({ Height.fromString("70yd") }, throws<IllegalArgumentException>())
        assertThat({ Height.fromString("Kinda Tall") }, throws<IllegalArgumentException>())
    }

    @Test fun testHeightWithinBoundaries() {
        assertThat(Height(Measure.Inches, 75), isA<Height>())
        assertThat(Height(Measure.Centimeters, 150), isA<Height>())
        assertThat({ Bounded(Height(Measure.Inches, 58), Passport.Companion::validHeight) }, throws<IllegalArgumentException>())
        assertThat({ Bounded(Height(Measure.Inches, 77), Passport.Companion::validHeight) }, throws<IllegalArgumentException>())
        assertThat({ Bounded(Height(Measure.Centimeters, 149), Passport.Companion::validHeight) }, throws<IllegalArgumentException>())
        assertThat({ Bounded(Height(Measure.Centimeters, 194), Passport.Companion::validHeight) }, throws<IllegalArgumentException>())
    }

    @Test fun testMakingPassports() {
        assertThat(
            Passport(
                Year(1921),
                Year(2015),
                Year(2025),
                Height.fromString("70in"),
                "#000000",
                "amb",
                "012345678",
                ""
            ),
            isA<Passport>()
        )

        assertThat(validPassportDTO.toPassportDTO().toPassport(), isA<Passport>())
    }

    @Test fun testNotMakingPassports() {
        assertThat(
            {
                Passport(
                    Year(1919), // BAD
                    Year(2015),
                    Year(2025),
                    Height.fromString("70in"),
                    "#000000",
                    "amb",
                    "012345678",
                    ""
                )
            },
            throws<IllegalArgumentException>()
        )

        assertThat({ invalidPassportDTO.toPassportDTO().toPassport() }, throws<IllegalArgumentException>())
    }
}
