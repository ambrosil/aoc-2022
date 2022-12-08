fun main() {
    fun part1(input: List<String>): Int {
        var elfNum = 0
        return input.groupBy { if (it.isBlank()) elfNum++; elfNum }
                    .map { elf -> elf.value.filterNot { calories -> calories.isBlank() } }
                    .maxOf { it.sumOf { n -> n.toInt() } }
    }

    fun part2(input: List<String>): Int {
        var elfNum = 0
        return input.groupBy { if (it.isBlank()) elfNum++; elfNum }
            .map { elf -> elf.value.filterNot { calories -> calories.isBlank() } }
            .map { it.sumOf { n -> n.toInt() } }
            .sortedDescending()
            .take(3)
            .sum()
    }

    val input = readInput("inputs/Day01")
    println(part1(input))
    println(part2(input))
}
