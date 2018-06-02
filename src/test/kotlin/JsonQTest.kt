import org.junit.Assert
import org.junit.Before
import org.junit.Test

class JsonQTest {
    private lateinit var jsonq: JsonQ

    @Before
    fun setUp() {
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("data.json")
        jsonq = JsonQ(inputStream)
    }

    @Test
    fun testSum() {
        val correctValue = 323000
        val sum = jsonq.from("products")
                .whereEq("user_id", 2)
                .sum("price")
        Assert.assertEquals(correctValue, sum.toInt())
    }

    @Test
    fun testAvg() {
        val correctValue = 80750
        val avg = jsonq.from("products")
                .whereEq("user_id", 2)
                .avg("price")
        Assert.assertEquals(correctValue, avg.toInt())
    }

    @Test
    fun testMax() {
        val correctValue = 150000
        val max = jsonq.from("products")
                .whereEq("user_id", 2)
                .max("price")
        Assert.assertEquals(correctValue, max.toInt())
    }

    @Test
    fun testMin() {
        val correctValue = 150
        val min = jsonq.from("products")
                .min("price")
        Assert.assertEquals(correctValue, min.toInt())
    }

    @Test
    fun testCount() {
        val correctValue = 6
        val size = jsonq.from("users")
                .size()
        Assert.assertEquals(correctValue, size)
    }
}