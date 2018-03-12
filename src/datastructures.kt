//Disjoint sets data structure (with union-by-rank and path compression)
class UnionFind {
    private val parentMap = hashMapOf<Int, Int>()
    private val rank = hashMapOf<Int, Int>()

    private fun parent(element: Int): Int = parentMap[element]!!

    fun makeSet(element: Int) {
        if (element !in parentMap) {
            parentMap[element] = element
            rank[element] = 0
        }
    }

    //Union by rank
    private fun link(el1: Int, el2: Int) {
        if (rank[el1]!! > rank[el2]!!){
            parentMap[el2] = parent(el1)
        } else {
            parentMap[el1] = parent(el2)
            if (rank[el1]!! == rank[el2]!!)
                rank[el2] = rank[el2]!! + 1
        }
    }

    fun find(element: Int): Int {
        makeSet(element)

        val p = parent(element)
        if (p == parent(p)) {
            return p
        } else {
            parentMap[element] = find(p) //path compression
            return parentMap[element]!!
        }
    }

    fun union(el1: Int, el2: Int) {
        makeSet(el1)
        makeSet(el2)

        link(find(el1), find(el2))
    }
}



class SegmentTree<T>(val size: Int, val zero: T, val composeFn: (T, T) -> T) {
    private val nodes: MutableList<T>
    private val roundedSize = Integer.highestOneBit(size * 2 - 1) //round up to closest power of two

    private val firstLeaf = roundedSize
    private val lastLeaf = firstLeaf + roundedSize - 1

    init {
        nodes = (1..2 * roundedSize).map { zero }.toMutableList()
    }

    constructor(elements: Collection<T>, zero: T, sum: (T, T) -> T): this(elements.size, zero, sum) {
        elements.forEachIndexed { i, el -> nodes[firstLeaf + i] = el }

        for (nodeIdx in firstLeaf - 1 downTo 1)
            fixElement(nodeIdx)
    }

    private fun parent(nodeIdx: Int) = nodeIdx / 2
    private fun leftChild(nodeIdx: Int) = nodeIdx * 2
    private fun rightChild(nodeIdx: Int) = nodeIdx * 2 + 1

    private fun fixElement(nodeIdx: Int) {
        assert(nodeIdx in 1..(firstLeaf - 1))
        nodes[nodeIdx] = composeFn(nodes[leftChild(nodeIdx)], nodes[rightChild(nodeIdx)])
    }

    private tailrec fun fixUp(nodeIdx: Int) {
        if (nodeIdx >= 1) {
            fixElement(nodeIdx)
            fixUp(parent(nodeIdx))
        }
    }

    operator fun get(i: Int) = nodes[firstLeaf + i]

    operator fun set(i: Int, newValue: T) {
        val nodeIdx = firstLeaf + i
        nodes[nodeIdx] = newValue
        fixUp(parent(nodeIdx))
    }

    fun toList(copy: Boolean = true): List<T> {
        val sublist = nodes.subList(firstLeaf, lastLeaf)
        return if (copy) sublist.toList() else sublist
    }

    fun queryInterval(i: Int, j: Int): T {
        fun queryInterval(nodeIdx: Int, begin: Int, end: Int): T {

            return when {
                j < begin || i > end -> zero
                i <= begin && end <= j -> nodes[nodeIdx]
                else -> {
                    val nodeSize = end - begin + 1
                    composeFn(queryInterval(leftChild(nodeIdx), begin, end - nodeSize / 2), queryInterval(rightChild(nodeIdx), begin + nodeSize / 2, end))
                }
            }
        }
        return queryInterval(1, 0, roundedSize - 1)
    }

}
