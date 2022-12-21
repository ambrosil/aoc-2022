fun main(args: Array<String>) {
    val input = readInput("inputs/Day20").mapIndexed { i, v -> i to v.toLong() }
    val ans1 = solve(input.toMutableList(), 1, 1)
    val ans2 = solve(input.toMutableList(), 10, 811589153)
    println(ans1)
    println(ans2)
}

fun solve(queue: MutableList<Pair<Int, Long>>, repeat: Int, key: Long): Long {
    repeat(repeat) {
        for (k in queue.indices) {
            val i = queue.indexOfFirst { it.first == k }
            val temp = queue[i]
            queue.removeAt(i)
            queue.add((i + key*temp.second).mod(queue.size), temp)
        }
    }
    val iZero = queue.indexOfFirst { it.second == 0L }
    return listOf(1000, 2000, 3000).sumOf { key*queue[(iZero + it)%queue.size].second }
}