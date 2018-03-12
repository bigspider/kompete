//Things that do not (yet) have another place


//Generic exponentiation by squaring, given an (associative) product function
fun<T> pow(n: T, k: Int, unit: T, product: (T, T) -> T): T {
    var result = unit
    var multiplier = n
    var t = k
    while (true) {
        if ((t and 1) != 0)
            result = product(result, multiplier)
        t = t shr 1
        if (t == 0)
            break

        multiplier = product(multiplier, multiplier)
    }
    return result
}
