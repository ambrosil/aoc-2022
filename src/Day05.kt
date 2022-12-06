fun main() {

    data class Common(val input: List<String>) {

        lateinit var moves: List<String>
        lateinit var stacks: List<ArrayDeque<Char>>

        init {
            parse()
        }

        fun parse() {
            val map = input.groupBy {
                when {
                    it.contains("move") -> "moves"
                    it.contains("[") -> "crates"
                    else -> "garbage"
                }
            }

            val maxStringLength = map["crates"]!!.maxOf { it.length }
            val crates = map["crates"]!!
                .map {
                    it.padEnd(maxStringLength)
                        .toList()
                        .chunked(4)
                        .map { list -> list[1] }
                }

            val rowLength = crates.first().size
            stacks = List(rowLength) { ArrayDeque() }

            crates.forEach {
                it.forEachIndexed {
                    i, c -> stacks[i].addLast(c)
                }
            }

            moves = map["moves"]!!.map {
                it.replace("move", "")
                  .replace("from", "")
                  .replace("to", "")
                  .trim()
            }
        }
    }

    fun part1(input: List<String>): String {
        val common = Common(input)

        common.moves.forEach {
            val (count, from, to) = it.split(" ").filterNot { s -> s.isBlank() }.map { n -> n.toInt() }
            repeat (count) {
                var element: Char
                do {
                    element = common.stacks[from - 1].removeFirst()
                } while(element == ' ')

                common.stacks[to-1].addFirst(element)
            }
        }

        return List(common.stacks.size) { i ->
            common.stacks[i].removeFirst()
        }.joinToString(separator = "")
    }

    fun part2(input: List<String>): String {
        val main = Common(input)

        main.moves.forEach {
            val (count, from, to) = it.split(" ").filterNot { s -> s.isBlank() }.map { n -> n.toInt() }
            val cratesToMove = mutableListOf<Char>()

            repeat (count) {
                if (main.stacks[from -1].isNotEmpty()) {
                    var element: Char
                    do {
                        element = main.stacks[from - 1].removeFirst()
                        if (element != ' ') {
                            cratesToMove.add(element)
                        }
                    } while (element == ' ')
                }
            }

            cratesToMove.reversed().forEach { c ->
                main.stacks[to - 1].addFirst(c)
            }
        }

        return List(main.stacks.size) { i ->
            main.stacks[i].removeFirst()
        }.joinToString(separator = "")
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
