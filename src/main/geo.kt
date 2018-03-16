@file:Suppress("NOTHING_TO_INLINE")

import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {
    fun dist(other: Point): Double = hypot((x - other.x).toDouble(), (y - other.y).toDouble())
}


//Returns the cross product of the vectors ab and ac
fun crossProduct(a: Point, b: Point, c: Point): Long =
        (b.x - a.x).toLong() * (c.y - a.y).toLong() - (c.x - a.x).toLong() * (b.y - a.y).toLong()

//Returns:
// 1 if a -> b -> c is a clockwise turn
// 0 if the points are collinear
// 1 if a -> b -> c is a counterclockwise turn
fun orientation(a: Point, b: Point, c: Point): Int {
    val cp = crossProduct(a, b, c)
    return when {
        cp > 0 ->  1
        cp < 0 -> -1
        else   ->  0
    }
}

fun isCwTurn(a: Point, b: Point, c: Point): Boolean = crossProduct(a, b, c) > 0
fun isCcwTurn(a: Point, b: Point, c: Point): Boolean = crossProduct(a, b, c) < 0
fun areCollinear(a: Point, b: Point, c: Point): Boolean = crossProduct(a, b, c) == 0L


////Assuming p, q, r are collinear, returns true if q is lies on the segment pr, false otherwise
//fun onSegment(p: Point, q: Point, r: Point) =
//        q.x in min(p.x, r.x)..max(p.x, r.x) && q.y in min(p.y, r.y)..max(p.y, r.y)

//Return true if [t1, t2] intersects [t3, t4]. Assumes t1 <= t2 and t3 <= t4
fun intervalsIntersect(t1: Int, t2: Int, t3: Int, t4: Int): Boolean = (t3 in t1..t2) || (t2 in t3..t4)

data class Segment(val p1: Point, val p2: Point) {
    val minX get() = min(p1.x, p2.x)
    val maxX get() = max(p1.x, p2.x)
    val minY get() = min(p1.y, p2.y)
    val maxY get() = max(p1.y, p2.y)

    fun intersects(other: Segment): Boolean {

        //test if bounding boxes intersect
        if (!intervalsIntersect(minX, maxX, other.minX, other.maxX))
            return false
        if (!intervalsIntersect(minY, maxY, other.minY, other.maxY))
            return false

        val o1 = orientation(p1, p2, other.p1)
        val o2 = orientation(p1, p2, other.p2)
        val o3 = orientation(other.p1, other.p2, p1)
        val o4 = orientation(other.p1, other.p2, p2)

        if (o1 == 0 && o2 == 0)
            return true //all points are collinear

        return o1 != o2 && o3 != o4
    }
}

data class Triangle(val p1: Point, val p2: Point, val p3: Point)

inline fun Triangle.contains(p: Point, strictly: Boolean = false): Boolean {
    val t1 = crossProduct(p1, p2, p)
    val t2 = crossProduct(p2, p3, p)
    val t3 = crossProduct(p3, p1, p)
    
    return if (strictly) {
        (t1 > 0 && t2 > 0 && t3 > 0) || (t1 < 0 && t2 < 0 && t3 < 0)
    } else {
        (t1 >= 0 && t2 >= 0 && t3 >= 0) || (t1 <= 0 && t2 <= 0 && t3 <= 0)
    }
}
