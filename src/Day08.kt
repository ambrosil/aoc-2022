fun main() {

    fun part1(input: List<String>): Int {
        val matrix = input.map { it.toList() }
        return matrix.count { row, col -> matrix.visible(row, col) }
    }

    fun part2(input: List<String>): Int {
        val matrix = input.map { it.toList() }
        return matrix.maxOf { row, col -> matrix.score(row, col) }
    }

    val input = readInput("inputs/Day08")
    println(part1(input))
    println(part2(input))
}

fun List<List<Char>>.forEachRowCol(block: (row: Int, col: Int) -> Unit) {
    for (i in indices) {
        val inner = this[i]
        for (j in inner.indices) {
            block(i, j)
        }
    }
}

fun List<List<Char>>.maxOf(block: (row: Int, col: Int) -> Int): Int {
    var max = 0

    forEachRowCol { row, col ->
        val value = block(row, col)
        max = if (value > max) value else max
    }

    return max
}

fun List<List<Char>>.count(block: (row: Int, col: Int) -> Boolean): Int {
    var count = 0

    forEachRowCol { row, col ->
        count += if (block(row, col)) 1 else 0
    }

    return count
}

fun List<List<Char>>.score(row: Int, col: Int) = scoreUp(row, col) * scoreDown(row, col) * scoreLeft(row, col) * scoreRight(row, col)

fun List<List<Char>>.scoreUp(row: Int, col: Int): Int {
    var count = 0

    for (i in row-1 downTo 0) {
        count++

        if (this[i][col] >= this[row][col]) {
            break
        }
    }

    return count
}

fun List<List<Char>>.scoreDown(row: Int, col: Int): Int {
    var count = 0

    for (i in row+1 until size) {
        count++

        if (this[i][col] >= this[row][col]) {
            break
        }
    }

    return count
}

fun List<List<Char>>.scoreLeft(row: Int, col: Int): Int {
    var count = 0

    for (i in col-1 downTo 0) {
        count++

        if (this[row][i] >= this[row][col]) {
            break
        }
    }

    return count
}

fun List<List<Char>>.scoreRight(row: Int, col: Int): Int {
    var count = 0

    for (i in col+1 until first().size) {
        count++

        if (this[row][i] >= this[row][col]) {
            break
        }
    }

    return count
}

fun List<List<Char>>.visibleUp(row: Int, col: Int): Boolean {
    for (i in 0 until row) {
        if (this[i][col] >= this[row][col]) {
            return false
        }
    }

    return true
}

fun List<List<Char>>.visibleDown(row: Int, col: Int): Boolean {
    for (i in row+1 until size) {
        if (this[i][col] >= this[row][col]) {
            return false
        }
    }

    return true
}

fun List<List<Char>>.visibleLeft(row: Int, col: Int): Boolean {
    for (i in 0 until col) {
        if (this[row][i] >= this[row][col]) {
            return false
        }
    }

    return true
}

fun List<List<Char>>.visibleRight(row: Int, col: Int): Boolean {
    for (i in col+1 until first().size) {
        if (this[row][i] >= this[row][col]) {
            return false
        }
    }

    return true
}

fun List<List<Char>>.visible(row: Int, col: Int): Boolean {
    if (row == 0 || col == 0) {
        return true
    } else if (row == size-1 || col == first().size-1) {
        return true
    }

    return visibleUp(row, col) || visibleDown(row, col) || visibleRight(row, col) || visibleLeft(row, col)
}
