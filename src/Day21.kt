fun main() {

    data class Monkey(val name: String, val expr: String)

    fun part1(input: List<String>): Long {
        val monkeys = input.map {
            var (name, expr) = it.split(":")
            Monkey(name, expr.trim())
        }

        val map = mutableMapOf<String, Long>()

        fun Monkey.calcExpr(): Long? {
            val parts = this.expr.split(" ")
            if (parts.size == 1) {
                return parts[0].toLong()
            }

            val name1 = parts.first()
            val name2 = parts.last()

            if (name1 in map && name2 in map) {
                val value1 = map[name1]!!
                val value2 = map[name2]!!
                val operator = parts[1]

                return when (operator) {
                    "+" -> value1 + value2
                    "-" -> value1 - value2
                    "*" -> value1 * value2
                    "/" -> value1 / value2
                    else -> null
                }
            }

            return null
        }

        while (map["root"] == null) {
            monkeys.forEach {
                if (map[it.name] == null) {
                    val calcExpr = it.calcExpr()
                    if (calcExpr != null) {
                        map[it.name] = calcExpr
                    }
                }
            }
        }

        return map["root"]!!
    }

    fun trywith(input: List<String>, n: Long): Long {
        val monkeys = input.map {
            var (name, expr) = it.split(":")

            if (name == "root") {
                expr = expr.replace("+", "=")
            }

            Monkey(name, expr.trim())
        }

        val map = mutableMapOf<String, Long>()
        map["humn"] = n

        fun Monkey.calcExpr(): Long? {
            val parts = this.expr.split(" ")
            if (parts.size == 1) {
                return parts[0].toLong()
            }

            val name1 = parts.first()
            val name2 = parts.last()

            if (name1 in map && name2 in map) {
                val value1 = map[name1]!!
                val value2 = map[name2]!!
                val operator = parts[1]

                return when (operator) {
                    "+" -> value1 + value2
                    "-" -> value1 - value2
                    "*" -> value1 * value2
                    "/" -> value1 / value2
                    "=" -> if (value1 == value2) 1 else -1
                    else -> null
                }
            }

            return null
        }

        while (map["root"] == null) {
            monkeys.forEach {
                if (map[it.name] == null) {
                    val calcExpr = it.calcExpr()
                    if (calcExpr != null) {
                        map[it.name] = calcExpr
                    }
                }
            }
        }

        return map["root"]!!
    }

    fun part2(input: List<String>): Long {
        var n = 0L

        while (true) {
            val res = trywith(input, n++)
            if (res == 1L) {
                return n-1
            }
        }
    }

    val input = readInput("inputs/Day21")
    println(part1(input))
    println(part2(input))
}
