fun main() {

    fun Set<Char>.points(): Int {
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
            first.toSet()
                .intersect(second.toSet())
                .points()
        }

    fun part2(input: List<String>) =
        input.chunked(3)
            .map {
                it.fold(it[0].toSet()) { acc, s ->
                    acc.intersect(s.toSet())
                }
            }
            .sumOf {
                it.points()
            }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
