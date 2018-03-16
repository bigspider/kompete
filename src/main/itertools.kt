import kotlin.coroutines.experimental.buildSequence

private fun<T> MutableList<T>.swap(i: Int, j: Int) {
    val t = get(i)
    set(i, get(j))
    set(j, t)
}

fun<T> List<T>.permutations(): Sequence<List<T>> = buildSequence {
    if (size == 1) {
        yield(listOf(first()))
    } else if (size > 1) {
        val copy = toMutableList()
        for (i in 0 until size) {
            copy.swap(0, i)
            for (rest in copy.drop(1).permutations()) {
                yield(listOf(copy.first()) + rest)
            }
            copy.swap(0, i)
        }
    }
}

fun<T> List<T>.orderedTuples(k: Int): Sequence<List<T>> = buildSequence {
    when {
        k < 0 -> throw IllegalStateException("The parameter k must be positive.")
        k == 0 -> return@buildSequence
        k == 1 -> this@orderedTuples.forEach { yield(listOf(it)) }
        else -> {
            val copy = toList()
            for (i in 0 until size) {
                val remainingItems = (copy.subList(0, i) + copy.subList(i + 1, size))
                remainingItems.orderedTuples(k - 1).forEach { rest -> yield(listOf(copy[i]) + rest) }
            }
        }
    }
}

fun<T> List<T>.unorderedTuples(k: Int): Sequence<List<T>> = buildSequence {
    when {
        k < 0 -> throw IllegalStateException("The parameter k must be non-negative.")
        k == 0 -> return@buildSequence
        k == 1 -> this@unorderedTuples.forEach { yield(listOf(it)) }
        else -> for (i in 0..size - k) {
            subList(i + 1, size).unorderedTuples(k - 1).forEach {
                yield(listOf(get(i)) + it)
            }
        }
    }
}

