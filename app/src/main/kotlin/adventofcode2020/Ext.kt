package adventofcode2020

fun Int.format(digits: Int) = "%0${digits}d".format(this)

fun List<String>.collapseLines(delim: String = " "): List<String> {
    var line = 0
    var s = String()
    // need a mutable list
    val collapsed = mutableListOf<String>()
    while (line < this.size) {
        val thisRow = this[line]
        s = s.plus(delim).plus(thisRow)
        // if this row is empty, close off our string and add it to the list 
        // or if we're on the last line, same deal
        if (thisRow.length == 0 || line + 1 == this.size) {
            collapsed.add(s)
            s = String()
        }
        line++
    }
    return collapsed
}
