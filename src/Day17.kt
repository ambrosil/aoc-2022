fun main() {

    data class Point(var x: Long, var y: Long) {
        operator fun plus(p: Point): Point {
            return Point(x + p.x, y + p.y)
        }
    }

    data class Rock(var points: List<Point>) {
        fun left() = points.minOf { it.x }
        fun top() = points.maxOf { it.y }
        fun bottom() = points.minOf { it.y }

        fun move(direction: Point) {
            points = points.map { it + direction }
        }
    }

    class RockGenerator(val maxLoops: Long) {
        var counter = 0
        val shapes = buildList {
            // ####
            add(Rock(listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))))

            // .#.
            // ###
            // .#.
            add(Rock(listOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2))))

            // ..#
            // ..#
            // ###
            add(Rock(listOf(Point(2, 2), Point(2, 1), Point(0, 0), Point(1, 0), Point(2, 0))))

            // #
            // #
            // #
            // #
            add(Rock(listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))))

            // ##
            // ##
            add(Rock(listOf(Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 1))))
        }

        fun hasNext() = counter < maxLoops
        fun next() = shapes[counter++ % shapes.size].copy()
    }

    data class Terrain(val bounds: IntRange) {
        val rocks = mutableListOf<Rock>()
        lateinit var currentRock: Rock

        fun print() {
            val points = rocks.flatMap { it.points }
            val maxY = 20L

            for (y in 0..20L) {
                for (x in 0..6L) {
                    if (maxY - y == 0L) {
                        print("\uD83D\uDFE7")
                        continue
                    }

                    val exists = points.any { it.x == x && it.y == maxY - y }
                    if (exists) {
                        print("\uD83D\uDFE6")
                    } else {
                        print("⬛")
                    }
                }
                println()
            }
        }

        fun height() = if (rocks.isEmpty()) 0 else rocks.maxOf { it.top() }

        fun addRock(rock: Rock) {
            val height = height()

            while (rock.left() < 2) {
                rock.move(Point(1, 0))
            }

            rock.move(Point(0, height))

            while (rock.bottom() - height <= 3) {
                rock.move(Point(0, 1))
            }

            rocks += rock
            currentRock = rock
        }

        val otherPoints get() = (rocks - currentRock).flatMap { it.points }

        fun moveX(direction: Point) {
            val canMove = currentRock.points.all {
                val newPosition = it + direction
                val canMoveX = newPosition.x >= bounds.first && newPosition.x <= bounds.last
                val canMove = !otherPoints.any { o -> o.x == newPosition.x && o.y == newPosition.y }

                canMoveX && canMove
            }

            if (canMove) {
                currentRock.move(direction)
            }
        }

        fun moveByJet(jet: Char) {
            when (jet) {
                '<' -> moveX(Point(-1, 0))
                '>' -> moveX(Point(+1, 0))
            }
        }

        fun moveDown(): Boolean {
            val direction =  Point(0, -1)

            val canMove = currentRock.points.all {
                val newPosition = it + direction
                val canMove = !otherPoints.any { o -> o.x == newPosition.x && o.y == newPosition.y }
                val canMoveY = newPosition.y > 0

                canMove && canMoveY
            }

            if (canMove) {
                currentRock.move(direction)
                return true
            }

            return false
        }
    }

    class JetsGenerator(input: List<String>) {
        val jets = input.single().toList()
        var counter = 0
        fun next() = jets[counter++ % jets.size]
    }

    fun part1(input: List<String>): Long {
        val jetsGenerator = JetsGenerator(input)
        val terrain = Terrain(0..6)
        val rockGenerator = RockGenerator(2022)

        while (rockGenerator.hasNext()) {
            terrain.addRock(rockGenerator.next())
            terrain.moveByJet(jetsGenerator.next())

            while (terrain.moveDown()) {
                terrain.moveByJet(jetsGenerator.next())
            }
        }

        return terrain.height()
    }

    fun part2(input: List<String>): Long {
        val N = 1_000_000_000_000L
        val jetsGenerator = JetsGenerator(input)
        val terrain = Terrain(0..6)
        val rockGenerator = RockGenerator(1000L)

        while (rockGenerator.hasNext()) {
            terrain.addRock(rockGenerator.next())
            terrain.moveByJet(jetsGenerator.next())

            while (terrain.moveDown()) {
                terrain.moveByJet(jetsGenerator.next())
            }
        }

        return (N / 1000) * terrain.height()
    }

    val input = readInput("inputs/test")
    println(part1(input))
    println(part2(input))
}
