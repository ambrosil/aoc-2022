import Sign.*
import java.lang.RuntimeException

enum class Sign { ROCK, PAPER, SCISSORS }
data class Round(val opponent: Sign, val mine: Sign) {

    private fun signPoints() =
        when (mine) {
            ROCK -> 1
            PAPER -> 2
            SCISSORS -> 3
        }

    private fun matchPoints() =
        when (mine) {
            ROCK -> when (opponent) {
                ROCK -> 3
                PAPER -> 0
                SCISSORS -> 6
            }

            PAPER -> when (opponent) {
                ROCK -> 6
                PAPER -> 3
                SCISSORS -> 0
            }

            SCISSORS -> when (opponent) {
                ROCK -> 0
                PAPER -> 6
                SCISSORS -> 3
            }
        }

    fun totalPoints() = signPoints() + matchPoints()
}

fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { it.split(" ") }
            .map {
                it.map { sign ->
                    when (sign) {
                        "A", "X" -> ROCK
                        "B", "Y" -> PAPER
                        "C", "Z" -> SCISSORS
                        else -> throw RuntimeException()
                    }
                }
            }
            .map { (opponent, mine) -> Round(opponent, mine) }
            .sumOf { it.totalPoints() }
    }

    fun part2(input: List<String>): Int {
        val signs = listOf(ROCK, SCISSORS, PAPER)

        return input
            .map { it.split(" ") }
            .map { (opponentSign, mySign) ->
                val opponent = when (opponentSign) {
                    "A" -> ROCK
                    "B" -> PAPER
                    "C" -> SCISSORS
                    else -> throw RuntimeException()
                }

                val mine = when (mySign) {
                    "X" -> signs[(signs.indexOf(opponent) + 1) % signs.size]
                    "Y" -> opponent
                    "Z" -> signs[(signs.indexOf(opponent) + 2) % signs.size]
                    else -> throw RuntimeException()
                }
                Round(opponent, mine)
            }
            .sumOf { it.totalPoints() }
    }

    val input = readInput("inputs/Day02")
    println(part1(input))
    println(part2(input))
}
