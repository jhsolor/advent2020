package adventofcode2020

import java.io.File

class FileReader(val fileName: String) {
    fun lines() : List<String> {
        println(fileName)
        val fileReader = File(fileName).bufferedReader()
        val contents = fileReader.readLines()
        return contents
    }

    fun numbers() : List<Int> {
        val contents = lines()
        val numbers: List<Int> = contents.map { it.toInt() }.sorted()
        return numbers
    }
}