import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

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