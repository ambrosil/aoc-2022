fun main() {

    fun Set<Char>.score(): Int {
        val c = single()
        return if (c.isLowerCase()) {
            1 + (c - 'a')
        } else {
            27 + (c - 'A')
        }
    }

    fun String.halve(): List<String> {
        val half = length / 2
        return listOf(substring(0, half), substring(half))
    }

    fun part1(input: List<String>) =
        input.sumOf { item ->
            val (first, second) = item.halve()
            (first intersect second).score()
        }

    fun part2(input: List<String>) =
        input
            .chunked(3)
            .sumOf {
                it.fold(it[0].toSet()) {
                    acc, s -> acc intersect s
                }
                .score()
            }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

infix fun String.intersect(o: String) = toSet() intersect o.toSet()
infix fun Set<Char>.intersect(o: String) = this intersect o.toSet()
