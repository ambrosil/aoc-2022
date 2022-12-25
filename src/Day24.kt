fun main() {
    val d = Day24(readInput("inputs/Day24"))
    println(d.part1())
    println(d.part2())
}

class Day24(input: List<String>) {

    private val initialMapState: MapState = MapState.of(input)
    private val start: Point = Point(input.first().indexOfFirst { it == '.' }, 0)
    private val goal: Point = Point(input.last().indexOfFirst { it == '.' }, input.lastIndex)

    fun part1(): Int =
        solve().first

    fun part2(): Int {
        val toGoal = solve()
        val backToStart = solve(goal, start, toGoal.second, toGoal.first)
        val backToGoal = solve(start, goal, backToStart.second, backToStart.first)
        return backToGoal.first
    }

    private fun solve(
        startPlace: Point = start,
        stopPlace: Point = goal,
        startState: MapState = initialMapState,
        stepsSoFar: Int = 0
    ): Pair<Int, MapState> {
        val mapStates = mutableMapOf(stepsSoFar to startState)
        val queue = mutableListOf(PathAttempt(stepsSoFar, startPlace))
        val seen = mutableSetOf<PathAttempt>()

        while (queue.isNotEmpty()) {
            val thisAttempt = queue.removeFirst()
            if (thisAttempt !in seen) {
                seen += thisAttempt
                val nextMapState = mapStates.computeIfAbsent(thisAttempt.steps + 1) { key ->
                    mapStates.getValue(key - 1).nextState()
                }

                if (nextMapState.isOpen(thisAttempt.location)) queue.add(thisAttempt.next())
                val neighbors = thisAttempt.location.adjacents()
                if (stopPlace in neighbors) return Pair(thisAttempt.steps + 1, nextMapState)

                neighbors
                    .filter { it == start || (nextMapState.inBounds(it) && nextMapState.isOpen(it)) }
                    .forEach { neighbor ->
                        queue.add(thisAttempt.next(neighbor))
                    }
            }
        }

        throw IllegalStateException("No paths found")
    }

    private data class PathAttempt(val steps: Int, val location: Point) {
        fun next(place: Point = location): PathAttempt =
            PathAttempt(steps + 1, place)
    }

    private data class MapState(val boundary: Point, val blizzards: Set<Blizzard>) {
        private val unsafeSpots = blizzards.map { it.location }.toSet()

        fun isOpen(place: Point): Boolean =
            place !in unsafeSpots

        fun inBounds(place: Point): Boolean =
            place.x > 0 && place.y > 0 && place.x <= boundary.x && place.y <= boundary.y

        fun nextState(): MapState =
            copy(blizzards = blizzards.map { it.next(boundary) }.toSet())

        companion object {
            fun of(input: List<String>): MapState =
                MapState(
                    Point(input.first().lastIndex - 1, input.lastIndex - 1),
                    input.flatMapIndexed { y, row ->
                        row.mapIndexedNotNull { x, char ->
                            when (char) {
                                '>' -> Blizzard(Point(x, y), Point(1, 0))
                                '<' -> Blizzard(Point(x, y), Point(-1, 0))
                                'v' -> Blizzard(Point(x, y), Point(0, 1))
                                '^' -> Blizzard(Point(x, y), Point(0, -1))
                                else -> null
                            }
                        }
                    }.toSet()
                )
        }
    }

    private data class Blizzard(val location: Point, val offset: Point) {
        fun next(boundary: Point): Blizzard {
            var nextLocation = location + offset
            when {
                nextLocation.x == 0 -> nextLocation = Point(boundary.x, location.y)
                nextLocation.x > boundary.x -> nextLocation = Point(1, location.y)
                nextLocation.y == 0 -> nextLocation = Point(location.x, boundary.y)
                nextLocation.y > boundary.y -> nextLocation = Point(location.x, 1)
            }
            return copy(location = nextLocation)
        }
    }
}
