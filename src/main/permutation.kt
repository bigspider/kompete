//Permutation of integers 0, 1, ... size - 1
class Permutation(val numbers: List<Int>) {
    val size = numbers.size

    companion object {
        fun identity(size: Int): Permutation = Permutation((0 until size).toList())
        fun reverse(size: Int): Permutation = Permutation((size - 1 downTo 0).toList())
    }

    operator fun<T> invoke(list: List<T>): List<T> =
            if (list.size != size)
                throw IllegalArgumentException("Size mismatch")
            else
                numbers.map { list[it] }

    override fun toString() = numbers.joinToString(" ", "(", ")")

    //Returns the permutation that first applies other, and than this.
    operator fun times(other: Permutation) = Permutation(other.numbers.map { numbers[it] })

    //Exponentiation by squaring
    fun pow(n: Int): Permutation = pow(this, n, identity(size), { x: Permutation, y: Permutation -> x * y })

    //TODO
    fun inverse(): Permutation {
        throw NotImplementedError()
    }

    //TODO: cycle decomposition


}