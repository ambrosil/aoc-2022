fun main() {

    data class Cube(val x: Int, val y: Int, val z: Int)
    fun Cube.adjacents(): List<Cube> {
        return listOf(
            Cube(x+1, y, z), Cube(x-1, y, z),
            Cube(x, y+1, z), Cube(x, y-1, z),
            Cube(x, y, z+1), Cube(x, y, z-1),
        )
    }

    fun parse(input: List<String>): Set<Cube> {
        return input.map {
            it.split(",").map { n -> n.toInt() }
        }
        .map { (x, y, z) -> Cube(x, y, z) }
        .toSet()
    }

    fun part1(input: List<String>): Int {
        val cubes = parse(input)
        return cubes.sumOf { (it.adjacents() - cubes).size }
    }

    fun part2(input: List<String>): Int {
        val cubes = parse(input)

        val xRange = cubes.minOf { it.x } - 1..cubes.maxOf { it.x } + 1
        val yRange = cubes.minOf { it.y } - 1..cubes.maxOf { it.y } + 1
        val zRange = cubes.minOf { it.z } - 1..cubes.maxOf { it.z } + 1

        val queue = ArrayDeque<Cube>().apply { add(Cube(xRange.first, yRange.first, zRange.first)) }
        val seen = mutableSetOf<Cube>()
        var sides = 0

        queue.forEach { current ->
            if (current !in seen) {
                seen += current

                current.adjacents()
                    .filter { it.x in xRange && it.y in yRange && it.z in zRange }
                    .forEach { adj -> if (adj in cubes) sides++ else queue.add(adj) }
            }
        }

        return sides
    }

    val input = readInput("inputs/Day18")
    println(part1(input))
    println(part2(input))
}
