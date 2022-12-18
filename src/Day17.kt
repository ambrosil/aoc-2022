import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val day17 = Day17(File("src/inputs/Day17.txt").readText())
    println(day17.part1())
    println(day17.part2())
}

class Day17(input: String) {

    private val jets = jets(input)
    private val shapes = shapes()
    private val cave = (0..6).map { Point(it, 0) }.toMutableSet()
    private val down = Point(0, 1)
    private val up = Point(0, -1)
    private var jetCounter = 0
    private var blockCounter = 0

    fun part1(): Int {
        repeat(2022) {
            simulate()
        }
        return cave.height()
    }

    fun part2(): Long =
        calculateHeight(1000000000000L - 1)

    private fun simulate() {
        var currentShape = shapes.nth(blockCounter++).moveToStart(cave.minY())
        do {
            val jetShape = currentShape * jets.nth(jetCounter++)
            if (jetShape in (0..6) && jetShape.intersect(cave).isEmpty()) {
                currentShape = jetShape
            }
            currentShape = currentShape * down
        } while (currentShape.intersect(cave).isEmpty())
        cave += (currentShape * up)
    }

    private fun calculateHeight(targetBlockCount: Long): Long {
        data class State(val ceiling: List<Int>, val blockMod: Int, val jetMod: Int)

        val seen: MutableMap<State, Pair<Int, Int>> = mutableMapOf()
        while (true) {
            simulate()
            val state = State(cave.normalizedCaveCeiling(), blockCounter % shapes.size, jetCounter % jets.size)
            if (state in seen) {
                val (blockCountAtLoopStart, heightAtLoopStart) = seen.getValue(state)
                val blocksPerLoop = blockCounter - 1L - blockCountAtLoopStart
                val totalLoops = (targetBlockCount - blockCountAtLoopStart) / blocksPerLoop
                val remainingBlocks = (targetBlockCount - blockCountAtLoopStart) - (totalLoops * blocksPerLoop)
                val heightGainedSinceLoop = cave.height() - heightAtLoopStart

                repeat (remainingBlocks.toInt()) {
                    simulate()
                }

                return cave.height() + heightGainedSinceLoop * (totalLoops - 1)
            }

            seen[state] = blockCounter - 1 to cave.height()
        }
    }

    private operator fun IntRange.contains(set: Set<Point>): Boolean = set.all { it.x in this }
    private operator fun Set<Point>.times(point: Point): Set<Point> = map { it + point }.toSet()
    private fun Set<Point>.minY(): Int = minOf { it.y }
    private fun Set<Point>.height(): Int = minY().absoluteValue

    private fun Set<Point>.normalizedCaveCeiling(): List<Int> {
        val let = groupBy { it.x }
            .entries
            .sortedBy { it.key }
            .map { pointList -> pointList.value.minBy { point -> point.y } }
            .let {
                val normalTo = this.minY()
                it.map { point -> normalTo - point.y }
            }
        return let
    }

    private fun Set<Point>.moveToStart(ceilingHeight: Int): Set<Point> =
        map { it + Point(2, ceilingHeight - 4) }.toSet()

    private fun shapes(): List<Set<Point>> =
        listOf(
            setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)),
            setOf(Point(1, 0), Point(0, -1), Point(1, -1), Point(2, -1), Point(1, -2)),
            setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, -1), Point(2, -2)),
            setOf(Point(0, 0), Point(0, -1), Point(0, -2), Point(0, -3)),
            setOf(Point(0, 0), Point(1, 0), Point(0, -1), Point(1, -1))
        )

    private fun jets(input: String): List<Point> =
        input.map {
            when (it) {
                '>' -> Point(1, 0)
                '<' -> Point(-1, 0)
                else -> throw IllegalStateException("Wrong jet $it")
            }
        }
}
