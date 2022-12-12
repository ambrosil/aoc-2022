fun main() {

    data class Point(val x: Int, val y: Int)

    class Terrain constructor(input: List<String>) {
        val distances: HashMap<Point, Int> = HashMap()
        var queue: ArrayDeque<Point> = ArrayDeque()
        var heightmap: List<List<Char>> = input.map { it.toList() }

        fun enqueue(point: Point, distance: Int) {
            if (distances[point] == null || distances[point]!! > distance) {
                queue += point
                distances[point] = distance
            }
        }

        fun find(c: Char): Point {
            heightmap.forEachIndexed { x, _ ->
                heightmap[x].forEachIndexed { y, _ ->
                    if (heightmap[x][y] == c) {
                        return Point(x, y)
                    }
                }
            }

            error("Not found $c")
        }

        fun height(point: Point): Int {
            val height = heightmap[point.x][point.y]
            return when (height) {
                'S' -> 0
                'E' -> 'z' - 'a'
                else -> height - 'a'
            }
        }

        fun minSteps(start: Point, end: Point): Int {
            enqueue(start, 0)

            while (queue.isNotEmpty()) {
                val point = queue.removeFirst()
                val distance = distances[point]!! + 1
                val height = height(point)

                check(Point(point.x-1, point.y), distance, height)
                check(Point(point.x+1, point.y), distance, height)
                check(Point(point.x, point.y-1), distance, height)
                check(Point(point.x, point.y+1), distance, height)
            }

            return distances[end] ?: Int.MAX_VALUE
        }

        fun check(p: Point, distance: Int, height: Int) {
            if (p.x !in heightmap.indices || p.y !in 0 until heightmap[0].size) return
            if (height <= height(p) + 1) {
                enqueue(p, distance)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val terrain = Terrain(input)
        val start = terrain.find('E')
        val end = terrain.find('S')
        return terrain.minSteps(start, end)
    }

    fun part2(input: List<String>): Int {
        val endingPoints = mutableListOf<Point>()
        val terrain = Terrain(input)

        terrain.heightmap.forEachIndexed { x, _ ->
            terrain.heightmap[x].forEachIndexed { y, _ ->
                val c = terrain.heightmap[x][y]
                if (c == 'a' || c == 'S') {
                    endingPoints += Point(x, y)
                }
            }
        }

        val start = terrain.find('E')
        terrain.minSteps(start, endingPoints[0])

        return endingPoints.minOf { terrain.distances[it] ?: Int.MAX_VALUE }
    }

    val input = readInput("inputs/Day12")
    println(part1(input))
    println(part2(input))
}
