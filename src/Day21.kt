fun main() {

    data class Monkey(val name: String, val expr: String)
    fun Monkey.evaluateExpr(map: MutableMap<String, Long>): Long? {
        val parts = this.expr.split(" ")
        if (parts.size == 1) {
            return parts[0].toLong()
        }

        val name1 = parts.first()
        val name2 = parts.last()
        val operator = parts[1]

        if (operator == "=") {
            if (name1 in map) {
                map[name2] = map[name1]!!
            } else if (name2 in map) {
                map[name1] = map[name2]!!
            }
        } else {
            if (name1 in map && name2 in map) {
                val value1 = map[name1]!!
                val value2 = map[name2]!!
                return when (operator) {
                    "+" -> value1 + value2
                    "-" -> value1 - value2
                    "*" -> value1 * value2
                    "/" -> value1 / value2
                    else -> null
                }
            }
        }

        return null
    }
    fun Monkey.variants(): List<Monkey> {
        val parts = this.expr.split(" ")
        if (parts.size == 1) {
            return listOf(this)
        }

        val name1 = parts.first()
        val name2 = parts.last()
        val operator = parts[1]

        return when (operator) {
            "+" -> listOf(this, Monkey(name1, "$name - $name2"), Monkey(name2, "$name - $name1"))
            "-" -> listOf(this, Monkey(name1, "$name + $name2"), Monkey(name2, "$name1 - $name"))
            "*" -> listOf(this, Monkey(name1, "$name / $name2"), Monkey(name2, "$name / $name1"))
            "/" -> listOf(this, Monkey(name1, "$name * $name2"), Monkey(name2, "$name1 / $name"))
            else -> listOf(this)
        }
    }

    fun List<Monkey>.calc(need: String): Long {
        val map = mutableMapOf<String, Long>()

        while (need !in map) {
            forEach {
                if (map[it.name] == null) {
                    it.evaluateExpr(map)?.let { result ->
                        map[it.name] = result
                    }
                }
            }
        }

        return map[need]!!
    }

    fun part1(input: List<String>) =
        input.map {
                val (name, expr) = it.split(": ")
                Monkey(name, expr)
            }
            .calc("root")

    fun part2(input: List<String>) =
        input.flatMap {
                val (name, expr) = it.split(": ")
                when (name) {
                    "root" -> Monkey(name, expr.replace("+", "=")).variants()
                    else -> Monkey(name, expr).variants()
                }
            }
            .filterNot { it.name == "humn" && it.expr.split(" ").size == 1 }
            .calc("humn")

    val input = readInput("inputs/Day21")
    println(part1(input))
    println(part2(input))
}
