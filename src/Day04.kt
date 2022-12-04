fun main() {

    fun part1(input: List<String>) =
        input
            .readRanges()
            .count { (first, second) ->
                first contains second || second contains first
            }

    fun part2(input: List<String>) =
        input
            .readRanges()
            .count { (first, second) ->
                first overlaps second
            }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun List<String>.readRanges() =
    this.map {
        it.split(",").map { s ->
            val (start, end) = s.split("-")
            start.toInt()..end.toInt()
        }
    }

infix fun IntRange.contains(second: IntRange) = this.first <= second.first && this.last >= second.last
infix fun IntRange.overlaps(second: IntRange) = (this intersect second).isNotEmpty()

