private sealed class Packet : Comparable<Packet> {
    companion object {
        fun of(input: String): Packet =
            of(input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                    .filter { it.isNotBlank() }
                    .filter { it != "," }
                    .iterator()
            )

        private fun of(input: Iterator<String>): Packet {
            val packets = mutableListOf<Packet>()
            while (input.hasNext()) {
                when (val symbol = input.next()) {
                    "]" -> return ListPacket(packets)
                    "[" -> packets.add(of(input))
                    else -> packets.add(IntPacket(symbol.toInt()))
                }
            }
            return ListPacket(packets)
        }
    }
}

private class IntPacket(val amount: Int) : Packet() {
    fun asList(): Packet = ListPacket(listOf(this))

    override fun compareTo(other: Packet): Int =
        when (other) {
            is IntPacket -> amount.compareTo(other.amount)
            is ListPacket -> asList().compareTo(other)
        }
}

private class ListPacket(val subPackets: List<Packet>) : Packet() {
    override fun compareTo(other: Packet): Int =
        when (other) {
            is IntPacket -> compareTo(other.asList())
            is ListPacket -> subPackets.zip(other.subPackets)
                .map { it.first.compareTo(it.second) }
                .firstOrNull { it != 0 } ?: subPackets.size.compareTo(other.subPackets.size)
        }
}

fun main() {

    fun parse(input: List<String>): Sequence<Packet> {
        return input.asSequence().filter { it.isNotBlank() }.map { Packet.of(it) }
    }

    fun part1(input: List<String>): Int {
        return parse(input)
                .chunked(2)
                .mapIndexed { index, (first, second) ->
                    if (first < second) index + 1 else 0
                }.sum()
    }

    fun part2(input: List<String>): Int {
        val packets = parse(input)
        val divider1 = Packet.of("[[2]]")
        val divider2 = Packet.of("[[6]]")
        val ordered = (packets + divider1 + divider2).sorted()
        return (ordered.indexOf(divider1) + 1) * (ordered.indexOf(divider2) + 1)
    }

    val input = readInput("inputs/Day13")
    println(part1(input))
    println(part2(input))
}
