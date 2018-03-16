//support classes for multidimensional arrays
class Array2D<T> (val width: Int, val height: Int, initializer: (Int, Int) -> T) {
    val data: List<MutableList<T>> //internally stored as data[y][x]

    init {
        data = (0 until height).map { y -> (0 until width).map { x -> initializer(x, y) }.toMutableList() }
    }

    constructor(width: Int, height: Int, fillWith: T) : this( width, height, { x, y -> fillWith } )

    override fun toString() = data.joinToString(transform = { row -> row.joinToString(separator = " ") }, separator = "\n")

    operator fun get(x: Int, y: Int): T = data[y][x]

    operator fun set(x: Int, y: Int, value: T) {
        data[y][x] = value
    }

    operator fun get(xRange: IntProgression, yRange: IntProgression): Array2D<T> {
        val xCoords = xRange.toList()
        val yCoords = yRange.toList()

        return Array2D(xCoords.size, yCoords.size) { x, y -> get(xCoords[x], yCoords[y]) }
    }

    operator fun get(x: Int, yRange: IntProgression) = get(x..x, yRange)
    operator fun get(xRange: IntProgression, y: Int) = get(xRange, y..y)
}


fun<T> Array2D<out T>.flatten(): List<T> = data.flatMap { it }

fun<T> Array2D<out T>.transpose() = Array2D(height, width, { x, y -> get(y, x)})


inline fun<T> Array2D<out T>.forEach(action: (T) -> Unit) = data.forEach { row -> row.forEach(action) }

inline fun<T> Array2D<out T>.forEachIndexed(action: (Int, Int, T) -> Unit) =
        data.forEachIndexed { rowIdx, row -> row.forEachIndexed { colIdx, element -> action(rowIdx, colIdx, element)} }


inline fun<T, R> Array2D<out T>.map(crossinline transform: (T) -> R): Array2D<R> =
        Array2D(width, height, { x, y -> transform(get(x, y)) })


inline fun<T> Array2D<out T>.all(predicate: (T) -> Boolean): Boolean {
    forEach {
        if (!predicate(it))
            return false
    }

    return true
}

inline fun<T> Array2D<out T>.none(predicate: (T) -> Boolean): Boolean {
    forEach {
        if (predicate(it))
            return false
    }

    return true
}

inline fun<T> Array2D<out T>.any(predicate: (T) -> Boolean) = !none(predicate)



//Commodity functions for prefix sums of lists and integer 2D arrays

fun List<Int>.computePrefixSums(): List<Int> {
    val result = ArrayList<Int>(size)
    var curSum = 0
    for (i in 0 until size) {
        curSum += get(i)
        result[i] = curSum
    }
    return result
}

fun sumRangeFromPrefixSums(prefixSum: List<Int>, x1: Int, x2: Int): Int =
        if (x1 == 0)
            prefixSum[x2]
        else
            prefixSum[x2] - prefixSum[x1 - 1]


fun Array2D<Int>.computePrefixSums(): Array2D<Int> {
    val result = Array2D(width, height, 0)

    //Populate first row and column
    var temp = 0
    for (x in 0 until width) {
        temp += get(x, 0)
        result[x, 0] = temp
    }
    temp = get(0, 0)
    for (y in 1 until height) {
        temp += get(0, y)
        result[0, y] = temp
    }

    //Populate all the rest
    for (y in 1 until height)
        for (x in 1 until width)
            result[x, y] = result[x - 1, y] + result[x, y - 1] + get(x, y)
    return result
}

fun sumRangeFromPrefixSums(prefixSums: Array2D<Int>, x1: Int, y1: Int, x2: Int, y2: Int): Int {
    fun getSum(x: Int, y: Int): Int = if (x < 0 || y < 0) 0 else prefixSums[x, y]

    return getSum(x2, y2) - getSum(x2, y1 - 1) - getSum(x1 - 1, y2) + getSum(x1 - 1, y1 - 1)
}



//TODO: 3D arrays could be useful too (maybe also generic N-dimensional?)