fun main() {

    fun List<Char>.findClosing(start: Int): Int {
        this.forEachIndexed { i, c ->
            if (i > start && c == ']') {
                return i
            }
        }

        error("closing ] not found")
    }

    fun parse(s: String): Any {
        var i = 0
        fun next(): Any {
            if (s[i] == '[') {
                i++
                val res = ArrayList<Any>()
                while(true) {
                    if (s[i] == ']') {
                        i++
                        return res
                    }
                    res.add(next())
                    if (s[i] == ']') {
                        i++
                        return res
                    }
                    check(s[i] == ',')
                    i++
                }
            }
            check(s[i] in '0'..'9')
            var num = 0
            while (s[i] in '0'..'9') {
                num = num * 10 + (s[i] - '0')
                i++
            }
            return num
        }
        val res = next()
        check(i == s.length)
        return res
    }

    fun part1(input: List<String>): Int {
        val split = input.filterNot { it.isEmpty() }
        val map = split.map { parse(it) }



        return 0
    }

    fun cmp(a: Any, b: Any): Int {
        if (a is Int && b is Int) return a.compareTo(b)
        if (a is List<*> && b is List<*>) {
            var i = 0
            while (i < a.size && i < b.size) {
                val c = cmp(a[i]!!, b[i]!!)
                if (c != 0) return c
                i++
            }
            return a.size.compareTo(b.size)
        }
        if (a is Int) return cmp(listOf(a), b)
        if (b is Int) return cmp(a, listOf(b))
        error("!!!")
    }

    val d1 = parse("[[2]]")
    val d2 = parse("[[6]]")

    val input = readInput("inputs/Day13")
    val list = (input.mapNotNull { s ->
        if (s != "") parse(s) else null
    } + listOf(d1, d2)).sortedWith { a, b -> cmp(a, b) }

    val i1 = list.indexOf(d1) + 1
    val i2 = list.indexOf(d2) + 1
    println(i1 * i2)

    fun part2(input: List<String>): Int {
        return 0
    }

    //val input = readInput("inputs/test")
    println(part1(input))
    println(part2(input))
}
