fun main() {
    val d = Day25(readInput("inputs/Day25"))
    println(d.part1())
}

class Day25(private val input: List<String>) {

    fun part1(): String=
        input.sumOf { it.fromSnafu() }.toSnafu()

    private fun String.fromSnafu(): Long =
        fold(0) { carry, char ->
            (carry * 5) + when(char) {
                '-' -> -1
                '=' -> -2
                else -> char.digitToInt()
            }
        }

    private fun Long.toSnafu(): String =
        generateSequence(this) { (it + 2) / 5 }
            .takeWhile { it != 0L }
            .map { "012=-"[(it % 5).toInt()] }
            .joinToString("")
            .reversed()

}