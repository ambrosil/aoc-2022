import Type.*

enum class Type { NONE, SAND, WALL }

fun main() {

    fun parse(input: List<String>): MutableMap<Point, Type> {
        val terrain = mutableMapOf<Point, Type>()

        val walls = input.map { it.split(" -> ")
                .map { it.split(",") }
                .map { (x, y) -> Point(x.toInt(), y.toInt()) }
        }

        walls.forEach { wall ->
            wall.reduce { p1, p2 ->
                val xRange = if (p1.x > p2.x) {
                    p2.x..p1.x
                } else {
                    p1.x..p2.x
                }

                val yRange = if (p1.y > p2.y) {
                    p2.y..p1.y
                } else {
                    p1.y..p2.y
                }

                for (x in xRange) {
                    terrain[Point(x, p1.y)] = WALL
                }
                for (y in yRange) {
                    terrain[Point(p1.x, y)] = WALL
                }

                p2
            }
        }

        return terrain
    }

    fun findMaxY(terrain: MutableMap<Point, Type>): Int {
        return terrain.keys.maxOf { it.y }
    }

    fun addSand(terrain: MutableMap<Point, Type>): Boolean {
        val maxY = findMaxY(terrain)
        val sandPoint = Point(500, 0)
        while (true) {
            sandPoint += Point(0, 1)

            if (sandPoint.y > maxY) {
                return false
            }

            val type = terrain[sandPoint] ?: NONE
            when (type) {
                NONE -> continue
                SAND, WALL -> {
                    val leftType = terrain[sandPoint + Point(-1, 0)] ?: NONE
                    when (leftType) {
                        NONE -> sandPoint += Point(-1, 0)
                        WALL, SAND -> {
                            val rightType = terrain[sandPoint + Point(+1, 0)] ?: NONE
                            if (rightType == NONE) {
                                sandPoint += Point(+1, 0)
                            } else {
                                terrain[sandPoint + Point(0, -1)] = SAND
                                return true
                            }
                        }
                    }
                }
            }
        }
    }

    fun addSand2(terrain: MutableMap<Point, Type>, maxY: Int): Boolean {
        val sandPoint = Point(500, 0)
        while (true) {
            sandPoint += Point(0, 1)

            if (sandPoint.y >= maxY+2) {
                terrain[sandPoint + Point(0, -1)] = SAND
                return true
            }

            val type = terrain[sandPoint] ?: NONE
            when (type) {
                NONE -> continue
                SAND, WALL -> {
                    val leftType = terrain[sandPoint + Point(-1, 0)] ?: NONE
                    when (leftType) {
                        NONE -> sandPoint += Point(-1, 0)
                        WALL, SAND -> {
                            val rightType = terrain[sandPoint + Point(+1, 0)] ?: NONE
                            if (rightType == NONE) {
                                sandPoint += Point(+1, 0)
                            } else {
                                val restPoint = sandPoint + Point(0, -1)
                                terrain[restPoint] = SAND

                                if (restPoint.x == 500 && restPoint.y == 0) {
                                    return false
                                }

                                return true
                            }
                        }
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val terrain = parse(input)

        var counter = 0
        while (addSand(terrain)) {
            counter++
        }

        return counter
    }

    fun part2(input: List<String>): Int {
        val terrain = parse(input)
        val maxY = findMaxY(terrain)

        var counter = 0
        while (addSand2(terrain, maxY)) {
            counter++
        }

        return counter+1
    }

    val input = readInput("inputs/Day14")
    println(part1(input))
    println(part2(input))
}
