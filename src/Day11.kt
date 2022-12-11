import kotlin.math.floor

fun main() {

    data class Monkey(
        val items: MutableList<Long>,
        val operation: String,
        val divisor: Long,
        val trueMonkey: Int,
        val falseMonkey: Int,
        var inspections: Long = 0
    )

    fun parse(input: List<String>) =
        input
            .joinToString("\n")
            .split("\n\n")
            .map {
                val (itemsInput, operationInput, testInput, trueInput, falseInput) = it.split("\n").drop(1)
                val items = itemsInput.substringAfter("items: ")
                    .split(",")
                    .map { s -> s.trim().toLong() }
                    .toMutableList()

                val operation = operationInput.substringAfter("new =").trim()
                val divisibleBy = testInput.substringAfter("by ").toLong()
                val trueMonkey = trueInput.substringAfter("monkey ").toInt()
                val falseMonkey = falseInput.substringAfter("monkey ").toInt()

                Monkey(items, operation, divisibleBy, trueMonkey, falseMonkey)
            }

    fun operation(operation: String, item: Long): Long {
        val expression = operation.replace("old", item.toString())
        val (n1, op, n2) = expression.split(" ")

        return when (op) {
            "+" -> n1.toLong() + n2.toLong()
            "*" -> n1.toLong() * n2.toLong()
            else -> error("wrong operation $op")
        }
    }


    fun common(input: List<String>, maxIterations: Int, nextWorryLevel: (operationResult: Long, magicNumber: Long) -> Long): Long {
        val monkeys = parse(input)
        val magicNumber = monkeys.map { it.divisor }.reduce { acc, i ->
            acc * i
        }

        repeat(maxIterations) {
            monkeys.forEach {
                it.items.forEach { item ->
                    it.inspections++

                    val operationResult = operation(it.operation, item)
                    val worryLevel = nextWorryLevel(operationResult, magicNumber)

                    if (worryLevel divisibleBy it.divisor) {
                        monkeys[it.trueMonkey].items += worryLevel
                    } else {
                        monkeys[it.falseMonkey].items += worryLevel
                    }
                }

                it.items.clear()
            }
        }

        return monkeys
            .map { it.inspections }
            .sorted()
            .takeLast(2)
            .reduce { acc, i ->
                acc * i
            }
    }

    fun part1(input: List<String>): Long {
        return common(input, 20) { operationResult, _ ->
            floor(operationResult / 3.0).toLong()
        }
    }

    fun part2(input: List<String>): Long {
        return common(input, 10000) { operationResult, magicNumber ->
            operationResult % magicNumber
        }
    }

    val input = readInput("inputs/Day11")
    println(part1(input))
    println(part2(input))
}

infix fun Long.divisibleBy(divisor: Long) = this % divisor == 0L
