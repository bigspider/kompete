
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Array2DTests {
    val a = Array2D(4, 6, { x, y -> x + y } )

    @Test
    fun `test width and height` () {
        assertEquals(a.width, 4)
        assertEquals(a.height, 6)
    }

    @Test
    fun `test initial values, set and get` () {
        for (x in 0 until a.width)
            for (y in 0 until a.height)
                assertEquals(a[x, y], x + y)

        a[2, 3] = 10
        assertEquals(a[2, 3], 10)
    }
}