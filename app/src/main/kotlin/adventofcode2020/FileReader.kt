package adventofcode2020

import java.io.File

class FileReader {
    fun doWork(fileName: String) : List<Int> {
        println(fileName)
        val fileReader = File(fileName).bufferedReader()
        val contents = fileReader.readLines()
        val numbers: List<Int> = contents.map { it.toInt() }.sorted()

        return numbers
    }
}