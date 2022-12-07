fun main() {

    data class Node(val name: String, var size: Int = 0, val dir: Boolean = false, val parent: Node? = null) {
        fun propagateSize() {
            var curr = parent
            while (curr != null) {
                curr.size += size
                curr = curr.parent
            }
        }
    }

    data class Common(val input: List<String>) {

        val nodes = mutableListOf<Node>()
        val root = Node(name = "/")

        init {
            var currentDir = root

            input
                .map { it.split(" ") }
                .forEach {
                    val (first, second) = it

                    if (first == "$" && second == "cd") {
                        when (val dirName = it[2]) {
                            ".." -> currentDir = currentDir.parent!!
                            "/" -> currentDir = root
                            else -> {
                                currentDir = Node(name = dirName, dir = true, parent = currentDir)
                                nodes.add(currentDir)
                            }
                        }
                    } else if (first.isNumber()) {
                        nodes.add(Node(name = second, size = first.toInt(), parent = currentDir))
                    }
                }

            nodes.filterNot { it.dir }
                 .forEach { it.propagateSize() }
        }
    }

    fun part1(input: List<String>): Int {
        val common = Common(input)

        return common.nodes
            .filter { it.dir && it.size <= 100000 }
            .sumOf { it.size }
    }

    fun part2(input: List<String>): Int {
        val common = Common(input)
        val freeSpace = 70000000 - common.root.size

        return common.nodes
            .filter { it.dir }
            .fold(70000000) { acc, item ->
                if (freeSpace + item.size >= 30000000 && acc > item.size) {
                    item.size
                } else {
                    acc
                }
        }
    }

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun String.isNumber(): Boolean {
    return try {
        toInt()
        true
    } catch (e: Exception) {
        false
    }
}
