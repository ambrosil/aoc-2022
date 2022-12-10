fun main() {

    data class Step(val cycle: Int, val sum: Int)

    fun run(input: List<String>): MutableList<Step>{
        var cycle = 1
        var sum = 1
        val steps = mutableListOf<Step>()

        input.forEach {
            when (it) {
                "noop" -> steps.add(Step(cycle++, sum))
                else -> {
                    val (_, amount) = it.split(" ")
                    steps += Step(cycle++, sum)
                    steps += Step(cycle++, sum)
                    sum += amount.toInt()
                }
            }
        }

        return steps
    }

    fun part1(input: List<String>): Int {
        val cycles = List(6) { i -> 20 + i * 40 }
        return run(input)
            .filter { it.cycle in cycles }
            .sumOf { it.cycle * it.sum }
    }

    fun part2(input: List<String>) {
        run(input)
            .map {
                val sprite = it.sum - 1..it.sum + 1
                if ((it.cycle - 1) % 40 in sprite) "🟨" else "⬛"
            }
            .chunked(40)
            .forEach { println(it.joinToString("")) }
    }

    val input = readInput("inputs/Day10")
    println(part1(input))
    part2(input)
}
