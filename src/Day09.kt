import kotlin.math.abs
import kotlin.math.sign

data class Point(var x: Int, var y: Int) {
    operator fun plusAssign(p: Point) {
        x += p.x
        y += p.y
    }

    fun follow(head: Point) {
        val dx = head.x - x
        val dy = head.y - y

        if (abs(dx) > 1 || abs(dy) > 1) {
            this += Point(dx.sign, dy.sign)
        }
    }
}

fun main() {

    fun draw(knots: List<Point>) {
        val size = 10
        for (j in -size..size) {
            for (i in -size..size) {
                val knot = knots.find { it.x == i && it.y == j }
                if (knot != null) {
                    if (knots.indexOf(knot) != 0) {
                        print("🟨")
                    } else print("\uD83D\uDFE5")
                } else {
                    print("⬛")
                }
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val head = Point(0, 0)
        val tail = Point(0, 0)
        val visited = mutableSetOf<Point>()

        input.map {
            it.split(" ")
        }.forEach { (direction, amount) ->
            val dirVector = when (direction) {
                "U" -> Point(0, +1)
                "D" -> Point(0, -1)
                "L" -> Point(-1, 0)
                "R" -> Point(+1, 0)
                else -> error("wrong direction $direction")
            }

            for (i in 0 until amount.toInt()) {
                head += dirVector
                tail.follow(head)
                visited.add(tail.copy())
            }
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        val knots = MutableList(10) { Point(0, 0) }
        val visited = mutableSetOf<Point>()

        input.map {
            it.split(" ")
        }.forEach { (direction, amount) ->
            val dirVector = when (direction) {
                "U" -> Point(0, +1)
                "D" -> Point(0, -1)
                "L" -> Point(-1, 0)
                "R" -> Point(+1, 0)
                else -> error("wrong direction $direction")
            }

            for (i in 0 until amount.toInt()) {
                knots.forEachIndexed { index, knot ->
                    if (index == 0) {
                        knot += dirVector
                    } else {
                        knot.follow(knots[index - 1])
                    }

                    if (index == 9) {
                        visited.add(knot.copy())
                    }
                }

                //draw(knots)
            }

            Thread.sleep(100)
        }

        return visited.size
    }

    val input = readInput("inputs/Day09")
    println(part1(input))
    println(part2(input))
}
