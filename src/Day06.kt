fun main() {

    fun part1(input: List<String>) = input.single() findMarker 4
    fun part2(input: List<String>) = input.single() findMarker 14

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

infix fun String.findMarker(size: Int): Int {
    val windowedList = toList().windowed(size)
    val index = windowedList.indexOf(windowedList.find { list -> list.isUnique() })
    return size + (index - 1) + 1
}

fun List<Char>.isUnique(): Boolean {
    return size == toSet().size
}

