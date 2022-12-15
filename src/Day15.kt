import kotlin.math.abs
import kotlin.math.sign

fun main() {

    fun List<IntRange>.union(): List<IntRange> {
        val sorted = sortedBy { it.first }
        val init = mutableListOf(sorted.first())

        return sortedBy { it.first }.fold(init) { acc, r ->
            val last = acc.last()

            if (r.first <= last.last) {
                acc[acc.lastIndex] = (last.first..maxOf(last.last, r.last))
            } else {
                acc += r
            }

            acc
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun frequency() = (x * 4_000_000L) + y

        infix fun distanceTo(other: Point): Int {
            return abs(x - other.x) + abs(y - other.y)
        }

        infix fun lineTo(other: Point): List<Point> {
            val xDelta = (other.x - x).sign
            val yDelta = (other.y - y).sign
            val steps = maxOf(abs(x - other.x), abs(y - other.y))
            return (1..steps).scan(this) { prev, _ -> Point(prev.x + xDelta, prev.y + yDelta) }
        }

        fun findRange(y: Int, distance: Int): IntRange? {
            val width = distance - abs(this.y - y)
            return (this.x - width..this.x + width).takeIf { it.first <= it.last }
        }
    }

    fun parse(input: List<String>): MutableList<Pair<Point, Point>> {
        return input.map {
            val sensorX = it.substringAfter("Sensor at x=").substringBefore(",").toInt()
            val sensorY = it.substringBefore(":").substringAfter("y=").toInt()

            val beaconX = it.substringAfter("closest beacon is at x=").substringBefore(",").toInt()
            val beaconY = it.substringAfterLast("y=").toInt()

            Pair(Point(sensorX, sensorY), Point(beaconX, beaconY))
        }
        .toMutableList()
    }

    fun part1(input: List<String>): Int {
        val pairs = parse(input)
        val y = 2_000_000

        return pairs
            .mapNotNull { (sensor, beacon) ->
                val distance = sensor distanceTo beacon
                sensor.findRange(y, distance)
            }
            .union()
            .sumOf { it.last - it.first }
    }

    fun part2(input: List<String>): Long {
        val pairs = parse(input)
        val cave = 0..4_000_000L

        return pairs.firstNotNullOf { (sensor, beacon) ->
            val distance = sensor distanceTo beacon

            val up = Point(sensor.x, sensor.y - distance - 1)
            val down = Point(sensor.x, sensor.y + distance + 1)
            val left = Point(sensor.x - distance - 1, sensor.y)
            val right = Point(sensor.x + distance + 1, sensor.y)

            (up.lineTo(right) + right.lineTo(down) + down.lineTo(left) + left.lineTo(up))
                .filter { it.x in cave && it.y in cave }
                .firstOrNull { point ->
                    pairs.all { (sensor, beacon) ->
                        sensor distanceTo point > sensor distanceTo beacon
                    }
                }
        }.frequency()
    }

    val input = readInput("inputs/Day15")
    println(part1(input))
    println(part2(input))
}
