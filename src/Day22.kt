import Tile.*

enum class Tile { WALL, VOID, FREE }

fun main() {

    fun Int.score(): Int {
        return when (this) {
            0 -> 3
            -270, 90 -> 0
            -180, 180  -> 1
            -90, 270 -> 2
            else -> error("wrong degree $this")
        }
    }

    fun Int.toVector(): Point {
        return when (this) {
            0 -> Point(0, -1)
            -270, 90 -> Point(1, 0)
            -180, 180 -> Point(0, 1)
            -90, 270 -> Point(-1, 0)
            else -> error("wrong direction $this")
        }
    }

    fun List<List<Tile>>.at(point: Point): Tile {
        return try {
            this[point.y][point.x]
        } catch (e: Exception) {
            VOID
        }
    }

    fun List<List<Tile>>.column(x: Int): List<Tile> {
        return this.flatMap { item ->
            item.filterIndexed { index, _ -> index == x }
        }
    }

    fun List<List<Tile>>.startPoint(): Point {
        val x = this.first().indexOfFirst { it == FREE }
        return Point(x, 0)
    }

    fun String.parseMap(): List<List<Tile>> {
        val list = this.split("\n")
        return list.map {
            it.map { c ->
                when (c) {
                    '#' -> WALL
                    '.' -> FREE
                    else -> VOID
                }
            }
        }
    }

    fun String.parseDirections(): List<Pair<String, Int>> {
        val digits = """-?\d+""".toRegex().findAll(this).map { it.value }.map { it.toInt() }.toList()
        val letters = """-?\D+""".toRegex().findAll(this).map { it.value }.toList()

        (letters as ArrayList).add(0, "A")
        return letters.zip(digits)
    }

    fun part1(input: List<String>): Int {
        val (mapString, directionsString) = input.joinToString("\n").split("\n\n")

        val map = mapString.parseMap()
        val directions = directionsString.parseDirections()

        val cursor = map.startPoint()
        var degree = 90

        directions.forEach { direction ->
            when (direction.first) {
                "R" -> degree += 90
                "L" -> degree -= 90
            }

            degree %= 360

            repeat (direction.second) {
                val dirVector = degree.toVector()
                val newPos = cursor + dirVector
                if (map.at(newPos) == FREE) {
                    cursor += degree.toVector()
                } else if (map.at(newPos) == VOID) {
                    if (dirVector.x == 1) {
                        val idxFree = map[newPos.y].indexOfFirst { it == FREE }
                        val idxWall = map[newPos.y].indexOfFirst { it == WALL }
                        if (idxWall == -1 || idxWall > idxFree) {
                            cursor.set(idxFree, newPos.y)
                        }
                    } else if (dirVector.x == -1) {
                        val idxFree = map[newPos.y].indexOfLast { it == FREE }
                        val idxWall = map[newPos.y].indexOfLast { it == WALL }
                        if (idxWall == -1 || idxWall < idxFree) {
                            cursor.set(idxFree, newPos.y)
                        }
                    } else if (dirVector.y == -1) {
                        val idxFree = map.column(newPos.x).indexOfLast { it == FREE }
                        val idxWall = map.column(newPos.x).indexOfLast { it == WALL }
                        if (idxWall == -1 || idxWall < idxFree) {
                            cursor.set(newPos.x, idxFree)
                        }
                    } else if (dirVector.y == 1) {
                        val idxFree = map.column(newPos.x).indexOfFirst { it == FREE }
                        val idxWall = map.column(newPos.x).indexOfFirst { it == WALL }
                        if (idxWall == -1 || idxWall > idxFree) {
                            cursor.set(newPos.x, idxFree)
                        }
                    }
                }
            }
        }

        return 1000 * (cursor.y+1) + 4 * (cursor.x+1) + degree.score()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("inputs/Day22")
    println(part1(input))
    println(part2(input))
}


