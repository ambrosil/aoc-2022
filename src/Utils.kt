import java.io.File

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun String.allInts() = allIntsInString(this)
fun allIntsInString(line: String): List<Int> {
    return """-?\d+""".toRegex().findAll(line)
        .map { it.value.toInt() }
        .toList()
}


fun <T> List<T>.nth(n: Int): T =
    this[n % size]

inline fun <T> List<T>.indexOfLast(predicate: (T) -> Boolean): Int {
    for (i in this.indices.reversed()) {
        if (predicate(this[i])) {
            return i
        }
    }

    return -1
}

data class Point(var x: Int = 0, var y: Int = 0) {
    operator fun plus(other: Point): Point {
        return Point(this.x + other.x, this.y + other.y)
    }

    fun adjacents(): Set<Point> =
        setOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1)
        )

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    operator fun plusAssign(p: Point) {
        x += p.x
        y += p.y
    }

    operator fun minus(point: Point): Point {
        return Point(x + point.x, y - point.y)
    }
}

inline fun List<List<Char>>.forEach(block: (row: Int, col: Int) -> Unit) {
    for (i in indices) {
        val inner = this[i]
        for (j in inner.indices) {
            block(i, j)
        }
    }
}

fun List<List<Char>>.maxOf(block: (row: Int, col: Int) -> Int): Int {
    var max = 0

    forEach { row, col ->
        val value = block(row, col)
        max = if (value > max) value else max
    }

    return max
}

fun List<List<Char>>.count(block: (row: Int, col: Int) -> Boolean): Int {
    var count = 0

    forEach { row, col ->
        count += if (block(row, col)) 1 else 0
    }

    return count
}