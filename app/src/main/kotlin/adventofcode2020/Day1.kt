// Day1
package adventofcode2020

fun Day1(list: List<Int>) : Int {
    // solve with a fast runner
    // but let's not make it fancy yet
    var i = 0
    while(i < list.size) {
        for (j in i..(list.size - 1)) {
            for (k in (j..list.size - 1)) {
                if (list[i] + list[j] + list[k] == 2020) {
                    return list[i] * list[j] * list[k]
                }
            }
        }


    }
    TODO("Didn't find an answer")
}