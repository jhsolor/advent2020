package adventofcode2020

import com.google.common.io.Resources

class Resource(val resourceSuffix: String) {
    val lines: List<String> by lazy { 
        Resources.readLines(resource(),Charsets.UTF_8)
    }

    val grouped: List<String> by lazy {
        lines.collapseLines().filter { it.length > 0 }
    }

    fun resource(): java.net.URL {
        return Resource::class.java.getResource("${DAY_RESOURCE_CONVENTION}${resourceSuffix}")
    }

    companion object {
        const val DAY_RESOURCE_CONVENTION = "advent_"
    }
}

fun resourceFactory(day: String): Resource {
    return Resource(day)
}

