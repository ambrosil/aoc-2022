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

        val xRange = cubes.minOf { it.x } until cubes.maxOf { it.x }
        val yRange = cubes.minOf { it.y } until cubes.maxOf { it.y }
        val zRange = cubes.minOf { it.z } until cubes.maxOf { it.y }
        fun Cube.outOfRange() = (x in xRange && y in yRange && z in zRange).not()

        val allPoints = xRange.flatMap { x -> yRange.flatMap { y -> zRange.map { z -> Cube(x, y, z) } } }
        val trapped = allPoints.toMutableSet()

        tailrec fun Cube.removeFromTrapped(seen: Set<Cube>) {
            if (outOfRange()) trapped.removeAll(seen + this)
            else adjacents().filter { (it !in cubes) && (it !in seen) }.forEach {
                return it.removeFromTrapped(seen + this)
            }
        }

        allPoints.forEach { it.removeFromTrapped(setOf(it)) }
        return cubes.sumOf { (it.adjacents() - cubes - trapped).size }
    }

    val input = readInput("inputs/Day18")
    println(part1(input))
    println(part2(input))
}
