package adventofcode2020

class Day7(resource: Resource) : ResourceSolver(resource) {
    val rules: HashMap<String, HashMap<String,Int>> by lazy { 
        parseRules()
    }

    override fun solve1(): Long {
        var containsShiny: Long = 0
        for(rule in rules) {
            if (containsBagType(rule.key, "shiny gold")) { containsShiny++ }
        }
        return containsShiny
    }

    override fun solve2(): Long {
        return countDescendants("shiny gold").toLong()
    }

    fun parseRules(): HashMap<String, HashMap<String,Int>> {
        var hm = HashMap<String, HashMap<String,Int>>()
        for(line in resource.lines.filter{ it.length > 0 }) {
            try {
                var (key, value) = parseRule(line)
                hm.put(key, value)
            } catch(ex: NullPointerException){
                println("Could not parse rule")
            }
        }
        return hm
    }

    // Bags need to have a descriptor, and a set of qtys of other bags they create
    // If I have this kind of bag, what kinds of bags does it contain?
    fun parseRule(s: String): Pair<String, HashMap<String, Int>> { 
        val regex = """(\w+\s\w+)\sbags contain (.+)""".toRegex()
        // val regex = """(\w+\s\w+)\sbags contain (\d+\s\w+\s\w+\sbags?[,.])+""".toRegex()
        val bagreg ="""(\d+|no)\s(\w+\s\w+|other)\sbags?[,.]""".toRegex() // don't want to figure out deep regex reference traversal just now
        var rule = HashMap<String, Int>()
        val (bag, makes) = regex.find(s)!!.destructured
    
        val contents = bagreg.findAll(makes).toList()
        for (content in contents) {
            val (qty, descriptor) = content.destructured
            val numericQty = if (qty == "no") 0 else qty.toInt()
            if (numericQty == 0) { continue }
            //println("${descriptor}: ${numericQty}")
            rule.put(descriptor, numericQty)
        }
        return Pair(bag, rule)
    }

    fun containsBagType(startingBagType: String, targetBagType: String): Boolean {
        println("Finding out if ${startingBagType} contains ${targetBagType}")
        val rule = rules[startingBagType]
        if (rule == null) return false
        for ( r in rule) {
            if (r.key == targetBagType) return true
            if (containsBagType(r.key, targetBagType)) return true
        }
        return false
    }

    fun countDescendants(bagType: String, filter: (_: String) -> Boolean = {_ -> true}): Int {
        val rule = rules[bagType]
        if (rule == null) return 0
        return rule.map { it.value + it.value * countDescendants(it.key, filter) }.fold(0) { sum, it -> sum + it }
    }
}

