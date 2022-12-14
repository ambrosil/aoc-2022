fun main() {
    val d = Day23(readInput("inputs/Day23"))
    println(d.part1())
    println(d.part2())
}

class Day23(input: List<String>) {

    private val startingPositions = parseInput(input)
    private val nextTurnOffsets: List<List<Point>> = createOffsets()

    fun part1(): Int {
        val locations = (0 until 10).fold(startingPositions) { carry, round -> carry.playRound(round) }
        val gridSize = ((locations.maxOf { it.x } - locations.minOf { it.x }) + 1) * ((locations.maxOf { it.y } - locations.minOf { it.y }) + 1)
        return gridSize - locations.size
    }

    fun part2(): Int {
        var thisTurn = startingPositions
        var roundId = 0
        do {
            val previousTurn = thisTurn
            thisTurn = previousTurn.playRound(roundId++)
        } while (previousTurn != thisTurn)
        return roundId
    }

    fun Point.neighbors(): Set<Point> =
        setOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1)
        )

    private fun Set<Point>.playRound(roundNumber: Int): Set<Point> {
        val nextPositions = this.toMutableSet()
        val movers: Map<Point, Point> = this
            .filter { elf -> elf.neighbors().any { it in this } }
            .mapNotNull { elf ->
                nextTurnOffsets.indices.map { direction -> nextTurnOffsets[(roundNumber + direction) % 4] }
                    .firstNotNullOfOrNull { offsets ->
                        if (offsets.none { offset -> (elf + offset) in this }) elf to (elf + offsets.first())
                        else null
                    }
            }.toMap()

        val safeDestinations = movers.values.groupingBy { it }.eachCount().filter { it.value == 1 }.keys
        movers
            .filter { (_, target) -> target in safeDestinations }
            .forEach { (source, target) ->
                nextPositions.remove(source)
                nextPositions.add(target)
            }
        return nextPositions
    }

    private fun createOffsets(): List<List<Point>> =
        listOf(
            listOf(Point(0, -1), Point(-1, -1), Point(1, -1)), // N
            listOf(Point(0, 1), Point(-1, 1), Point(1, 1)), // S
            listOf(Point(-1, 0), Point(-1, -1), Point(-1, 1)), // W
            listOf(Point(1, 0), Point(1, -1), Point(1, 1)), // E
        )

    private fun parseInput(input: List<String>): Set<Point> =
        input.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, char ->
                if (char == '#') Point(x, y) else null
            }
        }.toSet()
}