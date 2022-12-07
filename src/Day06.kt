fun main() {
    fun part1(input: List<String>) = input.single() findMarker 4
    fun part2(input: List<String>) = input.single() findMarker 14

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

infix fun String.findMarker(size: Int): Int {
    val windowedList = toList().windowed(size)
    val index = toList().windowed(size).indexOf(windowedList.find { it.size == it.toSet().size })
    return size + index
}
